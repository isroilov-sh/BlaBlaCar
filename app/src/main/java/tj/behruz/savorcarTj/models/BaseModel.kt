package tj.behruz.savorcarTj.models

data class BaseModel<T>(val code:Int,val message:String,val data:List<T>?)