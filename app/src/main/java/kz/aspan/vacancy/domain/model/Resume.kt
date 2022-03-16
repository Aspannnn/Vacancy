package kz.aspan.vacancy.domain.model


import com.google.gson.annotations.SerializedName

data class Resume(
    @SerializedName("id")
    val id: String,
    @SerializedName("resumeUrl")
    val resumeUrl: String
)