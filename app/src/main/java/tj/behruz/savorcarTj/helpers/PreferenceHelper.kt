package tj.behruz.savorcarTj.helpers

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object PreferenceHelper {

    const val USER_ID = "USER_ID"
    const val PHONE = "PHONE"
    const val HASH = "hash"
    const val USERNAME = "username"
    const val LEVEL = "level"
    const val CARMARK = "carmark"
    const val CARCLASS = "carclass"
    const val CARBODY = "carbody"
    const val GOSNOMER = "gosnomer"
    const val CARCOLOR = "carcolor"
    const val HAVECAR = "haveCar"
    const val CARMARKID = "carmarkId"
    const val CARCLASSID = "carclassId"
    const val CARBODYID = "carbodyId"
    const val HASHBYID = "hashbyid"
    const val CARIMG = "carimg"
    const val NOTIFICATION="notification"
    fun defaultPreference(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun customPreference(context: Context, name: String): SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    var SharedPreferences.userId
        get() = getInt(USER_ID, 0)
        set(value) {
            editMe {
                it.putInt(USER_ID, value)
            }
        }

    var SharedPreferences.phone
        get() = getString(PHONE, "")
        set(value) {
            editMe {
                it.putString(PHONE, value)
            }
        }

    var SharedPreferences.hash
        get() = getString(HASH, "")
        set(value) {
            editMe {
                it.putString(HASH, value)
            }
        }

    var SharedPreferences.username
        get() = getString(USERNAME, "")
        set(value) {
            editMe {
                it.putString(USERNAME, value)
            }
        }

    var SharedPreferences.level
        get() = getString(LEVEL, "")
        set(value) {
            editMe {
                it.putString(LEVEL, value)
            }
        }

    var SharedPreferences.carmark
        get() = getString(CARMARK, "")
        set(value) {
            editMe {
                it.putString(CARMARK, value)
            }
        }

    var SharedPreferences.carclass
        get() = getString(CARCLASS, "")
        set(value) {
            editMe {
                it.putString(CARCLASS, value)
            }
        }

    var SharedPreferences.carbody
        get() = getString(CARBODY, "")
        set(value) {
            editMe {
                it.putString(CARBODY, value)
            }
        }

    var SharedPreferences.gosnomer
        get() = getString(GOSNOMER, "")
        set(value) {
            editMe {
                it.putString(GOSNOMER, value)
            }
        }
    var SharedPreferences.carcolor
        get() = getString(CARCOLOR, "")
        set(value) {
            editMe {
                it.putString(CARCOLOR, value)
            }
        }


    var SharedPreferences.carmarkId
        get() = getString(CARMARKID, "")
        set(value) {
            editMe {
                it.putString(CARMARKID, value)
            }
        }

    var SharedPreferences.carbodyId
        get() = getString(CARBODYID, "")
        set(value) {
            editMe {
                it.putString(CARBODYID, value)
            }
        }

    var SharedPreferences.carclassId
        get() = getString(CARCLASSID, "")
        set(value) {
            editMe {
                it.putString(CARCLASSID, value)
            }
        }

    var SharedPreferences.haveCar
        get() = getBoolean(HAVECAR, false)
        set(value) {
            editMe {
                it.putBoolean(HAVECAR, value)
            }
        }

    var SharedPreferences.hashbyId
        get() = getString(HASHBYID, "")
        set(value) {
            editMe {
                it.putString(HASHBYID, value)
            }
        }
    var SharedPreferences.carimg
        get() = getString(CARIMG, "")
        set(value) {
            editMe {
                it.putString(CARIMG, value)
            }
        }

    var SharedPreferences.notification
    get() = getString(NOTIFICATION,"")
    set(value) {
        editMe {
            it.putString(NOTIFICATION,value)
        }
    }

    var SharedPreferences.clearValues
        get() = { }
        set(value) {
            editMe {
                it.clear()
            }
        }

}