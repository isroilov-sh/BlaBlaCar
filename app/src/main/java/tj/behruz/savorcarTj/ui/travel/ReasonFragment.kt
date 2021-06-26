package tj.behruz.savorcarTj.ui.travel

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import tj.behruz.savorcarTj.R
import tj.behruz.savorcarTj.adapters.ReasonAdapter
import tj.behruz.savorcarTj.databinding.ReasonFragmentBinding
import tj.behruz.savorcarTj.helpers.PreferenceHelper
import tj.behruz.savorcarTj.helpers.PreferenceHelper.hashbyId
import tj.behruz.savorcarTj.helpers.PreferenceHelper.userId
import tj.behruz.savorcarTj.helpers.Utils

class ReasonFragment: Fragment() {
    private lateinit var progressDialog: ProgressDialog
    private val binding by lazy { ReasonFragmentBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<TravelViewModel>()
    private val default by lazy { PreferenceHelper.defaultPreference(requireContext()) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root
    private var uid = ""
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val reasons = listOf(
            "Я решил(а) поехать другим способом",
            "Водитель изменил место встречи",
            "Мне уже не подходить дата поездки",
            "Водитель попросил отменить бронирование",
            "Не удалось связаться с водителем",
            "Водитель больше не предлагает эту поездку",
            "Я забронировал(а) место по ошибке",
        )

        uid = requireArguments().getString("uid").toString()
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Подождите пожалуйста.")
        val adapterReason = ReasonAdapter(reasons) { handler(it) }
        binding.reasonRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterReason
        }

        binding.imageView2.setOnClickListener {
            requireActivity().onBackPressed()
        }
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Utils.showErrorMessage(requireContext(), it)
        })


    }

    private fun handler(reason: String) {
        progressDialog.show()
        viewModel.cancelBookingDriver(userId = default.userId, hash = default.hashbyId.toString(), bookingId = uid.toString(), comment = reason).observe(viewLifecycleOwner, Observer {
            progressDialog.dismiss()
            if (it.code == 6) {
                findNavController().navigate(R.id.action_global_homeFragment)
            } else {
                Utils.showErrorMessage(requireContext(), it.message)
            }
        })


    }

}