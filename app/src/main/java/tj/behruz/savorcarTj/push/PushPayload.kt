package tj.behruz.savorcarTj.push

import android.os.Parcel
import android.os.Parcelable

data class PushPayload(
    val title:String,
    val body:String,
    val type:String,
    val id:String

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(body)
        parcel.writeString(type)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PushPayload> {
        override fun createFromParcel(parcel: Parcel): PushPayload {
            return PushPayload(parcel)
        }

        override fun newArray(size: Int): Array<PushPayload?> {
            return arrayOfNulls(size)
        }
    }
}