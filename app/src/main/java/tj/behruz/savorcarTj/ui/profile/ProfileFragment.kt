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
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.appbar.AppBarLayout
import com.theartofdev.edmodo.cropper.CropImage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import tj.behruz.savorcarTj.R
import tj.behruz.savorcarTj.adapters.ViewPagerAdapter
import tj.behruz.savorcarTj.databinding.ProfileFragmentBinding
import tj.behruz.savorcarTj.helpers.PreferenceHelper
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carbodyId
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carclass
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carclassId
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carcolor
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carimg
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carmark
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carmarkId
import tj.behruz.savorcarTj.helpers.PreferenceHelper.level
import tj.behruz.savorcarTj.helpers.PreferenceHelper.phone
import tj.behruz.savorcarTj.helpers.PreferenceHelper.userId
import tj.behruz.savorcarTj.helpers.PreferenceHelper.username
import tj.behruz.savorcarTj.helpers.Utils
import tj.behruz.savorcarTj.ui.login.InfoFragment
import java.io.File

private var selectedPosition = 0

class ProfileFragment: Fragment() {

    private lateinit var binding: ProfileFragmentBinding
    private val viewModel: UserViewModel by viewModels()
    private val sharedPreferences by lazy { PreferenceHelper.defaultPreference(requireContext()) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ProfileFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val adapter = ViewPagerAdapter(childFragmentManager)
        setupCollapseToolbar("")
        binding.menuIcon.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.photo -> {
                        CropImage.activity().start(requireActivity(), this)
                        true
                    }
                    R.id.editProfile -> {
                        val bundle = Bundle()
                        bundle.putBoolean("email", false)
                        findNavController().navigate(R.id.action_global_userInfoFragment, bundle)
                        true

                    }
                    else -> false

                }


            }
            popupMenu.inflate(R.menu.popup_menu)
            popupMenu.show()
        }
        val hash = Utils.generateHash(sharedPreferences.phone + "blabla" + sharedPreferences.phone)
        binding.userLevel.text = sharedPreferences.level.toString()
        binding.profileUserName.text = sharedPreferences.username.toString()
        viewModel.signIn(sharedPreferences.phone.toString(), hash).observe(viewLifecycleOwner, Observer {
            if (it.code == 6) {
                val info = it.data
                binding.profileUserName.text = info.name
                binding.userLevel.text = info.level
                if (info.car != null) {
                    sharedPreferences.carcolor = info.car.car_color
                    sharedPreferences.carclassId = info.car.car_class_id
                    sharedPreferences.carclass = info.car.car_class
                    sharedPreferences.carbodyId = info.car.car_body_id
                    sharedPreferences.carmarkId = info.car.car_mark_id
                    sharedPreferences.carmark = info.car.car_mark
                    sharedPreferences.carimg = info.car.cimg
                }
                Glide.with(this).load(it.data.pimg).diskCacheStrategy(DiskCacheStrategy.RESOURCE).
                placeholder(R.drawable.ic_waiting).error(R.drawable.user_icon).into(binding.userAvatar)
            } else {
                Utils.showSnackBar(binding.root, it.message)
            }

        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Utils.showErrorMessage(requireContext(), it)
        })

            adapter.addFragment(InfoFragment(), "Информация")
            adapter.addFragment(AccountFragment(), "Аккаунт")
            binding.viewPager.adapter = adapter
            binding.viewPager.currentItem = selectedPosition
            binding.tabs.setupWithViewPager(binding.viewPager, false)

            binding.viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    selectedPosition = position
                }

                override fun onPageSelected(position: Int) {
                    selectedPosition = position
                }

                override fun onPageScrollStateChanged(state: Int) {
                }


            })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) try {
                val resultUri = result.uri
                var path = getRealPathFromURI(requireContext(), resultUri)
                binding.userAvatar.setImageBitmap(BitmapFactory.decodeFile(resultUri.path));
                updateUserProfile(resultUri.path.toString())

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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
            viewModel.updateProfilePhoto(action, method, part, userId, hash).observe(viewLifecycleOwner) {
                Utils.showSnackBar(binding.root, it.message)
            }
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
            cursor.moveToFirst()
            cursor.getString(columnIndex)
        } catch (e: Exception) {
            ""
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
    }

    private fun setupCollapseToolbar(title: String) {
        binding.appBar.addOnOffsetChangedListener(object: AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, i: Int) {
                if (scrollRange == -1) {
                    scrollRange = binding.appBar.totalScrollRange
                    isShow = true
                }
                if (scrollRange + i == 0) {
                    isShow = true
                    binding.title.text = binding.profileUserName.text.toString()

                } else if (isShow) {
                    isShow = false
                    binding.title.text = "Профиль"

                }
            }
        })
    }
}