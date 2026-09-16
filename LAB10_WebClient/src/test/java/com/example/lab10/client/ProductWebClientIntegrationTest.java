package com.example.lab10.client;

import com.example.lab10.model.Product;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

// เปิด HTTP server บน port สุ่ม แยกจากโปรแกรมที่ใช้แคปภาพ
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductWebClientIntegrationTest {

    @LocalServerPort
    private int port;

    private ProductWebClient client;
    private final Duration timeout = Duration.ofSeconds(10);

    @BeforeEach
    void setUp() {
        client = new ProductWebClient();
        // เปลี่ยนปลายทางเฉพาะ client ใน test โดยคง Template ของอาจารย์ไว้
        ReflectionTestUtils.setField(client, "client", WebClient.create("http://localhost:" + port));
    }

    @Test
    void readsProductAndAllSeedProductsOverHttp() {
        StepVerifier.create(client.getProductById("1"))
                .assertNext(product -> {
                    assertThat(product.getId()).isEqualTo("1");
                    assertThat(product.getName()).contains("ปวัฒน์ ปัดทุมมา", "673380048-6", "SEC 1");
                })
                .expectComplete().verify(timeout);
        StepVerifier.create(client.getAllProducts().map(Product::getId).collectList())
                .assertNext(ids -> assertThat(ids).containsExactlyInAnyOrder("1", "2", "3"))
                .expectComplete().verify(timeout);
    }

    @Test
    void categorySupportsCaseInsensitiveSearchAndEmptyFallback() {
        StepVerifier.create(client.getByCategory("electronics"))
                .expectNextCount(3).expectComplete().verify(timeout);
        StepVerifier.create(client.getByCategory("missing").map(Product::getName)
                        .defaultIfEmpty("No products"))
                .expectNext("No products").expectComplete().verify(timeout);
    }

    @Test
    void returnsAllThreeDiscountPrices() {
        StepVerifier.create(client.getDiscountedPrice("1"))
                .expectNext(35910.0).expectComplete().verify(timeout);
        StepVerifier.create(client.getDiscountedPrice("2"))
                .expectNext(49900.0).expectComplete().verify(timeout);
        StepVerifier.create(client.getDiscountedPrice("3"))
                .expectNext(23920.0).expectComplete().verify(timeout);
    }

    @Test
    void createsReadsAndDeletesProductThroughWebClient() {
        Product product = new Product(null, "WebClient test", "TestCategory", "Test", 2, 100.0, "MEMBER");
        String[] createdId = new String[1];
        try {
            StepVerifier.create(client.createProduct(product))
                    .assertNext(saved -> {
                        createdId[0] = saved.getId();
                        assertThat(saved.getId()).isNotBlank();
                        assertThat(saved.getName()).isEqualTo("WebClient test");
                    })
                    .expectComplete().verify(timeout);
            StepVerifier.create(client.getProductById(createdId[0])
                            .flatMap(saved -> client.getDiscountedPrice(saved.getId())))
                    .expectNext(90.0).expectComplete().verify(timeout);
            StepVerifier.create(client.deleteProduct(createdId[0]))
                    .expectComplete().verify(timeout);
            StepVerifier.create(client.getByCategory("TestCategory"))
                    .expectComplete().verify(timeout);
            StepVerifier.create(client.getProductById(createdId[0]))
                    .expectErrorSatisfies(error -> {
                        assertThat(error).isInstanceOf(WebClientResponseException.class);
                        assertThat(((WebClientResponseException) error).getStatusCode().value()).isEqualTo(500);
                    }).verify(timeout);
        } finally {
            if (createdId[0] != null) {
                StepVerifier.create(client.deleteProduct(createdId[0])).expectComplete().verify(timeout);
            }
        }
    }

    @Test
    void missingProductIsAnErrorRatherThanEmptyFallback() {
        // ตาม Template: Service ส่ง RuntimeException เมื่อไม่พบสินค้า จึงได้ HTTP 500
        StepVerifier.create(client.getProductById("missing").map(Product::getName)
                        .defaultIfEmpty("No product"))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(WebClientResponseException.class);
                    assertThat(((WebClientResponseException) error).getStatusCode().value()).isEqualTo(500);
                }).verify(timeout);
    }
}
