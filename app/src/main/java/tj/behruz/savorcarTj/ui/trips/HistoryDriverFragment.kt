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
import tj.behruz.savorcarTj.databinding.HistoryFragmentTemplateBinding
import tj.behruz.savorcarTj.helpers.PreferenceHelper
import tj.behruz.savorcarTj.helpers.PreferenceHelper.userId
import tj.behruz.savorcarTj.helpers.Utils
import tj.behruz.savorcarTj.models.Passenger
import tj.behruz.savorcarTj.models.enum.TripDetailsAction
import tj.behruz.savorcarTj.ui.home.HomeViewModel

class HistoryDriverFragment: Fragment() {

    private lateinit var binding: HistoryFragmentTemplateBinding
    private val shared by lazy { PreferenceHelper.defaultPreference(requireContext()) }
    private val viewModel: HomeViewModel by viewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = HistoryFragmentTemplateBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initBinding()
    }

    private fun initBinding() {
        val hashById = Utils.generateHash(shared.userId.toString().plus("blabla").plus(shared.userId))
        binding.shimmerViewContainer.startShimmer()
        viewModel.getAllHistory(shared.userId, hashById).observe(viewLifecycleOwner, Observer { history ->
            if (history.code == 6) {
                binding.shimmerViewContainer.apply {
                    stopShimmer()
                    visibility = View.GONE
                }
                if (history.data.driver.isNotEmpty()) {
                    Log.d("Developer", history.data.driver.size.toString())
                    val passengerHistoryAdapter = PassengerHistoryAdapter(requireContext(), history.data.driver) { passengerHandler(it) }
                    binding.history.layoutManager = LinearLayoutManager(requireContext())
                    binding.history.adapter = passengerHistoryAdapter
                } else {
                    binding.notFoundTitle.visibility = View.VISIBLE
                }
            } else {
                Utils.showErrorMessage(requireContext(), history.message)
            }
        })

    }


    private fun passengerHandler(passenger: Passenger) {
        val bundle = bundleOf()
        bundle.putString("tripId", passenger.id)
        bundle.putString("type", TripDetailsAction.DRIVERHISTORY.name)
        findNavController().navigate(R.id.action_global_tripDetailsFragment, bundle)
    }
}