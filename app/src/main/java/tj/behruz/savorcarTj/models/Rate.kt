package tj.behruz.savorcarTj.models

import com.google.gson.annotations.SerializedName

data class Rate(
    @SerializedName("1")
    val veryBad: String,
    @SerializedName("2")
    val bad: String,
    @SerializedName("3")
    val nodBad: String,
    @SerializedName("4")
    val good: String,
    @SerializedName("5")
    val excellent: String
)