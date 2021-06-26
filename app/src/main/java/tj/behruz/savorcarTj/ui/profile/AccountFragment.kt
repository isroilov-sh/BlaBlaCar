package tj.behruz.savorcarTj.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import tj.behruz.savorcarTj.R
import tj.behruz.savorcarTj.databinding.AccountFragmentBinding
import tj.behruz.savorcarTj.helpers.PreferenceHelper
import tj.behruz.savorcarTj.helpers.PreferenceHelper.clearValues
import tj.behruz.savorcarTj.helpers.PreferenceHelper.userId
import tj.behruz.savorcarTj.helpers.PreferenceHelper.username

class AccountFragment: Fragment() {

    private lateinit var binding: AccountFragmentBinding
    private val default by lazy { PreferenceHelper.defaultPreference(requireContext()) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = AccountFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.historyAsPassenger.setOnClickListener {
            findNavController().navigate(R.id.action_global_historyFragment)
        }

        binding.allstats.setOnClickListener {
            findNavController().navigate(R.id.action_global_myReviewFragment)
        }

        binding.stats.setOnClickListener {
            findNavController().navigate(R.id.action_global_allReviewFragment)
        }

        binding.help.setOnClickListener {
            sendReview()
        }

        binding.agreement.setOnClickListener {
            sendReview()
        }

        binding.security.setOnClickListener {
            sendReview()
        }



        binding.exit.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).setTitle("Выход").setMessage("Вы действительно хотите выйти из приложение ?").setNegativeButton(resources.getString(R.string.decline)) { dialog, which ->
                // Respond to negative button press
            }.setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                default.userId = 0
                default.username = ""
                findNavController().navigate(R.id.action_global_loginFragment)
            }.show()


        }

    }

    private fun sendReview() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://blablacar.tj/privacy-policy/"))
        startActivity(browserIntent)

    }
}