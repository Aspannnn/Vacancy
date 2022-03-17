package kz.aspan.vacancy.domain.model


import com.google.gson.annotations.SerializedName

data class Student(
    @SerializedName("avgGPA")
    val avgGPA: String?,
    @SerializedName("email")
    val email: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("photo")
    val photo: String?
)