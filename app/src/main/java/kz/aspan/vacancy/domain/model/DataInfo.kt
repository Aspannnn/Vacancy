package kz.aspan.vacancy.domain.model

import com.google.gson.annotations.SerializedName

data class DataInfo(
    @SerializedName("count")
    val count: Int,
    @SerializedName("data")
    val data: List<Data>
)
