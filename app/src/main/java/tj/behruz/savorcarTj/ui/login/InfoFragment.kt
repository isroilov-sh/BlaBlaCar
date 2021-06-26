package tj.behruz.savorcarTj.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import tj.behruz.savorcarTj.R
import tj.behruz.savorcarTj.databinding.InfoFragmentBinding
import tj.behruz.savorcarTj.helpers.PreferenceHelper
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carbodyId
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carclass
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carclassId
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carcolor
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carimg
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carmark
import tj.behruz.savorcarTj.helpers.PreferenceHelper.carmarkId
import tj.behruz.savorcarTj.helpers.PreferenceHelper.haveCar
import tj.behruz.savorcarTj.helpers.PreferenceHelper.phone
import tj.behruz.savorcarTj.helpers.Utils
import tj.behruz.savorcarTj.ui.profile.UserViewModel

var carMark = ""
var carClass = ""
var carBody = ""
class InfoFragment:Fragment() {

    private val viewModel: UserViewModel by viewModels()


    private lateinit var binding: InfoFragmentBinding
    private val sharedPreferences by lazy { PreferenceHelper.defaultPreference(requireContext()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = InfoFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.number.text = sharedPreferences.phone


        val hash = Utils.generateHash(sharedPreferences.phone + "blabla" + sharedPreferences.phone)

        viewModel.signIn(sharedPreferences.phone.toString(), hash).observe(viewLifecycleOwner, Observer {
            if (it.code == 6) {
                val info = it.data
                if (info.car != null) {
                    sharedPreferences.carcolor = info.car.car_color
                    sharedPreferences.carclassId = info.car.car_class_id
                    sharedPreferences.carclass = info.car.car_class
                    sharedPreferences.carbodyId = info.car.car_body_id
                    sharedPreferences.carmarkId = info.car.car_mark_id
                    sharedPreferences.carmark = info.car.car_mark
                    sharedPreferences.carimg = info.car.cimg
                    if (!info.car.cimg.contains("def.png")){
                        Glide.with(this).load(info.car.cimg).placeholder(R.drawable.ic_waiting).error(R.drawable.ic_car).into(binding.carIcon)
                    }
                }

            } else {
                Utils.showSnackBar(binding.root, it.message)
            }

        })





        if (sharedPreferences.haveCar) {
            binding.addCard.text = "Редактировать авто"
            binding.carIcon.visibility=View.VISIBLE
            binding.carTitle.visibility=View.VISIBLE
            binding.carTitle.text = sharedPreferences.carmark.plus(" ${sharedPreferences.carclass}")


        } else {
            binding.addCard.text = "Добавить авто"
            binding.carIcon.visibility=View.GONE
            binding.carTitle.visibility=View.GONE
            binding.carTitle.text = sharedPreferences.carmark.plus(" ${sharedPreferences.carclass}")

        }


        carMark = sharedPreferences.carmarkId.toString()
        carBody = sharedPreferences.carbodyId.toString()
        carClass = sharedPreferences.carclassId.toString()

        binding.confirmEmail.setOnClickListener {
            val bundle=Bundle()
            bundle.putBoolean("email",true)
            findNavController().navigate(R.id.action_global_userInfoFragment,bundle)
        }

        binding.addCard.setOnClickListener {
            val bundle=Bundle()
            bundle.putString("home","")
            findNavController().navigate(R.id.action_global_carInfoFragment,bundle)
        }

        binding.addHobby.setOnClickListener {
            findNavController().navigate(R.id.action_global_preferenceFragment)
        }


    }

}