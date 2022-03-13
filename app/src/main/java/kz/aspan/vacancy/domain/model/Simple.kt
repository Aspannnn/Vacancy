package kz.aspan.vacancy.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Simple(
    @SerializedName("count")
    val count: Int,
    @SerializedName("data")
    val data: List<SimpleData>,
) : Serializable
