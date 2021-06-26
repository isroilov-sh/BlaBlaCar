package tj.behruz.savorcarTj.ui.trips

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
import androidx.recyclerview.widget.LinearLayoutManager
import tj.behruz.savorcarTj.R
import tj.behruz.savorcarTj.adapters.PassengerHistoryAdapter
import tj.behruz.savorcarTj.databinding.HistoryTemplateBinding
import tj.behruz.savorcarTj.helpers.PreferenceHelper
import tj.behruz.savorcarTj.helpers.PreferenceHelper.userId
import tj.behruz.savorcarTj.helpers.Utils
import tj.behruz.savorcarTj.models.Passenger
import tj.behruz.savorcarTj.models.enum.TripDetailsAction
import tj.behruz.savorcarTj.ui.home.HomeViewModel

class PassengerHistoryFragment(): Fragment() {

    private lateinit var binding: HistoryTemplateBinding
    private val shared by lazy { PreferenceHelper.defaultPreference(requireContext()) }
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = HistoryTemplateBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initBinding()
    }


    private fun initBinding() {
        val hashById = Utils.generateHash(shared.userId.toString().plus("blabla").plus(shared.userId))
        binding.shimmerViewContainer.startShimmer()
        viewModel.getAllTrips(shared.userId, hashById).observe(viewLifecycleOwner, Observer { history ->
            if (history.code == 6) {
                binding.shimmerViewContainer.apply {
                    stopShimmer()
                    visibility = View.GONE
                }
                if (history.data.passenger.isNotEmpty()) {
                    binding.background.visibility = View.GONE
                    binding.homeTitle.visibility = View.GONE
                    val passengerHistoryAdapter = PassengerHistoryAdapter(requireContext(), history.data.passenger) { passengerHandler(it) }
                    binding.history.layoutManager = LinearLayoutManager(requireContext())
                    binding.history.adapter = passengerHistoryAdapter
                } else {
                  //  binding.background.visibility = View.VISIBLE
                    binding.homeTitle.visibility = View.VISIBLE

                }
            } else {
                Utils.showErrorMessage(requireContext(), history.message)
            }
        })

    }


    private fun passengerHandler(passenger: Passenger) {
        val bundle = bundleOf()
        bundle.putString("tripId", passenger.tripid)
        bundle.putString("type", TripDetailsAction.PASSENGER.name)
        findNavController().navigate(R.id.action_global_tripDetailsFragment, bundle)
    }
}


