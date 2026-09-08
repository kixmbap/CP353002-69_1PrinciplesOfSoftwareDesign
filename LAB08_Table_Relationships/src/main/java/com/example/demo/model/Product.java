package com.example.demo.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String category;
    private String brand;
    private Integer stock;
    private Double price;
    private String discountType = "NONE";

    // Product owns the 1:1 relationship through detail_id.
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "detail_id", unique = true)
    private ProductDetail detail;

    // Review owns the foreign key; mappedBy refers to Review.product.
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public ProductDetail getDetail() {
        return detail;
    }

    public void setDetail(ProductDetail detail) {
        if (this.detail != null) {
            this.detail.setProduct(null);
        }
        this.detail = detail;
        if (detail != null) {
            detail.setProduct(this);
        }
    }

    public List<Review> getReviews() {
        return reviews;
    }

    // Use these helpers to keep both sides of the relationship in sync.
    public void addReview(Review review) {
        reviews.add(review);
        review.setProduct(this);
    }

    public void removeReview(Review review) {
        if (reviews.remove(review)) {
            review.setProduct(null);
        }
    }
}
