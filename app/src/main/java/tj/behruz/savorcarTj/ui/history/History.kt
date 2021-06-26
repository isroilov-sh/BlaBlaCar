package tj.behruz.savorcarTj.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import tj.behruz.savorcarTj.R
import tj.behruz.savorcarTj.adapters.PassengerHistoryAdapter
import tj.behruz.savorcarTj.adapters.ViewPagerAdapter
import tj.behruz.savorcarTj.databinding.HistoryFragmentBinding
import tj.behruz.savorcarTj.helpers.PreferenceHelper
import tj.behruz.savorcarTj.helpers.PreferenceHelper.hashbyId
import tj.behruz.savorcarTj.helpers.PreferenceHelper.userId
import tj.behruz.savorcarTj.helpers.Utils
import tj.behruz.savorcarTj.models.DriverTripPayload
import tj.behruz.savorcarTj.models.Passenger
import tj.behruz.savorcarTj.ui.home.HomeViewModel
import tj.behruz.savorcarTj.ui.trips.DriversHistoryFragment
import tj.behruz.savorcarTj.ui.trips.HistoryDriverFragment
import tj.behruz.savorcarTj.ui.trips.HistoryPassengerFragment
import tj.behruz.savorcarTj.ui.trips.PassengerHistoryFragment

class HistoryFragment: Fragment() {


    private val binding by lazy { HistoryFragmentBinding.inflate(layoutInflater) }
    private val viewModel: HomeViewModel by viewModels()
    private val shared by lazy { PreferenceHelper.defaultPreference(requireContext()) }
    private val history = mutableListOf<Passenger>()
    private var selectedPosition = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = ViewPagerAdapter(childFragmentManager)

        adapter.addFragment(HistoryPassengerFragment(), "Пассажир")
        adapter.addFragment(HistoryDriverFragment(), "Водител")
        binding.viewPager.adapter = adapter
        binding.viewPager.currentItem = selectedPosition
        binding.tabs.setupWithViewPager(binding.viewPager, false)
        adapter.saveState()
        binding.viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                selectedPosition = position
            }

            override fun onPageSelected(position: Int) {
                selectedPosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {
            }


        })
    }


}