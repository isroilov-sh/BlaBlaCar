package tj.behruz.savorcarTj.ui.travel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import tj.behruz.savorcarTj.R
import tj.behruz.savorcarTj.adapters.TravelAdapter
import tj.behruz.savorcarTj.databinding.SearchTripsFragmentBinding
import tj.behruz.savorcarTj.helpers.PreferenceHelper
import tj.behruz.savorcarTj.helpers.PreferenceHelper.hashbyId
import tj.behruz.savorcarTj.helpers.PreferenceHelper.userId
import tj.behruz.savorcarTj.helpers.Utils
import tj.behruz.savorcarTj.models.TripPayload

class SearchTripFragment: Fragment() {

    private val binding by lazy { SearchTripsFragmentBinding.inflate(layoutInflater) }
    private val sharedPreferences by lazy { PreferenceHelper.defaultPreference(requireContext()) }
    private val viewModel: TravelViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Utils.hideKeyboardFrom(requireContext(), binding.root)
        val from = arguments?.getString("from")
        val to = arguments?.getString("to")
        val date = arguments?.getString("date")
        val count = arguments?.getString("count")
        number = count.toString()
        binding.from.text = from
        binding.to.text = to
        binding.progressBar2.visibility = View.VISIBLE

        binding.description.text = date.plus(",").plus(" ").plus(count).plus(" ").plus("Пассажир")
        viewModel.searchTrip(sharedPreferences.userId.toString(), from.toString().trim(), to.toString().trim(), date.toString(), count.toString().toInt(), sharedPreferences.hashbyId.toString()).observe(viewLifecycleOwner) {
            binding.progressBar2.visibility = View.GONE
            if (it.code == 6) {
                if (it.data.isNotEmpty()) {
                    val adapter = TravelAdapter(requireContext(), it.data) { handler(it) }
                    binding.travelsList.layoutManager = LinearLayoutManager(requireContext())
                    binding.travelsList.adapter = adapter
                } else {
                    binding.notFoundMessage.visibility = View.VISIBLE

                }

            } else {
                Utils.showSnackBar(binding.imageView5, it.message)
            }


        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Utils.showErrorMessage(requireContext(), errorMessage)
        }

        binding.backEvent.setOnClickListener {
            requireActivity().onBackPressed()
        }


    }

    private fun handler(tripPayload: TripPayload) {
        val bundle = bundleOf()
        bundle.putString("id", tripPayload.id)
        findNavController().navigate(R.id.action_searchTripFragment_to_travelInfoFragment, bundle)

    }


}