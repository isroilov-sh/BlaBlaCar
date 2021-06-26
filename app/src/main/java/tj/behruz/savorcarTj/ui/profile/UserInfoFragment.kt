package tj.behruz.savorcarTj.ui.profile

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import tj.behruz.savorcarTj.R
import tj.behruz.savorcarTj.databinding.UserInfoFragmentBinding
import tj.behruz.savorcarTj.helpers.PreferenceHelper
import tj.behruz.savorcarTj.helpers.PreferenceHelper.phone
import tj.behruz.savorcarTj.helpers.PreferenceHelper.userId
import tj.behruz.savorcarTj.helpers.Utils
import java.io.File
import java.util.*

class UserInfoFragment: Fragment() {

    private var bdate = ""
    private lateinit var binding: UserInfoFragmentBinding
    private val userViewModel: UserViewModel by viewModels()
    var genders = listOf("Мужской", "Женский")

    private val sharedPreferences by lazy { PreferenceHelper.defaultPreference(requireContext()) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = UserInfoFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val email=requireArguments().getBoolean("email",false)
        if (email){
            binding.gender.isEnabled=false
            binding.userBirthday.isEnabled=false
            binding.userName.isEnabled=false
            binding.city.isEnabled=false
        }


        val hash = Utils.generateHash(sharedPreferences.phone + "blabla" + sharedPreferences.phone)
        init()
        setEvents()
        userViewModel.signIn(sharedPreferences.phone.toString(), hash).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it.code == 6) {
                val info = it.data
                binding.userName.setText(info.name)
                binding.email.setText(info.email)
                binding.city.setText(info.cname)
                binding.userBirthday.setText(info.birthday)
                Glide.with(this).load(it.data.pimg).placeholder(R.drawable.ic_waiting).error(R.drawable.user_icon).into(binding.profileImage)
            }

        })

        binding.saveProfile.setOnClickListener {

            updateProfile()
        }

        binding.backEvent.setOnClickListener {
            requireActivity().onBackPressed()
        }
        userViewModel.errorMessage.observe(viewLifecycleOwner, {
            Utils.showErrorMessage(requireContext(),it)
        })


    }

    private fun setEvents() {

        binding.cropImage.setOnClickListener {
            CropImage.activity().start(requireActivity(), this)
        }

        binding.userBirthday.setOnClickListener {
            showCalendar(binding.userBirthday)
        }
    }

    private fun init() {
        try {
            val genderAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, genders)
            binding.gender.adapter = genderAdapter
            genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            val hash = Utils.generateHash(sharedPreferences.phone.plus("blabla").plus(sharedPreferences.phone))
            userViewModel.signIn(sharedPreferences.phone.toString(), hash).observe(viewLifecycleOwner, androidx.lifecycle.Observer { response ->
                binding.nestedScrollView.visibility = View.VISIBLE
                binding.progressBar2.visibility = View.GONE
                if (response.code == 6) {
                    binding.userName.setText(response.data.name)
                    binding.city.setText(response.data.cname)
                    binding.email.setText(response.data.email)
                    binding.userBirthday.setText(response.data.birthday)

                    binding.gender.setSelection(genderAdapter.getPosition(genders[response.data.gander.toInt()]))
                }

            })

            val cityHash = Utils.generateHash("blablaalbalb")
            userViewModel.getCitiesAsync(cityHash).observe(viewLifecycleOwner) { response ->
                if (response.code == 6) {
                    val cities = arrayListOf<String>()
                    for (i in response.data!!) {
                        cities.add(i.name)
                    }
                    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, cities)
                    binding.city.setAdapter(adapter)

                }


            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) try {
                val resultUri = result.uri
                binding.profileImage.setImageBitmap(BitmapFactory.decodeFile(resultUri.path));
                binding.progressBar2.visibility = View.VISIBLE
                result.uri.path?.let { updateUserProfile(it) }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun updateProfile() {

        val updateHash = Utils.generateHash(sharedPreferences.userId.toString().plus("blabla").plus(sharedPreferences.userId))

            userViewModel.updateProfile(binding.userName.text.toString(), binding.city.text.toString(), binding.userBirthday.text.toString(), genders.indexOf(binding.gender.selectedItem.toString()), userId = sharedPreferences.userId, hash = updateHash,email = binding.email.text.toString()).observe(viewLifecycleOwner, androidx.lifecycle.Observer {


                if (it.code == 6) {
                    Utils.successSnackBar(binding.root, "Профиль успешно изменён") { handler(it) }
                    requireActivity().onBackPressed()

                } else {
                    Utils.showSnackBar(binding.root, it.message)
                }

            })

    }

    private fun updateUserProfile(path: String) {
        try {
            val userId = sharedPreferences.userId.toString().toRequestBody(MultipartBody.FORM)
            val hash = Utils.generateHash(sharedPreferences.userId.toString().plus("blabla").plus(sharedPreferences.userId)).toRequestBody(MultipartBody.FORM)
            val action = "home".toRequestBody(MultipartBody.FORM)
            val method = "editProfilePhoto".toRequestBody(MultipartBody.FORM)
            val file = File(path)
            val fileReqBody = file.asRequestBody("image/jpg".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("photo", file.name, fileReqBody)
            userViewModel.updateProfilePhoto(action, method, part, userId, hash).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                binding.progressBar2.visibility = View.GONE

                if (it.code == 200) {


                } else {
                    Utils.showSnackBar(binding.root, it.message)
                }

            })
        } catch (e: java.lang.Exception) {
            Log.d("BEHRUZ", e.message.toString())
        }


    }



    private fun showCalendar(field: EditText) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(), { view, year, monthOfYear, dayOfMonth ->
            val month = monthOfYear + 1
            var formattedMonth = month.toString()
            if (month < 10) {
                formattedMonth = "0$month"
            }
            var formattedDayOfMonth = "" + dayOfMonth
            if (dayOfMonth < 10) {
                formattedDayOfMonth = "0$dayOfMonth"
            }
            bdate = year.toString().plus("-").plus(formattedMonth).plus("-").plus(formattedDayOfMonth)
            // Display Selected date in Toast
            field.setText("""$formattedDayOfMonth-${formattedMonth}-$year""")
        }, year, month, day)
        dpd.show()
    }

    private fun handler(action: Int) {

    }


}