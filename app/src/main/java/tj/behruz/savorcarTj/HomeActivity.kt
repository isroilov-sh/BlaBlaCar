package tj.behruz.savorcarTj

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import tj.behruz.savorcarTj.databinding.ActivityMainBinding
import tj.behruz.savorcarTj.helpers.PreferenceHelper
import tj.behruz.savorcarTj.helpers.PreferenceHelper.haveCar
import tj.behruz.savorcarTj.helpers.PreferenceHelper.notification
import tj.behruz.savorcarTj.push.PushPayload

class HomeActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val preference by lazy { PreferenceHelper.defaultPreference(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_BlaBlaCar)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val push = intent.getParcelableExtra<PushPayload>("notification")
        if (push != null) {
            preference.notification = push.id
        }


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.reasonFragment, R.id.historyDescription, R.id.tripDetailsFragment, R.id.reverseFragment, R.id.preferenceFragment, R.id.travelInfoFragment, R.id.userViewFragment, R.id.allReviewFragment, R.id.myReviewFragment, R.id.cancelTripFragment, R.id.historyFragment, R.id.addTripSuccessFragment, R.id.registrationFragment, R.id.tripInfoFragment, R.id.bookingSuccessFragment, R.id.searchTripFragment, R.id.carInfoFragment, R.id.userInfoFragment, R.id.carPropertyFragment, R.id.loginFragment, R.id.smsFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }


        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.addTripFragment -> {
                    val bundle = Bundle()
                    bundle.putString("home", "home")
                    if (preference.haveCar) {
                        navController.navigate(R.id.action_global_addTripFragment)
                    } else {
                        navController.navigate(R.id.action_global_carInfoFragment, bundle)
                    }
                }
                R.id.homeFragment -> {
                    navController.navigate(R.id.action_global_homeFragment)
                }

                R.id.searchTravelFragment -> {
                    navController.navigate(R.id.action_global_searchTravelFragment)
                }

                R.id.profileFragment -> {
                    navController.navigate(R.id.action_global_profileFragment)
                }
            }
            false
        }
    }

}