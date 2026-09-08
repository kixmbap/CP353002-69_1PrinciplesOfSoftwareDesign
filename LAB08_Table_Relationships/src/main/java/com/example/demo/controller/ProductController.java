package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.model.ProductDetail;
import com.example.demo.model.Review;
import com.example.demo.service.ProductService;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @InitBinder("product")
    public void configureBinding(WebDataBinder binder) {
        // IDs and relationship owners are assigned by the service, never by form input.
        binder.setAllowedFields("name", "category", "brand", "stock", "price", "discountType",
                "detail.description", "detail.warranty", "detail.weight", "detail.dimensions",
                "detail.manufacturedCountry", "reviews[0].reviewer", "reviews[0].rating",
                "reviews[0].comment");
    }

    @GetMapping({"", "/"})
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        Product product = new Product();
        product.setDetail(new ProductDetail());
        Review review = new Review();
        review.setRating(5);
        product.addReview(review);
        model.addAttribute("product", product);
        return "products/add";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product, RedirectAttributes redirect) {
        productService.createProduct(product);
        redirect.addFlashAttribute("message", "เพิ่มสินค้าสำเร็จ");
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product.getDetail() == null) {
            product.setDetail(new ProductDetail());
        }
        model.addAttribute("product", product);
        return "products/edit";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute("product") Product product,
                                RedirectAttributes redirect) {
        productService.updateProduct(id, product);
        redirect.addFlashAttribute("message", "แก้ไขสินค้าสำเร็จ");
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String showDeleteForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "products/delete";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirect) {
        productService.deleteProduct(id);
        redirect.addFlashAttribute("message", "ลบสินค้าสำเร็จ");
        return "redirect:/products";
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String productNotFound() {
        return "Product not found";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String invalidProduct(IllegalArgumentException exception) {
        return exception.getMessage();
    }
}
