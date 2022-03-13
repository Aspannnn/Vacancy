package kz.aspan.vacancy.domain.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("Login")
    val login: String,
    @SerializedName("RoleId")
    val roleId: String,
    @SerializedName("Password")
    val password: String
)
