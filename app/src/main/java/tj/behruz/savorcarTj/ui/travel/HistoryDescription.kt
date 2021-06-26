package tj.behruz.savorcarTj.ui.travel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import tj.behruz.savorcarTj.R
import tj.behruz.savorcarTj.adapters.HistoryPassengerAdapter
import tj.behruz.savorcarTj.databinding.HistoryDescriptionFragmentBinding
import tj.behruz.savorcarTj.helpers.PreferenceHelper
import tj.behruz.savorcarTj.helpers.Utils
import tj.behruz.savorcarTj.models.Booking
import tj.behruz.savorcarTj.models.BookingAction
import tj.behruz.savorcarTj.adapters.PassengerAdapter


class HistoryDescription: Fragment() {

    private lateinit var binding: HistoryDescriptionFragmentBinding
    private val prefrence by lazy { PreferenceHelper.defaultPreference(requireContext()) }
    private val tripViewModel: TravelViewModel by viewModels()
    private lateinit var bookedAdapter: HistoryPassengerAdapter
    private var bookings = mutableListOf<Booking>()
    private var id = ""
    private lateinit var bookingAction: BookingAction
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = HistoryDescriptionFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.progressBar.visibility = View.GONE

        try {


            Utils.tripHandler.observe(viewLifecycleOwner, Observer {
                binding.pricePerson.text = it.cost.plus(" ").plus("c")
                binding.fromCityDriver.text = it.cityfrom
                binding.toCityDriver.text = it.cityto

                if (!it.placefrom.isNullOrEmpty()) {
                    binding.fromCityDriver.text = it.cityfrom.plus(", ").plus(it.placefrom)
                }

                if (!it.placeto.isNullOrEmpty()) {
                    binding.toCityDriver.text = it.cityto.plus(", ").plus(it.placeto)

                }

                val dates = it.datetime.split(" ")
                val days = dates[0].split("-")
                binding.historyDriverDate.text = days[2].plus(" ${Utils.getMonthByNumber(days[1])}").plus(" в ").plus(dates[1].removeRange(dates[1].length - 3, dates[1].length))
                id = it.id
                if (it.booking!!.isNotEmpty()) {
                    bookings.addAll(it.booking)
                    bookedAdapter = HistoryPassengerAdapter(requireContext(), it.booking) { handler(it) }
                    binding.passengerList.adapter = bookedAdapter
                    binding.passengerErrors.visibility = View.GONE

                } else {
                    binding.passengerErrors.visibility = View.VISIBLE
                }

                val adapter = PassengerAdapter(requireContext(), it.number.toInt(), it.booking.size)
                binding.recyclerView2.adapter = adapter

                when (it.status) {
                    "2" -> {
                        binding.status.text = "Отменён"
                    }
                    "3" -> {
                        binding.status.text = "Поездка не состоялась"
                    }
                }
            })


        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.backEvent.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun handler(item: Booking) {
        val bundle = Bundle()
        bundle.putString("username", item.name)
        findNavController().navigate(R.id.action_global_userViewFragment, bundle)
    }


}