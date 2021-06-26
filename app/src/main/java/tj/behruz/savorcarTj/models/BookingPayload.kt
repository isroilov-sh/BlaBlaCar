package tj.behruz.savorcarTj.models

data class BookingPayload(
    val bid: String,
    val bookerid: String,
    val date: String,
    val name: String,
    val number: String,
    val phone: String,
    val pimg: String,
    val status: String,
    val uid: String
)