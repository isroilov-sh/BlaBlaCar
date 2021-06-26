package tj.behruz.savorcarTj.models

data class UserInfoResponse(val code: Int, val message: String, val data: UserInfoPayload

)

data class UserInfoPayload(val level: String, val total_rate: Int, val rate: Rate, val user_info: UserInfo, val reviews: List<Any>, val preference: List<Any>)

data class UserInfo(val pimg: String, val birthday: String)