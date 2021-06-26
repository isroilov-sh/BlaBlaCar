package tj.behruz.savorcarTj.models


data class TripPayload(
    val car_color: String,
    val car_mark: String,
    val cityfrom: String,
    val cityto: String,
    val comment: String,
    val cost: String,
    val datetime: String,
    val gos_nomer: String,
    val id: String,
    val name: String,
    val number: String,
    val status: String,
    val userid: String,
    val booked:Int,
    val rate:Int,
    val pimg:String,
    val placefrom:String,
    val placeto:String
)

data class SearchTrip(
    val code: Int,
    val data: List<TripPayload>,
    val message: String
)