package pl.oryeh.controller

import pl.oryeh.model.Scope.BUYER
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@SpringBootTest
@AutoConfigureMockMvc
internal class UserControllerTest(@Autowired private val mockMvc: MockMvc) {
    private val email = "Kaltovich@Dmitry.test"
    private val baseUrl = "/user"

    @Test
    fun `Register user & fetch Test`() {
        mockMvc.perform(
            post(baseUrl)
                .content("""{ "email": "$email", "password" : "BatMan", "role": "$BUYER" }""")
                .contentType(APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$").isNotEmpty)
            .andExpect(jsonPath("$.access_token").isNotEmpty)
    }

}
