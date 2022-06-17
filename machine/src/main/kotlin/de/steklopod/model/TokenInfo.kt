package de.steklopod.model

import com.fasterxml.jackson.annotation.JsonProperty


data class TokenInfo(
    @JsonProperty("access_token")
    val accessToken: String,
    val token: String = accessToken.substringAfter("Bearer "),
)
