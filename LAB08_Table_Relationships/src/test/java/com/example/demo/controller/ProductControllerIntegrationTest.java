package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductDetailRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:lab8tests;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa", "spring.datasource.password=",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
@Transactional
class ProductControllerIntegrationTest {

    @Autowired private MockMvc mvc;
    @Autowired private ProductRepository products;
    @Autowired private ProductDetailRepository details;
    @Autowired private ReviewRepository reviews;
    @Autowired private EntityManager entityManager;

    @Test
    void createsReadsUpdatesAndDeletesProductWithItsChildren() throws Exception {
        mvc.perform(get("/products/add")).andExpect(status().isOk())
                .andExpect(content().string(containsString("reviews[0].reviewer")));

        mvc.perform(form("/products/save")
                .param("reviews[0].reviewer", "Lab reviewer")
                .param("reviews[0].rating", "5")
                .param("reviews[0].comment", "Good product"))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/products"));
        flushAndClear();
        Product product = products.findAll().get(0);
        Long id = product.getId();
        Long detailId = product.getDetail().getId();
        assertEquals(1, product.getReviews().size());
        Long reviewId = product.getReviews().get(0).getId();
        assertEquals(id, product.getReviews().get(0).getProduct().getId());
        assertNotNull(product.getReviews().get(0).getReviewDate());

        mvc.perform(get("/products")).andExpect(status().isOk())
                .andExpect(content().string(containsString("Lab test product")))
                .andExpect(content().string(containsString("900.00")));
        mvc.perform(get("/products/edit/{id}", id)).andExpect(status().isOk());

        mvc.perform(form("/products/update/" + id).param("detail.warranty", "2 Years"))
                .andExpect(status().is3xxRedirection());
        flushAndClear();
        product = products.findById(id).orElseThrow();
        assertEquals(detailId, product.getDetail().getId());
        assertEquals("2 Years", product.getDetail().getWarranty());
        assertEquals(reviewId, product.getReviews().get(0).getId());

        mvc.perform(get("/products/delete/{id}", id)).andExpect(status().isOk());
        mvc.perform(post("/products/delete/{id}", id)).andExpect(status().is3xxRedirection());
        flushAndClear();
        assertFalse(products.existsById(id));
        assertFalse(details.existsById(detailId));
        assertFalse(reviews.existsById(reviewId));
    }

    @Test
    void blankOptionalReviewDoesNotCreateEmptyReview() throws Exception {
        mvc.perform(form("/products/save").param("reviews[0].reviewer", "")
                .param("reviews[0].rating", "5").param("reviews[0].comment", ""))
                .andExpect(status().is3xxRedirection());
        flushAndClear();
        assertEquals(1, products.count());
        assertEquals(1, details.count());
        assertEquals(0, reviews.count());
    }

    @Test
    void rejectsInvalidReviewWithoutSavingProduct() throws Exception {
        mvc.perform(form("/products/save").param("reviews[0].reviewer", "Reviewer")
                .param("reviews[0].rating", "6")).andExpect(status().isBadRequest());
        assertEquals(0, products.count());
    }

    @Test
    void rejectsNegativePriceAndInvalidNumber() throws Exception {
        mvc.perform(post("/products/save").param("name", "Invalid")
                .param("category", "Test").param("brand", "Test")
                .param("stock", "1").param("price", "-1"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/products/save").param("price", "invalid"))
                .andExpect(status().isBadRequest());
        assertEquals(0, products.count());
    }

    @Test
    void missingProductReturnsNotFoundForReadUpdateAndDelete() throws Exception {
        mvc.perform(get("/products/edit/999999")).andExpect(status().isNotFound());
        mvc.perform(get("/products/delete/999999")).andExpect(status().isNotFound());
        mvc.perform(form("/products/update/999999")).andExpect(status().isNotFound());
        mvc.perform(post("/products/delete/999999")).andExpect(status().isNotFound());
    }

    @Test
    void formCannotChooseEntityIdsOrRelationshipOwners() throws Exception {
        mvc.perform(form("/products/save").param("id", "999999")
                .param("detail.id", "999999").param("detail.product.id", "999999"))
                .andExpect(status().is3xxRedirection());
        flushAndClear();
        Product product = products.findAll().get(0);
        assertNotEquals(999999L, product.getId());
        assertNotEquals(999999L, product.getDetail().getId());
        assertEquals(product.getId(), product.getDetail().getProduct().getId());
    }

    private MockHttpServletRequestBuilder form(String url) {
        return post(url).param("name", "Lab test product")
                .param("category", "Electronics").param("brand", "Test brand")
                .param("price", "1000").param("stock", "3").param("discountType", "MEMBER")
                .param("detail.description", "Test description").param("detail.weight", "1.5");
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
