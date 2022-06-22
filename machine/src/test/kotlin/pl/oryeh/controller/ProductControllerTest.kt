package pl.oryeh.controller

import de.steklopod.TestHelper.SELLER_TOKEN
import de.steklopod.TestHelper.patchWithTokenInHeader
import de.steklopod.TestHelper.postWithTokenInHeader
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@SpringBootTest
@AutoConfigureMockMvc
internal class ProductControllerTest(@Autowired private val mockMvc: MockMvc) {
    private val baseUrl = "/product"

    @Test
    fun `get All Test`() {
        mockMvc.perform(
            get(baseUrl)
                .param("sort", "id,desc")
        )
            .andDo(print())
            .andExpect(status().isOk)
//            .andExpect(jsonPath("$", hasSize<Product>(3)))
            .andExpect(jsonPath("$[0].cost").value("130"))
            .andExpect(jsonPath("$[1].cost").value("125"))
            .andExpect(jsonPath("$[2].cost").value("100"))
    }

    @Test
    fun `Create Test`() {
        mockMvc.perform(
            postWithTokenInHeader("$baseUrl/mamba", token = SELLER_TOKEN)
                .content("15")
        )
            .andDo(print())
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").isNotEmpty)
    }

    @Test
    fun `Create invalid count Test`() {
        mockMvc.perform(
            postWithTokenInHeader("$baseUrl/mamba", token = SELLER_TOKEN)
                .content("13")
        )
            .andDo(print())
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Update cost Test`() {
        mockMvc.perform(
            patchWithTokenInHeader("$baseUrl/cost/-3", token = SELLER_TOKEN)
                .content("100")
        )
            .andDo(print())
            .andExpect(status().isOk)
    }

    @Test
    fun `Update amount Test`() {
        mockMvc.perform(
            patchWithTokenInHeader("$baseUrl/amount/-3", token = SELLER_TOKEN)
                .content("0")
        )
            .andDo(print())
            .andExpect(status().isOk)
    }



}
