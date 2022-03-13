package kz.aspan.vacancy.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SimpleData(
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: String,
    @SerializedName("numberOfVacancies")
    val numberOfVacancies: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("subTitle")
    val subTitle: String?
) : Serializable
