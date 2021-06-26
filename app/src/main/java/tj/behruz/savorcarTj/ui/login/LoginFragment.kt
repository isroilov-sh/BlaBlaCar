package tj.behruz.savorcarTj.ui.login

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import tj.behruz.savorcarTj.R
import tj.behruz.savorcarTj.databinding.LoginFragmentBinding
import tj.behruz.savorcarTj.helpers.PreferenceHelper
import tj.behruz.savorcarTj.helpers.PreferenceHelper.phone
import tj.behruz.savorcarTj.helpers.PreferenceHelper.userId
import tj.behruz.savorcarTj.helpers.Utils
import tj.behruz.savorcarTj.helpers.hideKeyboard

class LoginFragment: Fragment() {

    private val binding by lazy { LoginFragmentBinding.inflate(layoutInflater) }
    private val viewModel: LoginViewModel by viewModels()
    private val default by lazy { PreferenceHelper.defaultPreference(requireContext()) }
    private lateinit var progressDialog: ProgressDialog
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (default.userId > 0) {
            findNavController().navigate(R.id.action_global_homeFragment)
            return
        }

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Подождите пожалуйста.")


        val ruleText = SpannableString(requireContext().getString(R.string.rule_text))
        ruleText.setSpan(ForegroundColorSpan(Color.BLUE), 20, 28, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.ruleText.text = ruleText
        binding.login.setOnClickListener {
            val phone = "+992".plus(binding.numberPhone.text.toString())
            it.hideKeyboard()
            login(phone)
        }

        binding.numberPhone.addTextChangedListener {
            binding.login.isEnabled = it.toString().length == 9 && binding.checkBox.isChecked

        }

        binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->

            binding.login.isEnabled = binding.numberPhone.text.toString().length == 9 && isChecked

        }

        binding.ruleText.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://blablacar.tj/privacy-policy/"))
            startActivity(browserIntent)
        }
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

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Utils.showErrorMessage(requireContext(),it)
            progressDialog.dismiss()
        })

    }
    private fun login(phone: String) {

        progressDialog.show()
        val hash = Utils.generateHash(phone + phone + "blabla")
        Log.d("DEVELOPER","{$phone}")
        viewModel.login(phone, hash).observe(viewLifecycleOwner) { response ->
            Log.d("LoginFragment", "login: $response")
            progressDialog.dismiss()
            if (response.code == 6) {
                val bundle = bundleOf()
                bundle.putString("code", response.data.code.toString())
                bundle.putString("phone", phone)
                bundle.putBoolean("is_exist", response.data.is_exist)
                default.phone = phone
                findNavController().navigate(R.id.action_loginFragment_to_smsFragment, bundle)

            } else {
                Utils.showErrorMessage(requireContext(), response.message)
            }

        }

    }

}
