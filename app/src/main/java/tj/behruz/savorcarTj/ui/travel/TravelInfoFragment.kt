package tj.behruz.savorcarTj.ui.travel

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import tj.behruz.savorcarTj.R
import tj.behruz.savorcarTj.databinding.TravelInfoFragmentBinding
import tj.behruz.savorcarTj.helpers.PreferenceHelper
import tj.behruz.savorcarTj.helpers.PreferenceHelper.userId
import tj.behruz.savorcarTj.helpers.Utils
import tj.behruz.savorcarTj.ui.bottomsheet.PassengerCountFragment
import tj.behruz.savorcarTj.adapters.PassengerAdapter

var number = ""

class TravelInfoFragment: Fragment() {
    private lateinit var progressDialog: ProgressDialog

    lateinit var binding: TravelInfoFragmentBinding
    private val viewModel: TravelViewModel by viewModels()
    private val prefrenceHelper by lazy { PreferenceHelper.defaultPreference(requireContext()) }
    private var tripId = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = TravelInfoFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Подождите пожалуйста.")

        try {
            val userId = prefrenceHelper.userId.toString()
            val id = arguments?.getString("id")
            val hash = Utils.generateHash(userId.plus("blabla").plus(userId))

            viewModel.getTripInfo(userId.toInt(), id?.toInt()!!, hash).observe(viewLifecycleOwner, Observer { trip ->
                    binding.progressBar.visibility = View.GONE
                    binding.content.visibility = View.VISIBLE
                    if (trip.code == 6 && trip.data != null) {
                        val tripPayload = trip.data
                        tripId = tripPayload.id.toInt()
                        val dates = tripPayload.datetime.split(" ")
                        val days = dates[0].split("-")
                        binding.date.text = days[2].plus(" ${Utils.getMonthByNumber(days[1])}").plus(" в ").plus(dates[1].removeRange(dates[1].length - 3, dates[1].length))
                        binding.driverName.text = tripPayload.name
                        binding.pirceTripInfo.text = tripPayload.cost.plus("  c")
                        binding.fromCityTripInfo.text = tripPayload.cityfrom.plus(", ").plus(tripPayload.placefrom)
                        binding.toCity.text = tripPayload.cityto.plus(", ").plus(tripPayload.placeto)
                        binding.rating.text = tripPayload.rate.toString().plus("/5")
                        val adapter = PassengerAdapter(requireContext(), tripPayload.number.toInt(), tripPayload.booked)
                        binding.passengerItem.adapter = adapter
                        Glide.with(this).load(tripPayload.pimg).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(binding.imageView14)

                    }

                })



            binding.passengerCount.setOnClickListener {
                val delete = PassengerCountFragment(binding.passengerCount.text.toString().substring(0, 1)) { countHandler(it) }
                delete.show(childFragmentManager, "cancel")
            }

            viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
                Utils.showErrorMessage(requireContext(), it)
            })

            binding.profileLayout.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("username", binding.driverName.text.toString())
                findNavController().navigate(R.id.action_global_userViewFragment, bundle)
            }
            when (number) {
                "1" -> {
                    binding.passengerCount.text = number.plus(" пассажир")

                }
                "2", "3", "4" -> {
                    binding.passengerCount.text = number.plus(" пассажира")
                }
                "5", "6", "7", "8" -> {
                    binding.passengerCount.text = number.plus(" пассажиров")

                }
            }

            binding.confirm.setOnClickListener {
                progressDialog.show()
                viewModel.addBooking(userId = userId.toInt(), tripId = tripId, count = binding.passengerCount.text.toString().substring(0, 1).toInt(), hash = hash).observe(viewLifecycleOwner, Observer {
                    progressDialog.dismiss()
                    when (it.code) {
                        6 -> {
                            findNavController().navigate(R.id.action_travelInfoFragment_to_bookingSuccessFragment)
                        }
                        10 -> {
                            Utils.successSnackBar(binding.view, it.message) { handler(it) }
                        }
                        else -> {
                            Utils.showErrorMessage(requireContext(), it.message)
                        }
                    }


                })

            }

            binding.backEvent.setOnClickListener {

                requireActivity().onBackPressed()

            }
        } catch (e: Exception) {

        }


    }

    private fun handler(item: Int) {

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