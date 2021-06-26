package tj.behruz.savorcarTj.ui.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import tj.behruz.savorcarTj.databinding.AllReviewsFragmentBinding
import tj.behruz.savorcarTj.helpers.PreferenceHelper
import tj.behruz.savorcarTj.helpers.PreferenceHelper.hashbyId
import tj.behruz.savorcarTj.helpers.PreferenceHelper.userId
import tj.behruz.savorcarTj.helpers.Utils
import tj.behruz.savorcarTj.ui.profile.UserViewModel

class AllReviewFragment: Fragment() {

    private val binding by lazy { AllReviewsFragmentBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<UserViewModel>()
    private val default by lazy { PreferenceHelper.defaultPreference(requireContext()) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.backEvent.setOnClickListener {
            requireActivity().onBackPressed()
        }


        viewModel.getAllReviews(default.userId,default.hashbyId.toString()).observe(viewLifecycleOwner,Observer{
            binding.progressBar6.visibility=View.GONE

            if (it.code==6){
                binding.content.visibility=View.VISIBLE
                binding.allreviewTitle.text="Вы получили ${it.data.total_rate} отзыв(а) с средный оценкой"
                binding.allReviewsCount.text=it.data.total_rate.toString().plus("/").plus("5")
                binding.excellentCount.text=it.data.rate.excellent
                binding.goodCount.text=it.data.rate.good
                binding.nodBad.text=it.data.rate.nodBad
                binding.badCount.text=it.data.rate.bad
                binding.verybad.text=it.data.rate.veryBad
            }else{
                Utils.showErrorMessage(requireContext(),it.message)
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Utils.showErrorMessage(requireContext(),it)
        })


    }

}