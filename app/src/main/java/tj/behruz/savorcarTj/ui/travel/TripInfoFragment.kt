package tj.behruz.savorcarTj.ui.travel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import tj.behruz.savorcarTj.R
import tj.behruz.savorcarTj.adapters.BookingAdapter
import tj.behruz.savorcarTj.adapters.PassengerAdapter
import tj.behruz.savorcarTj.databinding.TripFragmentBinding
import tj.behruz.savorcarTj.helpers.Utils
import tj.behruz.savorcarTj.helpers.enum.Action
import tj.behruz.savorcarTj.models.Booking
import tj.behruz.savorcarTj.models.BookingAction
import tj.behruz.savorcarTj.models.EditModel
import tj.behruz.savorcarTj.models.enum.TripDetailsAction
import tj.behruz.savorcarTj.ui.bottomsheet.CancelBookingPassenger
import tj.behruz.savorcarTj.ui.bottomsheet.ConfirmBottomSheet

class TripInfoFragment : Fragment() {
    val bundle = Bundle()
    private lateinit var binding: TripFragmentBinding
    private lateinit var bookedAdapter: BookingAdapter
    private var bookings = mutableListOf<Booking>()
    private var id = ""
    private lateinit var bookingAction: BookingAction
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TripFragmentBinding.inflate(layoutInflater, container, false)
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
                val dates = it.datetime.split(" ")
                val days = dates[0].split("-")
                binding.historyDriverDate.text =
                    days[2].plus(" ${Utils.getMonthByNumber(days[1])}").plus(" в ")
                        .plus(dates[1].removeRange(dates[1].length - 3, dates[1].length))
                id = it.id


                if (it.booking != null) {
                    bookings.addAll(it.booking)
                    bookedAdapter = BookingAdapter(requireContext(), TripDetailsAction.PASSENGER.name,it.booking) { handler(it) }
                    binding.passengerList.adapter = bookedAdapter
                    binding.passengerErrors.visibility = View.GONE
                    val passengerCountAdapter =
                        PassengerAdapter(requireContext(), it.number.toInt(), it.booking.size)
                    binding.passengerCountList.apply {
                        adapter = passengerCountAdapter
                    }
                } else {
                    binding.passengerErrors.visibility = View.VISIBLE
                }
            })


            binding.cancelTrip.setOnClickListener {
                bundle.putString("id", id)
                findNavController().navigate(
                    R.id.action_tripInfoFragment_to_cancelTripFragment,
                    bundle
                )
            }

            binding.userName.setOnClickListener {
                bundle.putString("username", binding.userName.text.toString())
                findNavController().navigate(R.id.action_global_userViewFragment, bundle)
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.backEvent.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun handler(item: BookingAction) {
        bookingAction = item
        when (item.action) {
            Action.CONFIRM.name -> {
                val confirm = ConfirmBottomSheet(
                    bid = item.item.bid.toInt(),
                    number = item.item.number.toInt(),
                    handler = { confrimHandler(it) })
                confirm.show(childFragmentManager, "confirm")

            }
            Action.DELETE.name -> {
                val delete = CancelBookingPassenger(
                    tripId = id.toInt(),
                    bid = item.item.bid.toInt(),
                    number = item.item.number.toInt(),
                    handler = { handlerAction(it) })
                delete.show(childFragmentManager, "cancel")
            }
        }

    }


    private fun handlerAction(action: EditModel) {
//        if (action.code == 6) {
//            for (i in bookings) {
//                if (i == bookingAction.item) {
//                    bookings.remove(i)
//                }
//            }
//            if (bookings.isNullOrEmpty()) {
//                binding.passengerErrors.visibility = View.VISIBLE
//            }
//
//            bookedAdapter.dataChanged(bookings)
//        }


    }

    private fun confrimHandler(action: EditModel) {

    }


}