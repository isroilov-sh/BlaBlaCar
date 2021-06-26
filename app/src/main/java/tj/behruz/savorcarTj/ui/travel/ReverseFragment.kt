package tj.behruz.savorcarTj.ui.travel

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import tj.behruz.savorcarTj.R
import tj.behruz.savorcarTj.databinding.AddReverseFragmentBinding
import tj.behruz.savorcarTj.helpers.PreferenceHelper
import tj.behruz.savorcarTj.helpers.PreferenceHelper.hashbyId
import tj.behruz.savorcarTj.helpers.PreferenceHelper.userId
import tj.behruz.savorcarTj.helpers.Utils
import tj.behruz.savorcarTj.helpers.getMonthName
import tj.behruz.savorcarTj.ui.bottomsheet.PassengerCountFragment
import java.util.*

class ReverseFragment: Fragment() {

    private val binding by lazy { AddReverseFragmentBinding.inflate(layoutInflater) }
    private val viewModel: TravelViewModel by viewModels()
    private lateinit var progressDialog: ProgressDialog
    private val preference by lazy { PreferenceHelper.defaultPreference(requireContext()) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root
    private var rDate = ""
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Подождите пожалуйста.")
        val from = requireArguments().getString("from") ?: ""
        val fromPlace = requireArguments().getString("fromPlace") ?: " "
        val to = requireArguments().getString("to") ?: ""
        val toPlace = requireArguments().getString("toPlace") ?: " "
        val price = requireArguments().getString("price") ?: ""
        val passenger = requireArguments().getString("passenger") ?: ""
        val date = requireArguments().getString("date") ?: ""
        rDate = requireArguments().getString("date") ?: ""
        val time = requireArguments().getString("time") ?: ""
        var rTo = ""
        var rFrom = ""
        var rTime = ""
        var rFromPlace = ""
        var rToPlace = ""
        var rPrice = ""
        var rNumber = ""
        val dates = date.split("-")

        binding.fromCity.setText(to)
        binding.toCity.setText(from)
        binding.passengerCount.text = passenger
        binding.price.setText(price)
        binding.day.text = dates[2].plus(" ").plus(String().getMonthName(dates[1]))

        when (passenger) {
            "1" -> {
                binding.passengerCount.text = passenger.plus(" пассажир")

            }
            "2", "3", "4" -> {
                binding.passengerCount.text = passenger.plus(" пассажира")
            }
            "5", "6", "7", "8" -> {
                binding.passengerCount.text = passenger.plus(" пассажиров")

            }
        }

        binding.confirmBtn.setOnClickListener {
            binding.trip.visibility = View.VISIBLE
            binding.confirmLayout.visibility = View.GONE
            binding.backEvent.visibility = View.GONE
            binding.closeReverse.visibility = View.VISIBLE

        }

        binding.passengerCount.setOnClickListener {
            val delete = PassengerCountFragment(binding.passengerCount.text.toString().substring(0, 1)) { countHandler(it) }
            delete.show(childFragmentManager, "cancel")
        }

        binding.closeReverse.setOnClickListener {
            binding.reverelayout.visibility = View.VISIBLE
            binding.confirmLayout.visibility = View.GONE
            binding.backEvent.visibility = View.VISIBLE
            binding.closeReverse.visibility = View.GONE
        }

        binding.backEvent.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.closeReverse.setOnClickListener {

        }

        binding.cancelBtn.setOnClickListener {
            progressDialog.show()

            viewModel.addTravel(userId = preference.userId, from = from, fromPlace = fromPlace, to = to, toPlace = toPlace, price = price, date = date, time = time, number = passenger.toInt(), hash = preference.hashbyId.toString()).observe(viewLifecycleOwner) {
                progressDialog.dismiss()
                if (it.code == 6) {
                    findNavController().navigate(R.id.action_global_addTripSuccessFragment)
                } else {
                    Utils.showErrorMessage(requireContext(), it.message)
                }

            }
        }

        binding.time.setOnClickListener {
            showTimePicker(binding.time)
        }

        binding.day.setOnClickListener {
            showCalendarDte(binding.day)
        }

        binding.button.setOnClickListener {

            if (validate()) {

                progressDialog.show()
                rFrom = binding.fromCity.text.toString()
                rTo = binding.toCity.text.toString()
                rPrice = binding.price.text.toString()
                rFromPlace = binding.fromStreet.text.toString()
                rToPlace = binding.toStreet.text.toString()
                rTime = binding.time.text.toString()
                viewModel.addTravel(preference.userId, from, fromPlace, to, toPlace, price, date, time, passenger.toInt(), preference.hashbyId.toString()).observe(viewLifecycleOwner) {
                    progressDialog.dismiss()
                    if (it.code == 6) {
                        viewModel.addTravel(preference.userId, rFrom, rFromPlace, rTo, rToPlace, rPrice, rDate, binding.time.text.toString(), binding.passengerCount.text.toString().substring(0, 1).toInt(), preference.hashbyId.toString()).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                            if (it.code == 6) {
                                findNavController().navigate(R.id.action_global_addTripSuccessFragment

                                )
                            } else {
                                Utils.showErrorMessage(requireContext(), it.message)
                            }


                        })
                    } else {
                        Utils.showErrorMessage(requireContext(), it.message)
                    }

                }
            }
        }


    }


    private fun countHandler(count: String) {
        when (count) {
            "1" -> {
                binding.passengerCount.text = count.plus(" пассажир")

            }
            "2", "3", "4" -> {
                binding.passengerCount.text = count.plus(" пассажира")
            }
            "5", "6", "7", "8" -> {
                binding.passengerCount.text = count.plus(" пассажиров")

            }
        }


    }

    private fun showCalendarDte(field: TextView) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->
            val month = monthOfYear + 1
            var formattedMonth = month.toString()
            if (month < 10) {
                formattedMonth = "0$month"
            }
            var formattedDayOfMonth = "" + dayOfMonth
            if (dayOfMonth < 10) {
                formattedDayOfMonth = "0$dayOfMonth"
            }
            field.text = formattedDayOfMonth.plus(" ").plus(String().getMonthName(formattedMonth))
            rDate = year.toString().plus("-").plus(formattedMonth).plus("-").plus(formattedDayOfMonth)

        }, year, month, day)
        dpd.datePicker.minDate = System.currentTimeMillis()
        dpd.show()
    }

    private fun showTimePicker(field: TextView) {
        val cldr = Calendar.getInstance()
        val hour = cldr[Calendar.HOUR_OF_DAY]
        val minutes = cldr[Calendar.MINUTE]
        val picker = TimePickerDialog(requireContext(), { _, sHour, sMinute -> field.text = "$sHour:$sMinute" }, hour, minutes, true)
        picker.show()
    }

    private fun validate(): Boolean {
        var result = false
        if (binding.time.text.isNullOrEmpty()) {
            result = false
            binding.timeError.visibility = View.VISIBLE
        } else {
            binding.timeError.visibility = View.GONE
            result = true
        }

        return result
    }
}