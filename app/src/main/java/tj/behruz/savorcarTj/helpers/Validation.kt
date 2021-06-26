package tj.behruz.savorcarTj.helpers

data class RegisValid(var name: Boolean = true, var city: Boolean = true, var birthdate: Boolean = true)
data class CreateTravelValid(var from: Boolean = true, var to: Boolean = true,var time:Boolean=true,var price:Boolean=true, var r_time:Boolean=true)
data class SearchValid(var from: Boolean=true,var to: Boolean=true)