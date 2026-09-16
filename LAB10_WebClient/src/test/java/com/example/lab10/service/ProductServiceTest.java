package com.example.lab10.service;

import com.example.lab10.model.Product;
import com.example.lab10.repository.ProductRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

class ProductServiceTest {

    private final ProductService service = new ProductService(new ProductRepository());

    @Test
    void getByIdReturnsProductOrError() {
        StepVerifier.create(service.getById("1"))
                .expectNextMatches(p -> p.getName().contains("673380048-6"))
                .verifyComplete();
        StepVerifier.create(service.getById("missing"))
                .verifyErrorMessage("Product not found: missing");
    }

    @Test
    void saveGeneratesUuidOnSubscriptionAndPersistsProduct() {
        Product product = new Product(null, "Notebook", "Stationery", "Test", 10, 50.0, "NONE");
        Mono<Product> save = service.save(product);
        assertThat(product.getId()).isNull();

        StepVerifier.create(save)
                .assertNext(saved -> {
                    assertThat(saved).isSameAs(product);
                    assertThat(UUID.fromString(saved.getId()).toString()).isEqualTo(saved.getId());
                })
                .verifyComplete();
        StepVerifier.create(service.getById(product.getId()))
                .expectNext(product).verifyComplete();
    }

    @Test
    void savePreservesProvidedId() {
        Product product = new Product("custom-id", "Notebook", "Stationery", "Test", 10, 50.0, "NONE");
        StepVerifier.create(service.save(product)).expectNext(product).verifyComplete();
        StepVerifier.create(service.getById("custom-id")).expectNext(product).verifyComplete();
    }

    @Test
    void getAllAndCategoryReturnMatchingProducts() {
        Product product = new Product("4", "Notebook", "Stationery", "Test", 10, 50.0, "NONE");
        StepVerifier.create(service.save(product)).expectNext(product).verifyComplete();
        StepVerifier.create(service.getAll()).expectNextCount(4).verifyComplete();
        StepVerifier.create(service.getByCategory("stationery"))
                .expectNext(product).verifyComplete();
        StepVerifier.create(service.getByCategory("missing")).verifyComplete();
    }

    @Test
    void deleteRemovesProductAndCompletesWithoutValue() {
        StepVerifier.create(service.delete("1")).verifyComplete();
        StepVerifier.create(service.getById("1"))
                .verifyErrorMessage("Product not found: 1");
        StepVerifier.create(service.delete("missing")).verifyComplete();
    }

    @Test
    void discountedPriceUsesProductDiscountAndPropagatesMissingProductError() {
        StepVerifier.create(service.getDiscountedPrice("1")).expectNext(35910.0).verifyComplete();
        StepVerifier.create(service.getDiscountedPrice("2")).expectNext(49900.0).verifyComplete();
        StepVerifier.create(service.getDiscountedPrice("3")).expectNext(23920.0).verifyComplete();
        StepVerifier.create(service.getDiscountedPrice("missing"))
                .verifyErrorMessage("Product not found: missing");
    }
}
