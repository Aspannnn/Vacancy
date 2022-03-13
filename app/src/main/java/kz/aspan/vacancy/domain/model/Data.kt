package kz.aspan.vacancy.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Data(
    @SerializedName("address")
    val address: String,
    @SerializedName("company")
    val company: String,
    @SerializedName("companyDescription")
    val companyDescription: String,
    @SerializedName("conditions")
    val conditions: String,
    @SerializedName("contacts")
    val contacts: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("industry")
    val industry: String,
    @SerializedName("jobType")
    val jobType: String,
    @SerializedName("profession")
    val profession: String,
    @SerializedName("site")
    val site: String,
    @SerializedName("skills")
    val skills: List<String>,
    @SerializedName("сity")
    val сity: String?
) : Serializable