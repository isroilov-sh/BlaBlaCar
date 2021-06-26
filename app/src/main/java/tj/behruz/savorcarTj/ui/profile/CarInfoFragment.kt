package tj.behruz.savorcarTj.ui.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import tj.behruz.savorcarTj.R
import tj.behruz.savorcarTj.databinding.CarInfoFragmentBinding
import tj.behruz.savorcarTj.helpers.PreferenceHelper
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carbody
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carbodyId
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carclass
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carclassId
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carcolor
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carimg
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carmark
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carmarkId
import tj.behruz.savorcarTj.helpers.PreferenceHelper.gosnomer
import tj.behruz.savorcarTj.helpers.PreferenceHelper.haveCar
import tj.behruz.savorcarTj.helpers.PreferenceHelper.userId
import tj.behruz.savorcarTj.helpers.Utils
import tj.behruz.savorcarTj.ui.login.carBody
import tj.behruz.savorcarTj.ui.login.carClass
import tj.behruz.savorcarTj.ui.login.carMark
import java.io.File

class CarInfoFragment: Fragment() {

    private var id = ""
    private lateinit var binding: CarInfoFragmentBinding
    private var isPhoto = true
    private val userViewModel: UserViewModel by viewModels()
    private val prefrence by lazy { PreferenceHelper.defaultPreference(requireContext()) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = CarInfoFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (prefrence.haveCar) {
            binding.addCardInfo.visibility = View.GONE
            binding.toolbar.visibility = View.VISIBLE
            binding.title.text = "Редактирование авто"

        } else {
            binding.addCardInfo.visibility = View.VISIBLE
            binding.toolbar.visibility = View.GONE
            binding.title.text = "Добавление авто"

        }


        var home = requireArguments().getString("home")
        if (home!!.isEmpty()) {
            binding.addCardInfo.visibility = View.GONE
            binding.toolbar.visibility = View.VISIBLE
        }
        setBiding()
        init()
        Utils.handler.observe(viewLifecycleOwner, Observer {

            when (it.type) {
                "getCarMark" -> {
                    carMark = it.property.id.toString()
                    binding.carMark.setText(it.property.name)
                    id = it.property.id.toString()
                }

                "getCarClass" -> {
                    id = ""
                    carClass = it.property.id.toString()
                    binding.carClass.setText(it.property.name)
                }

                "getCarBody" -> {
                    id = ""
                    carBody = it.property.id.toString()

                    binding.carBody.setText(it.property.name)
                }

            }


        })

        binding.backEventAdd.setOnClickListener {
            requireActivity().onBackPressed()
        }


        binding.saveCar.setOnClickListener {
            val carColor = binding.carColor.text.toString()
            val carNumber = binding.carNumber.text.toString()
            val hash = Utils.generateHash(prefrence.userId.toString().plus("blabla").plus(prefrence.userId))

            userViewModel.updateCar(carMark, carClass, carBody, carColor, carNumber, prefrence.userId, hash).observe(viewLifecycleOwner, Observer {
                if (it.code == 6) {
                    Utils.successSnackBar(binding.root, "Данные успешно сохраненые") { snackBar(it) }
                    prefrence.carbody = binding.carBody.text.toString()
                    prefrence.carclass = binding.carClass.text.toString()
                    prefrence.carmark = binding.carMark.text.toString()
                    prefrence.gosnomer = binding.carNumber.text.toString()
                    prefrence.carcolor = binding.carColor.text.toString()
                    prefrence.carbodyId = carBody
                    prefrence.carclassId = carClass
                    prefrence.carmarkId = carMark
                    prefrence.haveCar = true
                    if (home?.isNotEmpty()!!) {
                        findNavController().navigate(R.id.action_global_addTripFragment)
                    } else {
                        requireActivity().onBackPressed()
                    }


                }


            })


        }

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.fechImage.setOnClickListener {
            CropImage.activity().start(requireActivity(), this)

        }

        userViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Utils.showErrorMessage(requireContext(), it)
        })

    }

    private fun setBiding() {
        val bundle = Bundle()
        binding.carMark.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && isPhoto) {
                bundle.putString("type", "getCarMark")
                bundle.putString("title", "Марка автомобиля")
                bundle.putString("id", "")
                findNavController().navigate(R.id.action_global_carPropertyFragment, bundle)
            }
        }



        binding.carClass.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && isPhoto) {
                bundle.putString("type", "getCarClass")
                bundle.putString("title", "Модель автомобиля")
                bundle.putString("id", carMark)

                findNavController().navigate(R.id.action_global_carPropertyFragment, bundle)
            }
        }

        binding.carBody.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && isPhoto) {
                bundle.putString("type", "getCarBody")
                bundle.putString("title", "Модель автомобиля")
                bundle.putString("id", id)

                findNavController().navigate(R.id.action_global_carPropertyFragment, bundle)
            }
        }


    }

    private fun init() {

        if (prefrence.haveCar) {
            binding.title.setText("Редактировать авто")
        }
        binding.carMark.setText(prefrence.carmark.toString())
        binding.carBody.setText(prefrence.carbody.toString())
        binding.carClass.setText(prefrence.carclass.toString())
        binding.carNumber.setText(prefrence.gosnomer.toString())
        binding.carColor.setText(prefrence.carcolor.toString())
        binding.carMark.tag = prefrence.carmarkId
        binding.carBody.tag = prefrence.carbodyId
        binding.carClass.tag = prefrence.carclassId
        Glide.with(this).load(prefrence.carimg).placeholder(R.drawable.ic_waiting).error(R.drawable.ic_car).into(binding.carImage)


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) try {
                val resultUri = result.uri
                val path = getRealPathFromURI(requireContext(), resultUri)
                binding.carImage.setImageBitmap(BitmapFactory.decodeFile(resultUri.path));
                binding.progressBar.visibility = View.VISIBLE
                result.uri.path?.let { updateCarPhoto(it) }
                isPhoto = false

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun updateCarPhoto(path: String) {
        try {
            val userId = prefrence.userId.toString().toRequestBody(MultipartBody.FORM)
            val hash = Utils.generateHash(prefrence.userId.toString().plus("blabla").plus(prefrence.userId)).toRequestBody(MultipartBody.FORM)
            val action = "home".toRequestBody(MultipartBody.FORM)
            val method = "editCarPhoto".toRequestBody(MultipartBody.FORM)
            val file = File(path)
            val fileReqBody = file.asRequestBody("image/jpg".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("photo", file.name, fileReqBody)
            userViewModel.updateCarPhoto(action, method, part, userId, hash).observe(viewLifecycleOwner, Observer {
                binding.progressBar.visibility = View.GONE

                if (it.code == 6) {
                    Utils.showSnackBar(binding.root, it.message.toString())
                } else {
                    Utils.showSnackBar(binding.root, it.message)
                }

            })
        } catch (e: java.lang.Exception) {
            Log.d("BEHRUZ", e.message.toString())
        }


    }



    private fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor!!.moveToFirst()
            cursor!!.getString(columnIndex)
        } catch (e: Exception) {
            ""
        } finally {
            if (cursor != null) {
                cursor!!.close()
            }
        }
    }

    private fun snackBar(action: Int) {

    }


}