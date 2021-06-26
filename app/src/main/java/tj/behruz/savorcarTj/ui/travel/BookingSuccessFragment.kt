package tj.behruz.savorcarTj.ui.travel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import tj.behruz.savorcarTj.R
import tj.behruz.savorcarTj.databinding.BookingSuccessFragmentBinding

class BookingSuccessFragment: Fragment() {

    private val binding by lazy { BookingSuccessFragmentBinding.inflate(layoutInflater) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.goHome.setOnClickListener {
            findNavController().navigate(R.id.action_global_homeFragment)
        }
    }


}