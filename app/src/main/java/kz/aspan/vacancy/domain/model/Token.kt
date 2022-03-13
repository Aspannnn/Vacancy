package kz.aspan.vacancy.domain.model

import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("token")
    val token: String
)