package tj.behruz.savorcarTj.ui.login

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import tj.behruz.savorcarTj.R
import tj.behruz.savorcarTj.databinding.SmsFragmentBinding
import tj.behruz.savorcarTj.helpers.PreferenceHelper
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carbody
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carbodyId
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carclass
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carclassId
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carcolor
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carmark
import tj.behruz.savorcarTj.helpers.PreferenceHelper.gosnomer
import tj.behruz.savorcarTj.helpers.PreferenceHelper.haveCar
import tj.behruz.savorcarTj.helpers.PreferenceHelper.level
import tj.behruz.savorcarTj.helpers.PreferenceHelper.phone
import tj.behruz.savorcarTj.helpers.PreferenceHelper.userId
import tj.behruz.savorcarTj.helpers.PreferenceHelper.username
import tj.behruz.savorcarTj.helpers.Utils
import tj.behruz.savorcarTj.helpers.hideKeyboard
import tj.behruz.savorcarTj.ui.profile.UserViewModel

class SmsFragment: Fragment() {

    private val binding by lazy { SmsFragmentBinding.inflate(layoutInflater) }
    private val shared by lazy { PreferenceHelper.defaultPreference(requireContext()) }
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var progressDialog: ProgressDialog
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val number = arguments?.getString("phone", "")
        val code = arguments?.getString("code", "")
        val isExist = arguments?.getBoolean("is_exist")
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Подождите пожалуйста.")
        binding.smsCodeTitle.text = "Код аутентификации был отправлен на номер".plus(" ").plus(number)

        try {
            binding.smsCode.addTextChangedListener {
                val code = it.toString()
                binding.checkSms.isEnabled = code.length == 4 || code.length == 5

            }

            binding.changeNumber.setOnClickListener {
                requireActivity().onBackPressed()
            }
            binding.checkSms.setOnClickListener {
                progressDialog.show()
                it.hideKeyboard()
                if (binding.smsCode.text.toString() == code) {
                    if (isExist == true) {
                        getInfo()

                    } else {
                        progressDialog.dismiss()
                        findNavController().navigate(R.id.action_smsFragment_to_registrationFragment)
                    }
                } else {
                    progressDialog.dismiss()
                    Utils.showErrorMessage(requireContext(), "Неправелный код")
                }
            }

            viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
                Utils.showErrorMessage(requireContext(), it)
            })
        } catch (e: Exception) {
            Utils.showAlert(e.message.toString(), requireContext())
        }

    }

    private fun getInfo() {
        val hash = Utils.generateHash(shared.phone.plus("blabla").plus(shared.phone))

        userViewModel.signIn(shared.phone.toString(), hash).observe(viewLifecycleOwner, Observer {
            if (it.code == 6) {
                shared.userId = it.data.user_id.toInt()
                shared.username = it.data.name
                shared.level = it.data.level
                if (it.data.car != null) {
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
                progressDialog.dismiss()
                findNavController().navigate(R.id.action_smsFragment_to_homeFragment)
            } else {
                Utils.showSnackBar(binding.root, it.message)
            }
        })
    }
}