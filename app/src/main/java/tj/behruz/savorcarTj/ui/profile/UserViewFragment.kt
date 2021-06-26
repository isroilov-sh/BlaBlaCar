package tj.behruz.savorcarTj.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import tj.behruz.savorcarTj.R
import tj.behruz.savorcarTj.databinding.UserViewFragmentBinding
import tj.behruz.savorcarTj.helpers.PreferenceHelper
import tj.behruz.savorcarTj.helpers.PreferenceHelper.hashbyId
import tj.behruz.savorcarTj.helpers.PreferenceHelper.userId
import tj.behruz.savorcarTj.helpers.Utils

class UserViewFragment: Fragment() {

    private val binding by lazy { UserViewFragmentBinding.inflate(layoutInflater) }

    private val userViewModel by viewModels<UserViewModel>()
    private val preference by lazy { PreferenceHelper.defaultPreference(requireContext()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val userName = arguments?.getString("username")

        binding.progressBar.visibility=View.VISIBLE
        userViewModel.getUserInfo(preference.userId, preference.hashbyId.toString(), userName.toString()).observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility=View.GONE
            if (it.code == 6) {
                val payload = it.data
                binding.content.visibility=View.VISIBLE
                binding.numberTitle.visibility=View.VISIBLE
                binding.rate.visibility=View.VISIBLE
                Glide.with(this).load(payload.user_info.pimg).placeholder(R.drawable.ic_waiting).error(R.drawable.user_icon).into(binding.profileImage)
                binding.userAge.text = payload.user_info.birthday
                binding.userLevel.text = "Опыт: ".plus(payload.level)
                binding.userName.text = userName
                binding.rate.text=payload.total_rate.toString().plus(" ").plus("/").plus("5").plus("  - ${payload.total_rate}  отзывов")
            }else{
                Utils.showAlert(it.message,requireContext())
            }
        })

        binding.backEvent.setOnClickListener {
            requireActivity().onBackPressed()

        }
    }
}