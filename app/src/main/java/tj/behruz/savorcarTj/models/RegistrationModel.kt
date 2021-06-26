package tj.behruz.savorcarTj.models

data class RegistrationModel(
    val code: Int,
    val data: RegistrationPayload,
    val message: String
)

data class RegistrationPayload(
    val birthday: String,

    val cname: String,
    val email: String,
    val gander: String,
    val gos_nomer: String,
    val name: String,
    val phone: String,
    val user_id: Int,
    val pimg:String,
    val level:String,
    val car:Car?

)