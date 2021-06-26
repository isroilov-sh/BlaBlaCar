package tj.behruz.savorcarTj.ui.registration

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import tj.behruz.savorcarTj.R
import tj.behruz.savorcarTj.databinding.RegistrationFragmentBinding
import tj.behruz.savorcarTj.helpers.CustomTextWatcher
import tj.behruz.savorcarTj.helpers.PreferenceHelper
import tj.behruz.savorcarTj.helpers.PreferenceHelper.phone
import tj.behruz.savorcarTj.helpers.PreferenceHelper.userId
import tj.behruz.savorcarTj.helpers.RegisValid
import tj.behruz.savorcarTj.helpers.Utils
import tj.behruz.savorcarTj.ui.profile.UserViewModel
import java.util.*

class RegistrationFragment: Fragment() {


    private val binding by lazy { RegistrationFragmentBinding.inflate(layoutInflater) }
    private val genders = arrayOf("Мужской", "Женский")
    private val sharedPreferences by lazy { PreferenceHelper.defaultPreference(requireContext()) }
    private val userViewModel: UserViewModel by viewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        try {
            binding.backEvent.setOnClickListener {
                requireActivity().onBackPressed()
            }

            userViewModel.errorMessage.observe(viewLifecycleOwner) {
                Utils.showErrorMessage(requireContext(), it)
            }


            init()

        } catch (e: Exception) {
            e.printStackTrace()
            Utils.showAlert(e.message.toString(), requireContext())
        }


    }


    private fun init() {
        val genderAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, genders)
        binding.gender.setText(genders[0])
        binding.gender.setAdapter(genderAdapter)
        binding.birthday.addTextChangedListener(CustomTextWatcher(binding.birthday, "##.##.####"))
        val hash = Utils.generateHash("blablaalbalb")

        userViewModel.getCitiesAsync(hash).observe(viewLifecycleOwner, { response ->

            if (response.code == 6) {
                val cities = arrayListOf<String>()
                for (i in response.data!!) {
                    cities.add(i.name)
                }


                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, cities)
                binding.town.setAdapter(adapter)

            }


        })

        binding.dateLayout.setEndIconOnClickListener {
            // Respond to end icon presses
            showCalendar()
        }
        binding.birthday.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showCalendar()
            }
        }

        binding.registration.setOnClickListener {
            val valid = RegisValid()

            if (valid == validate()) {
                login()
            }
        }


    }

    @SuppressLint("SetTextI18n")
    private fun showCalendar() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->
            val month = monthOfYear + 1
            var formattedMonth = month.toString()
            if (month < 10) {
                formattedMonth = "0$month"
            }
            var formattedDayOfMonth = "" + dayOfMonth
            if (dayOfMonth < 10) {
                formattedDayOfMonth = "0$dayOfMonth"
            }

            // Display Selected date in Toast
            binding.birthday.setText("""$formattedDayOfMonth.${formattedMonth}.$year""")
        }, year, month, day)
        dpd.show()
    }

    private fun validate(): RegisValid {
        val regisValid = RegisValid()
        if (binding.name.text.toString().isNotEmpty()) {
            regisValid.name = true
            binding.nameLayout.error = null
        } else {
            regisValid.name = false
            binding.nameLayout.error = "Запольните имя"
        }
        if (binding.town.text.toString().isNotEmpty()) {
            regisValid.city = true
            binding.townLayout.error = null
        } else {
            regisValid.city = false
            binding.townLayout.error = "Запольните город"
        }

        if (binding.birthday.text.toString().isNotEmpty()) {
            regisValid.birthdate = true
            binding.dateLayout.error = null
        } else {
            regisValid.birthdate = false
            binding.dateLayout.error = "Запольните дата рождение"
        }
        return regisValid
    }

    private fun login() {

        val name = binding.name.text.toString()
        val lastName = binding.surName.text.toString()
        val phone = PreferenceHelper.defaultPreference(requireContext()).phone.toString()
        val city = binding.town.text.toString() ?: " "
        val email = binding.email.text.toString() ?: " "
        val dates = binding.birthday.text.toString().split(".")
        val date = dates[2].plus("-").plus(dates[1]).plus("-").plus(dates[0])
        val hash = Utils.generateHash(phone.plus("blabla").plus(name).plus(name))

        userViewModel.registrationAsync(phone = phone, email = email, gender = genders.indexOf(binding.gender.text.toString()), bdate = date, city = city, first_name = name, last_name = lastName, car_mark = "", car_color = "", gos_nomer = "", hash = hash, car_class = "", car_body = "").observe(viewLifecycleOwner, {
            if (it.code == 6) {
                sharedPreferences.userId = it.data.user_id
                findNavController().navigate(R.id.action_registrationFragment_to_homeFragment)
            } else {
                Utils.showSnackBar(binding.root, it.message)
            }


        })


    }


}
