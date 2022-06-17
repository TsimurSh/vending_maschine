package de.steklopod

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient

@ActiveProfiles("test")
@AutoConfigureWebTestClient
@SpringBootTest
internal class RouterTest(@Autowired private var webTestClient: WebTestClient) {

    @Test
    fun `NOT FOUND http status`() {
        webTestClient.get().uri("/anything/123")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `STATIC content - should return logo`() {
        webTestClient.get().uri("/static/img/gateway_flow.png")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.IMAGE_PNG)
    }

}
