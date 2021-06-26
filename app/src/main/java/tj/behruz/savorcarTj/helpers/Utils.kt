package tj.behruz.savorcarTj.helpers

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import tj.behruz.savorcarTj.models.CarInfo
import tj.behruz.savorcarTj.models.DriverTripPayload
import tj.behruz.savorcarTj.models.Passenger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class Utils {

    companion object {

        var handler = MutableLiveData<CarInfo>()
        var tripHandler = MutableLiveData<Passenger>()


        @SuppressLint("ResourceAsColor")
        fun showSnackBar(view: View, message: String) {
            val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_FADE)
            snackbar.setAction("OK") {

            }
            val snackBarView = snackbar.view
            snackBarView.setBackgroundColor(Color.RED)
            snackbar.show()
        }

        fun successSnackBar(view: View, message: String, handler: (Int) -> Unit) {
            val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            snackbar.animationMode = Snackbar.ANIMATION_MODE_FADE
            snackbar.setAction("OK") {
                handler.invoke(1)
            }
            val snackBarView = snackbar.view
            //   snackBarView.setBackgroundColor(Color.parseColor("#00c853"))
            snackbar.setActionTextColor(Color.YELLOW)
            val textView = snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            textView.setTextColor(Color.WHITE)
            snackbar.show()
        }

        fun getMonthByNumber(number: String): String {
            var result = ""
            when (number) {

                "01" -> {
                    result = "Января"
                }
                "02" -> {
                    result = "Февраля"
                }
                "03" -> {
                    result = "Марта"
                }
                "04" -> {
                    result = "Апреля"
                }
                "05" -> {
                    result = "Мая"
                }


                "06" -> {
                    result = "Июня"
                }
                "07" -> {
                    result = "Июля"
                }
                "08" -> {
                    result = "Августа"
                }
                "09" -> {
                    result = "Cентября"
                }
                "10" -> {
                    result = "Октября"
                }
                "11" -> {
                    result = "Ноября"
                }

                "12" -> {
                    result = "Декабря"
                }

            }
            return result

        }

        fun generateHash(phone: String): String {

            val MD5 = "MD5"
            try {
                // Create MD5 Hash
                val digest = MessageDigest.getInstance(MD5)
                digest.update(phone.toByteArray())
                val messageDigest = digest.digest()

                // Create Hex String
                val hexString = StringBuilder()
                for (aMessageDigest in messageDigest) {
                    var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                    while (h.length < 2) h = "0$h"
                    hexString.append(h)
                }
                return hexString.toString()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
            return ""
        }

        @SuppressLint("ServiceCast")
        fun hideKeyboardFrom(context: Context, view: View) {
            val imm: InputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }

        fun showAlert(message: String, context: Context) {
            val dialogBuilder = AlertDialog.Builder(context)

            // set message of alert dialog
            dialogBuilder.setMessage(message)
                // if the dialog is cancelable
                .setCancelable(true)
                // positive button text and action
                .setPositiveButton("Ок", DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                })

            // create dialog box
            val alert = dialogBuilder.create()
            // set title for alert dialog box
            alert.setTitle("Ошибка")
            // show alert dialog
            alert.show()


        }


        fun showErrorMessage(context: Context, message: String) {
            val dialog = MaterialAlertDialogBuilder(context).setTitle("Ошибка").setMessage(message)

                .setPositiveButton("OK") { dialog, which ->
                    dialog.dismiss()
                }.show()
        }

    }

}