package tj.behruz.savorcarTj.ui.travel

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import tj.behruz.savorcarTj.R
import tj.behruz.savorcarTj.adapters.BookingAdapter
import tj.behruz.savorcarTj.adapters.PassengerAdapter
import tj.behruz.savorcarTj.databinding.TripDetailsFragmentBinding
import tj.behruz.savorcarTj.helpers.PreferenceHelper
import tj.behruz.savorcarTj.helpers.PreferenceHelper.hashbyId
import tj.behruz.savorcarTj.helpers.PreferenceHelper.userId
import tj.behruz.savorcarTj.helpers.PreferenceHelper.username
import tj.behruz.savorcarTj.helpers.Utils
import tj.behruz.savorcarTj.helpers.enum.Action
import tj.behruz.savorcarTj.models.Booking
import tj.behruz.savorcarTj.models.BookingAction
import tj.behruz.savorcarTj.models.EditModel
import tj.behruz.savorcarTj.models.enum.TripDetailsAction
import tj.behruz.savorcarTj.ui.bottomsheet.CancelBookingPassenger
import tj.behruz.savorcarTj.ui.bottomsheet.ConfirmBottomSheet

class TripDetailsFragment: Fragment() {
    private var id = ""
    private val binding by lazy { TripDetailsFragmentBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<TravelViewModel>()
    private val shared by lazy { PreferenceHelper.defaultPreference(requireContext()) }
    private lateinit var bookedAdapter: BookingAdapter
    private var bookings = mutableListOf<Booking>()
    private val tripId by lazy { arguments?.getString("tripId") }
    private val type by lazy { arguments?.getString("type") }
    private val bundle = bundleOf()
    private lateinit var bookingAction: BookingAction
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = binding.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getInfo()


        binding.backEvent.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.cancelTrip.setOnClickListener {
            val currentBooking = bookings.find { it.name == shared.username }
            when (type) {
                TripDetailsAction.PASSENGER.name -> {
                    val bundle = Bundle()
                    bundle.putString("uid", currentBooking!!.bid)
                    findNavController().navigate(R.id.action_global_reasonFragment, bundle)

                }
                TripDetailsAction.DRIVER.name -> {
                    binding.cancelTrip.visibility = View.GONE
                    bundle.putString("id", tripId)
                    findNavController().navigate(R.id.action_tripDetailsFragment_to_cancelTripFragment, bundle)
                }

            }

        }

        binding.userName.setOnClickListener {
            bundle.putString("username", binding.userName.text.toString())
            findNavController().navigate(R.id.action_global_userViewFragment, bundle)
        }
    }

    private fun handler(item: BookingAction) {
        bookingAction = item
        when (item.action) {
            Action.CONFIRM.name -> {
                if (item.item.status == "0") {
                    val confirm = ConfirmBottomSheet(bid = item.item.bid.toInt(), number = item.item.number.toInt(), handler = { confrimHandler(it) })
                    confirm.show(childFragmentManager, "confirm")

                } else {
                    Utils.showSnackBar(binding.view4, "Вы уже подтвердили запрос")
                }

            }
            Action.DELETE.name -> {
                val delete = CancelBookingPassenger(tripId = id.toInt(), bid = item.item.bid.toInt(), number = item.item.number.toInt(), handler = { handlerAction(it) })
                delete.show(childFragmentManager, "cancel")
            }

            Action.INFO.name -> {
                val bundle = bundleOf()
                bundle.putString("username", item.item.name)
                findNavController().navigate(R.id.action_global_userViewFragment, bundle)

            }

        }

    }

    private fun handlerAction(action: EditModel) {
        getInfo()
    }

    private fun confrimHandler(action: EditModel) {
        getInfo()
    }

    private fun getInfo() {
        bookings.clear()

        when (type) {
            TripDetailsAction.PASSENGER.name -> {
                binding.cancelTrip.setText("Отменить брон")
            }
            TripDetailsAction.PASSENGERHISTORY.name  -> {
                binding.cancelTrip.visibility = View.GONE
            }
            TripDetailsAction.DRIVERHISTORY.name->{
                binding.cancelTrip.visibility = View.GONE
            }


        }

        viewModel.getTripInfo(shared.userId, tripId.toString().toInt(), shared.hashbyId.toString()).observe(viewLifecycleOwner, Observer { tripinfo ->
            binding.progressBar.visibility = View.GONE
            if (tripinfo.code == 6 && tripinfo.data != null) {
                val trip = tripinfo.data
                id = trip.id
Log.d("DEVELOPER","{${tripinfo.toString()}}")
                binding.content.visibility = View.VISIBLE
                binding.toCityDriver.text = trip.cityto.plus(", ${trip.placeto}")
                binding.fromCityDriver.text = trip.cityfrom.plus(", ${trip.placefrom}")
                binding.pricePerson.text = trip.cost.plus(" c")
                binding.historyDriverDate.text = trip.datetime.removeRange(trip.datetime.length - 3, trip.datetime.length)
                binding.userName.text = trip.name
                val passengerCountAdapter = PassengerAdapter(requireContext(), trip.number.toInt(), trip.booking!!.size)
                binding.passengerCountList.apply {
                    adapter = passengerCountAdapter
                }
                if (trip.booking.isNotEmpty()) {
                    bookings.addAll(trip.booking)
                    bookedAdapter = BookingAdapter(requireContext(),type.toString(), trip.booking) { handler(it) }
                    binding.passengerList.adapter = bookedAdapter
                    binding.passengerErrors.visibility = View.GONE
                    Log.d("DEVELOPERs", shared.username.toString())
                    Log.d("DEVELOPER",trip.name)
                    if (shared.username != trip.name) {
                        binding.userName.visibility = View.VISIBLE
                        binding.userView.visibility = View.VISIBLE
                    }

                } else {
                    binding.passengerList.visibility = View.GONE
                    binding.passengerErrors.visibility = View.VISIBLE
                }


            } else {
                Utils.showAlert(tripinfo.message, requireContext())
            }

        })

    }


}