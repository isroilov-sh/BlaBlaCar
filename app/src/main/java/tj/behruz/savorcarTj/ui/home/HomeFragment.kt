package tj.behruz.savorcarTj.ui.home

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import tj.behruz.savorcarTj.R
import tj.behruz.savorcarTj.adapters.ViewPagerAdapter
import tj.behruz.savorcarTj.databinding.HomeFragmentBinding
import tj.behruz.savorcarTj.helpers.PreferenceHelper
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carbody
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carbodyId
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carclass
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carclassId
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carcolor
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carmark
import tj.behruz.savorcarTj.helpers.PreferenceHelper.gosnomer
import tj.behruz.savorcarTj.helpers.PreferenceHelper.hashbyId
import tj.behruz.savorcarTj.helpers.PreferenceHelper.haveCar
import tj.behruz.savorcarTj.helpers.PreferenceHelper.level
import tj.behruz.savorcarTj.helpers.PreferenceHelper.notification
import tj.behruz.savorcarTj.helpers.PreferenceHelper.phone
import tj.behruz.savorcarTj.helpers.PreferenceHelper.userId
import tj.behruz.savorcarTj.helpers.PreferenceHelper.username
import tj.behruz.savorcarTj.helpers.Utils
import tj.behruz.savorcarTj.ui.profile.UserViewModel
import tj.behruz.savorcarTj.ui.trips.DriversHistoryFragment
import tj.behruz.savorcarTj.ui.trips.PassengerHistoryFragment


class HomeFragment: Fragment() {
    private var selecedPosition = 0
    private lateinit var binding: HomeFragmentBinding
    private val shared by lazy { PreferenceHelper.defaultPreference(requireContext()) }
    private val userViewModel: UserViewModel by viewModels()
    val bundle = bundleOf()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = HomeFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val adapter = ViewPagerAdapter(childFragmentManager)
        val hashById = Utils.generateHash(shared.userId.toString().plus("blabla").plus(shared.userId))
        shared.hashbyId = hashById
        adapter.addFragment(PassengerHistoryFragment(), "Пассажир")
        adapter.addFragment(DriversHistoryFragment(), "Водител")
        binding.viewPager.adapter = adapter
        binding.viewPager.currentItem = selecedPosition
        binding.tabs.setupWithViewPager(binding.viewPager, false)
        if (shared.userId > 0) {
            sendFireBaseToken()
        }

        adapter.saveState()
        binding.viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                selecedPosition = position
            }

            override fun onPageSelected(position: Int) {
                selecedPosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {
            }


        })

        sendFireBaseToken()
        var doubleBackToExitPressedOnce = false
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (doubleBackToExitPressedOnce) {
                activity?.finishAffinity()
                return@addCallback
            }

            doubleBackToExitPressedOnce = true
            Toast.makeText(requireContext(), "Нажмите еще раз чтобы выйти из приложения", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getInfo()

        if (shared.notification!!.isNotEmpty()) {
            bundle.putString("tripId", shared.notification)
            shared.notification = ""
            findNavController().navigate(R.id.action_global_tripDetailsFragment, bundle)
        }

    }

    private fun getInfo() {
        val hash = Utils.generateHash(shared.phone.plus("blabla").plus(shared.phone))
        userViewModel.signIn(shared.phone.toString(), hash).observe(viewLifecycleOwner, Observer {
            if (it.code == 6) {
                shared.userId = it.data.user_id.toInt()
                if (it.data.car != null) {
                    shared.userId = it.data.user_id.toInt()
                    shared.username = it.data.name
                    shared.level = it.data.level
                    shared.haveCar = true
                    shared.carmark = it.data.car.car_mark
                    shared.carclass = it.data.car.car_class
                    shared.carbody = it.data.car.car_body
                    shared.gosnomer = it.data.car.gos_nomer
                    shared.carcolor = it.data.car.car_color
                    shared.carbodyId = it.data.car.car_body_id
                    shared.carmark = it.data.car.car_mark_id
                    shared.carclassId = it.data.car.car_class_id
                }
            } else {
                Utils.showSnackBar(binding.root, it.message)
            }
        })
    }

    private fun sendFireBaseToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener { task ->
            // 2
            if (!task.isSuccessful) {
                return@OnCompleteListener
            } else {
                Log.d("DEVELOPER", "token${task.result!!.token.toString()}")
                userViewModel.sendFireBaseToken(shared.userId, task.result!!.token, shared.hashbyId.toString())
            }
            // 3
            val token = task.result?.token

        })
    }

    fun NavController.navigateSafe(@IdRes resId: Int, args: Bundle? = null, navOptions: NavOptions? = null, navExtras: Navigator.Extras? = null) {
        val action = currentDestination?.getAction(resId) ?: graph.getAction(resId)
        if (action != null && currentDestination?.id != action.destinationId) {
            navigate(resId, args, navOptions, navExtras)
        }
    }
}

