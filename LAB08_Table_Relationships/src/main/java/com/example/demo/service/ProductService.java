package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.model.ProductDetail;
import com.example.demo.model.Review;
import com.example.demo.repository.ProductRepository;
import com.example.demo.strategy.DiscountContext;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final DiscountContext discountContext;

    public ProductService(ProductRepository productRepository, DiscountContext discountContext) {
        this.productRepository = productRepository;
        this.discountContext = discountContext;
    }

    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        products.forEach(this::prepareForDisplay);
        return products;
    }

    public Product getProductById(Long id) {
        Product product = findProduct(id);
        prepareForDisplay(product);
        return product;
    }

    @Transactional
    public Product createProduct(Product form) {
        validateProduct(form);
        Product product = new Product();
        copyProductFields(form, product);
        copyDetail(form.getDetail(), product);
        for (Review submitted : form.getReviews()) {
            // The add form includes an optional first review with a default rating.
            if (submitted == null || (!hasText(submitted.getReviewer()) && !hasText(submitted.getComment()))) {
                continue;
            }
            if (!hasText(submitted.getReviewer()) || submitted.getRating() == null
                    || submitted.getRating() < 1 || submitted.getRating() > 5) {
                throw new IllegalArgumentException("Review requires a reviewer and a rating from 1 to 5");
            }
            Review review = new Review();
            review.setReviewer(submitted.getReviewer());
            review.setRating(submitted.getRating());
            review.setComment(submitted.getComment());
            product.addReview(review);
        }
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product form) {
        Product product = findProduct(id);
        validateProduct(form);
        // Update the managed entity, retaining existing detail identity and reviews.
        copyProductFields(form, product);
        copyDetail(form.getDetail(), product);
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.delete(findProduct(id));
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
    }

    private void prepareForDisplay(Product product) {
        product.setDiscountedPrice(discountContext.calculateFinalPrice(
                product.getDiscountType(), product.getPrice()));
        // Load the count while the transaction is open for use by the list template.
        product.getReviews().size();
    }

    private void copyProductFields(Product source, Product target) {
        target.setName(source.getName());
        target.setCategory(source.getCategory());
        target.setBrand(source.getBrand());
        target.setStock(source.getStock());
        target.setPrice(source.getPrice());
        target.setDiscountType(source.getDiscountType() == null ? "NONE" : source.getDiscountType());
    }

    private void copyDetail(ProductDetail source, Product product) {
        if (source == null) {
            return;
        }
        if (product.getDetail() == null) {
            product.setDetail(new ProductDetail());
        }
        ProductDetail target = product.getDetail();
        target.setDescription(source.getDescription());
        target.setWarranty(source.getWarranty());
        target.setWeight(source.getWeight());
        target.setDimensions(source.getDimensions());
        target.setManufacturedCountry(source.getManufacturedCountry());
    }

    private void validateProduct(Product product) {
        if (!hasText(product.getName()) || !hasText(product.getCategory()) || !hasText(product.getBrand())) {
            throw new IllegalArgumentException("Name, category and brand are required");
        }
        if (product.getPrice() == null || !Double.isFinite(product.getPrice()) || product.getPrice() < 0
                || product.getStock() == null || product.getStock() < 0) {
            throw new IllegalArgumentException("Price and stock must be non-negative");
        }
        if (product.getDiscountType() != null
                && !List.of("NONE", "MEMBER", "SEASONAL").contains(product.getDiscountType())) {
            throw new IllegalArgumentException("Unknown discount type");
        }
        Double weight = product.getDetail() == null ? null : product.getDetail().getWeight();
        if (weight != null && (!Double.isFinite(weight) || weight < 0)) {
            throw new IllegalArgumentException("Weight must be non-negative");
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
