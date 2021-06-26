package tj.behruz.savorcarTj.ui.passenger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import tj.behruz.savorcarTj.R
import tj.behruz.savorcarTj.databinding.PassngerTripInfoBinding
import tj.behruz.savorcarTj.helpers.PreferenceHelper
import tj.behruz.savorcarTj.helpers.Utils
import tj.behruz.savorcarTj.ui.travel.TravelViewModel

class PassengerTripFragment: Fragment() {

    private val binding by lazy { PassngerTripInfoBinding.inflate(layoutInflater) }
    private var uid = ""
    private val viewModel by viewModels<TravelViewModel>()
    private val default by lazy { PreferenceHelper.defaultPreference(requireContext()) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val fromCity = arguments?.getString("from") ?: ""
        val to = arguments?.getString("to") ?: ""
        val price = arguments?.getString("price") ?: ""
        val date = arguments?.getString("date") ?: ""
        val dates = date.split(" ")
        val days = dates[0].split("-")
        val id = arguments?.getString("id") ?: ""
        val tripid = arguments?.getString("tripid") ?: ""
        uid = arguments?.getString("uid") ?: ""
        val userName = arguments?.getString("username") ?: ""
        val phone = arguments?.getString("phone") ?: ""
        val count = arguments?.getString("count") ?: "1"
        binding.historyDriverDate.text = days[2].plus(" ${Utils.getMonthByNumber(days[1])}").plus(" в ").plus(dates[1].removeRange(dates[1].length - 3, dates[1].length))
        binding.fromCityDriver.text = fromCity
        binding.toCityDriver.text = to
        binding.pricePerson.text.toString()
        binding.pricePerson.text = price
        binding.userName.text = userName
        binding.phone.text = phone

        binding.backEvent.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.cancelTrip.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("uid", uid)
            findNavController().navigate(R.id.action_global_reasonFragment, bundle)

        }

        binding.userName.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("username", binding.userName.text.toString())
            findNavController().navigate(R.id.action_global_userViewFragment, bundle)
        }

    }

    private fun handler(position: Int) {

    }

}