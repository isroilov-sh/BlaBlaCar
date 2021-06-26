package tj.behruz.savorcarTj.models

data class DriverTripPayload(
    val booking: List<Booking>,
    val cityfrom: String,
    val cityto: String,
    val comment: String,
    val cost: String,
    val datetime: String,
    val id: String,
    val number: String,
    val status: String,
    val userid: String,
    val placeto:String,
    val placefrom:String
)