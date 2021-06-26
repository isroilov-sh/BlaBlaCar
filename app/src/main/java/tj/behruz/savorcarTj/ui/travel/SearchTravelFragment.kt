package tj.behruz.savorcarTj.ui.travel

import android.annotation.SuppressLint
import android.app.DatePickerDialog
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
import tj.behruz.savorcarTj.databinding.SearchTripFragmentBinding
import tj.behruz.savorcarTj.helpers.SearchValid
import tj.behruz.savorcarTj.helpers.Utils
import tj.behruz.savorcarTj.helpers.getMonthName
import tj.behruz.savorcarTj.ui.bottomsheet.PassengerCountFragment
import java.text.SimpleDateFormat
import java.util.*

class SearchTravelFragment: Fragment() {

    private val binding by lazy { SearchTripFragmentBinding.inflate(layoutInflater) }
    private val viewModel: TravelViewModel by viewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root
    private var date = ""
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
        setTextWatchers()

    }


    @SuppressLint("SimpleDateFormat")
    private fun init() {
        val sdf = SimpleDateFormat("yyyy-M-dd")
        val currentDate = sdf.format(Date())
        date = currentDate
        val hash = Utils.generateHash("blablaalbalb")

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

        binding.day.setOnClickListener {
            showCalendar(binding.day)

        }

        binding.passengerCount.setOnClickListener {
            val delete = PassengerCountFragment(binding.passengerCount.text.toString().substring(0, 1)) { countHandler(it) }
            delete.show(childFragmentManager, "cancel")
        }

        binding.searchBtn.setOnClickListener {

            val searchValid = SearchValid()
            if (searchValid == validate()) {
                val bundle = Bundle()
                bundle.putString("from", binding.fromCity.text.toString())
                bundle.putString("to", binding.toCity.text.toString())
                bundle.putString("date", date)
                bundle.putString("count", binding.passengerCount.text.substring(0, 1))
                findNavController().navigate(R.id.action_searchTravelFragment_to_searchTripFragment, bundle)
            }

        }

        binding.reverse.setOnClickListener {
            val city = binding.fromCity.text.toString()
            binding.fromCity.setText(binding.toCity.text.toString())
            binding.toCity.setText(city)
        }

    }


    private fun showCalendar(field: TextView) {
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

            // Display Selected date in Toast
            field.setText(formattedDayOfMonth.plus(" ").plus(String().getMonthName(formattedMonth)))
            date = year.toString().plus("-").plus(formattedMonth).plus("-").plus(formattedDayOfMonth)


        }, year, month, day)
        dpd.datePicker.minDate=System.currentTimeMillis()
        dpd.show()
    }


    private fun setTextWatchers() {

        binding.fromCity.addTextChangedListener { from ->
            if (from.toString().isNotEmpty()) {
                binding.clearFrom.visibility = View.VISIBLE
            } else {
                binding.clearFrom.visibility = View.GONE

            }

        }

        binding.toCity.addTextChangedListener { from ->

            if (from.toString().isNotEmpty()) {
                binding.clearToCity.visibility = View.VISIBLE
            } else {
                binding.clearToCity.visibility = View.GONE

            }

        }


        binding.clearFrom.setOnClickListener {
            binding.fromCity.setText("")
        }
        binding.clearToCity.setOnClickListener {
            binding.toCity.setText("")
        }

    }


    private fun validate(): SearchValid {
        val searchValid = SearchValid()
        if (binding.fromCity.text.toString().isEmpty()) {
            searchValid.from = false
            binding.fromErrorMessage.visibility = View.VISIBLE
        } else {
            binding.fromErrorMessage.visibility = View.GONE
            searchValid.from = true
        }

        if (binding.toCity.text.toString().isEmpty()) {
            searchValid.to = false
            binding.toErrorMessage.visibility = View.VISIBLE
        } else {
            searchValid.to = true
            binding.toErrorMessage.visibility = View.GONE

        }
        return searchValid
    }

    private fun countHandler(count: String) {
        when (count) {
            "1" -> {
                binding.passengerCount.text = count.plus(" пассажир")

            }
            "2", "3", "4" -> {
                binding.passengerCount.text = count.plus(" пассажира")
            }
            "5",  "6", "7", "8" -> {
                binding.passengerCount.text = count.plus(" пассажиров")

            }
        }

    }

}