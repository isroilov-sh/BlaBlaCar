package tj.behruz.savorcarTj.models

data class Passenger(
    val cityfrom: String,
    val cityto: String,
    val comment: String,
    val cost: String,
    val date: String,
    val datetime: String,
    val id: String,
    val name: String,
    val number: String,
    val phone: String,
    val status: String,
    val tnumber: String,
    val tripid: String,
    val tstatus: String,
    val uid: String,
    val placeto:String?,
    val placefrom:String?,
    val booking: List<Booking>?
)
