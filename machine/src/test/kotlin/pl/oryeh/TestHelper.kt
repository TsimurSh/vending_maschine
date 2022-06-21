package pl.oryeh

import org.mockito.Mockito
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

internal object TestHelper {
    fun <T> any(): T = Mockito.any()

    const val SELLER_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzY29wZSI6IltTRUxMRVJdIiwiYXVkIjoiZC5hLmthbHRvdmljaEBnbWFpbC5jb20iLCJzdWIiOiItMSIsImlhdCI6MTY0ODQwODcwNywiZXhwIjoxNjQ5NTMxOTA3fQ.NQN3MQ1IGYKg1rwowKWoLdGgd7iFuN84qZdqc4nVVmFAIoI1q6oMkmeYt5dizZ-WwJsTjD1JyshUjPKRfMdCXA"
    const val BUYER_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzY29wZSI6IltCVVlFUl0iLCJhdWQiOiJzdGVrbG9wb2RAZ21haWwuY29tIiwic3ViIjoiLTIiLCJpYXQiOjE2NDg0MDg3MDcsImV4cCI6MTY0OTUzMTkwN30.AIxD3WNdfWRdwMQiv0Tan5tZr_k3w4NJoHJYGwEhIlKqLpuBhURsuDhcsrP-vuodFYIZwV7RtSP9cvH8GuPhPA"

    fun postWithTokenInHeader(url: String, token: String = BUYER_TOKEN): MockHttpServletRequestBuilder = MockMvcRequestBuilders
        .post(url).contentType(APPLICATION_JSON).header(AUTHORIZATION, token)

    fun getWithTokenInHeader(url: String, token: String = BUYER_TOKEN): MockHttpServletRequestBuilder = MockMvcRequestBuilders
        .get(url).header(AUTHORIZATION, token)

    fun patchWithTokenInHeader(url: String, token: String = BUYER_TOKEN): MockHttpServletRequestBuilder = MockMvcRequestBuilders
        .patch(url).contentType(APPLICATION_JSON).header(AUTHORIZATION, token)

    fun deleteWithTokenInHeader(url: String, token: String = BUYER_TOKEN): MockHttpServletRequestBuilder = MockMvcRequestBuilders
        .delete(url).contentType(APPLICATION_JSON).header(AUTHORIZATION, token)
}
