package tj.behruz.savorcarTj.models

data class LoginModel(
    val code: Int,
    val message:String,
    val data:LoginPayload
)


data class LoginPayload(val is_exist: Boolean, val code: Int)