package com.example.lab10.repository;

import com.example.lab10.model.Product;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ProductRepositoryTest {

    private final ProductRepository repository = new ProductRepository();

    @Test
    void findAllReturnsThreeSeedProducts() {
        StepVerifier.create(repository.findAll())
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void saveAndLookupRunWhenSubscribed() {
        Product product = new Product("4", "Notebook", "Stationery", "Test", 10, 50.0, "NONE");
        Mono<Product> lookup = repository.findById("4");
        Mono<Product> save = repository.save(product);

        StepVerifier.create(lookup).verifyComplete();
        StepVerifier.create(save).expectNext(product).verifyComplete();
        StepVerifier.create(lookup).expectNext(product).verifyComplete();
    }

    @Test
    void deleteRunsWhenSubscribedAndCompletesWithoutValue() {
        Mono<Void> delete = repository.deleteById("1");

        StepVerifier.create(repository.findById("1")).expectNextCount(1).verifyComplete();
        StepVerifier.create(delete).verifyComplete();
        StepVerifier.create(repository.findById("1")).verifyComplete();
        StepVerifier.create(repository.deleteById("missing")).verifyComplete();
    }

    @Test
    void categoryIgnoresCaseAndExcludesOtherCategories() {
        Product product = new Product("4", "Notebook", "Stationery", "Test", 10, 50.0, "NONE");
        StepVerifier.create(repository.save(product)).expectNext(product).verifyComplete();

        StepVerifier.create(repository.findByCategory("electronics"))
                .expectNextMatches(p -> "Electronics".equals(p.getCategory()))
                .expectNextMatches(p -> "Electronics".equals(p.getCategory()))
                .expectNextMatches(p -> "Electronics".equals(p.getCategory()))
                .verifyComplete();
        StepVerifier.create(repository.findByCategory("Stationery"))
                .expectNext(product).verifyComplete();
        StepVerifier.create(repository.findByCategory("missing")).verifyComplete();
    }
}
