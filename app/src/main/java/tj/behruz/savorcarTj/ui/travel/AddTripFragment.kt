package tj.behruz.savorcarTj.ui.travel

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import tj.behruz.savorcarTj.R
import tj.behruz.savorcarTj.databinding.AddTripFragmentBinding
import tj.behruz.savorcarTj.helpers.CreateTravelValid
import tj.behruz.savorcarTj.helpers.PreferenceHelper
import tj.behruz.savorcarTj.helpers.PreferenceHelper.haveCar
import tj.behruz.savorcarTj.helpers.Utils
import tj.behruz.savorcarTj.helpers.getMonthName
import tj.behruz.savorcarTj.ui.bottomsheet.PassengerCountFragment
import java.text.SimpleDateFormat
import java.util.*

class AddTripFragment: Fragment() {


    private var date = ""
    private var reverseDate = ""
    private val binding by lazy { AddTripFragmentBinding.inflate(layoutInflater) }
    private val viewModel: TravelViewModel by viewModels()
    private lateinit var progressDialog: ProgressDialog
    private val default by lazy { PreferenceHelper.defaultPreference(requireContext()) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Подождите пожалуйста.")

        if (!default.haveCar) {
            findNavController().navigate(R.id.action_addTripFragment_to_carInfoFragment)
        }

        initData()
        setTextWatchers()

    }


    @SuppressLint("SimpleDateFormat")
    private fun initData() {
        val hash = Utils.generateHash("blablaalbalb")
        val sdf = SimpleDateFormat("yyyy-M-dd")
        val currentDate = sdf.format(Date())
        binding.day.tag = currentDate
        date = currentDate
        reverseDate = currentDate

        viewModel.getCitiesAsync(hash).observe(viewLifecycleOwner) { response ->
            if (response.code == 6) {
                val cities = arrayListOf<String>()
                for (i in response.data!!) {
                    cities.add(i.name)
                }
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, cities)
                binding.fromCity.setAdapter(adapter)
                binding.toCity.setAdapter(adapter)

            }

        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Utils.showErrorMessage(requireContext(), it)
        }


    }

    private fun setTextWatchers() {
        binding.fromCity.addTextChangedListener { from ->
            if (from!!.isNotEmpty()) {
                binding.clearFrom.visibility = View.VISIBLE
            } else {
                binding.clearFrom.visibility = View.GONE
            }

        }
        binding.toCity.addTextChangedListener { toCity ->
            if (toCity!!.isNotEmpty()) {
                binding.clearTo.visibility = View.VISIBLE
            } else {
                binding.clearTo.visibility = View.GONE
            }
        }
        binding.price.addTextChangedListener { price ->
            if (price!!.isNotEmpty()) {
                binding.clearPrice.visibility = View.VISIBLE
            } else {
                binding.clearPrice.visibility = View.GONE
            }
        }
        binding.price.addTextChangedListener { price ->
            if (price!!.isNotEmpty()) {
                binding.clearPrice.visibility = View.VISIBLE
            } else {
                binding.clearPrice.visibility = View.GONE
            }
        }
        binding.toStreet.addTextChangedListener { price ->
            if (price!!.isNotEmpty()) {
                binding.clearToStreet.visibility = View.VISIBLE
            } else {
                binding.clearToStreet.visibility = View.GONE
            }
        }
        binding.fromStreet.addTextChangedListener { price ->
            if (price!!.isNotEmpty()) {
                binding.clearFromStreet.visibility = View.VISIBLE
            } else {
                binding.clearFromStreet.visibility = View.GONE
            }
        }
        binding.day.setOnClickListener {
            showCalendarDte(binding.day)
        }

        binding.time.setOnClickListener {
            showTimePicker(binding.time)
        }

        binding.clearPrice.setOnClickListener {
            binding.price.setText("")
        }
        binding.clearFrom.setOnClickListener {
            binding.fromCity.setText("")
        }
        binding.clearTo.setOnClickListener {
            binding.toCity.setText("")
        }

        binding.clearFromStreet.setOnClickListener {
            binding.fromStreet.setText("")
        }

        binding.clearToStreet.setOnClickListener {
            binding.toStreet.setText("")
        }


        binding.button.setOnClickListener {
            addTravel()
        }

        binding.passengerCount.setOnClickListener {
            val delete = PassengerCountFragment(binding.passengerCount.text.toString().substring(0, 1)) { countHandler(it) }
            delete.show(childFragmentManager, "cancel")
        }


    }

    private fun addTravel() {
        val createTravelValid = CreateTravelValid()
        if (createTravelValid == validate()) {

            val bundle = Bundle()
            bundle.putString("from", binding.fromCity.text.toString())
            bundle.putString("fromPlace", binding.fromStreet.text.toString())
            bundle.putString("to", binding.toCity.text.toString())
            bundle.putString("toPlace", binding.toStreet.text.toString())
            bundle.putString("price", binding.price.text.toString())
            bundle.putString("passenger", binding.passengerCount.text.toString().substring(0, 1))
            bundle.putString("date", date)
            bundle.putString("time", binding.time.text.toString())
            findNavController().navigate(R.id.action_global_reverseFragment, bundle)

        }


    }

    private fun validate(): CreateTravelValid {
        val valid = CreateTravelValid()

        if (binding.fromCity.text.toString().isEmpty()) {
            binding.fromErrorMessage.visibility = View.VISIBLE
            valid.from = false
        } else {
            valid.from = true
            binding.fromErrorMessage.visibility = View.GONE
        }

        if (binding.toCity.text.toString().isEmpty()) {
            binding.toErrorMessage.visibility = View.VISIBLE
            valid.to = false
        } else {
            binding.toErrorMessage.visibility = View.GONE
            valid.to = true
        }

        if (binding.price.text.toString().isEmpty()) {
            binding.priceError.visibility = View.VISIBLE
            valid.price = false
        } else {
            binding.priceError.visibility = View.GONE
            valid.price = true
        }

        if (binding.time.text.toString().isEmpty()) {
            binding.timeError.visibility = View.VISIBLE
            valid.r_time = false
        } else {
            binding.timeError.visibility = View.GONE
            valid.r_time = true
        }
        return valid
    }


    private fun showCalendarDte(field: TextView) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(), { view, year, monthOfYear, dayOfMonth ->
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
            date = year.toString().plus("-").plus(formattedMonth).plus("-").plus(formattedDayOfMonth)

        }, year, month, day)
        dpd.datePicker.minDate = System.currentTimeMillis()
        dpd.show()
    }

    private fun showTimePicker(field: TextView) {
        val cldr = Calendar.getInstance()
        val hour = cldr[Calendar.HOUR_OF_DAY]
        val minutes = cldr[Calendar.MINUTE]
        val picker = TimePickerDialog(requireContext(), { tp, sHour, sMinute -> field.text = "$sHour:$sMinute" }, hour, minutes, true)
        picker.show()
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


}
