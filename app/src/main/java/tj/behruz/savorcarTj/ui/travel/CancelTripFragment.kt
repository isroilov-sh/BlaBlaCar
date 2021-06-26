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
import tj.behruz.savorcarTj.R
import tj.behruz.savorcarTj.databinding.CancelTripFragmentBinding
import tj.behruz.savorcarTj.helpers.PreferenceHelper
import tj.behruz.savorcarTj.helpers.PreferenceHelper.hashbyId
import tj.behruz.savorcarTj.helpers.PreferenceHelper.userId
import tj.behruz.savorcarTj.helpers.Utils

class CancelTripFragment: Fragment() {

    private val binding by lazy { CancelTripFragmentBinding.inflate(layoutInflater) }
    private val travelViewModel: TravelViewModel by viewModels()
    private val prefrence by lazy { PreferenceHelper.defaultPreference(requireContext()) }
    private lateinit var progressDialog: ProgressDialog
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Подождите пожалуйста.")
        try {
            val id = requireArguments().getString("id")

            binding.cancelButton.setOnClickListener {
                progressDialog.show()
                travelViewModel.cancelTrip(prefrence.userId, prefrence.hashbyId.toString(), id?.toInt()!!).observe(viewLifecycleOwner, Observer {
                    progressDialog.dismiss()
                    if (it.code == 6) {
                        findNavController().navigate(R.id.action_global_homeFragment)
                    } else {
                        Utils.showErrorMessage(requireContext(), it.message)
                    }
                })
            }

            travelViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
                progressDialog.dismiss()
                Utils.showErrorMessage(requireContext(), it)
            })

            binding.imageView12.setOnClickListener {
                requireActivity().onBackPressed()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}