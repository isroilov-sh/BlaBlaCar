package tj.behruz.savorcarTj.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import tj.behruz.savorcarTj.adapters.CarPropertyAdapter
import tj.behruz.savorcarTj.databinding.CarPropertyFragmentBinding
import tj.behruz.savorcarTj.helpers.Utils
import tj.behruz.savorcarTj.models.CarInfo
import tj.behruz.savorcarTj.models.CarProperty

class CarPropertyFragment:Fragment() {
    private val binding by lazy { CarPropertyFragmentBinding.inflate(layoutInflater) }
    private val userViewModel: UserViewModel by viewModels()
    private var type = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val hash = Utils.generateHash("blablaalbalb")
        type = arguments?.getString("type").toString()
        val title = arguments?.getString("title")
        val id = arguments?.getString("id")
        binding.title.text = title

        Utils.hideKeyboardFrom(requireContext(), binding.root)
        if (id!!.isNotEmpty()) {
            userViewModel.getCarClass(type, id.toInt(), Utils.generateHash("blablaalbalb$id")).observe(viewLifecycleOwner, Observer {
                if (it.code == 6) {
                    binding.progressBar5.visibility = View.GONE
                    val adapter = CarPropertyAdapter(it.data!!) { itemHandler(it) }
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    binding.recyclerView.adapter = adapter

                }
            })
        } else {
            userViewModel.getCarMarks(type, hash).observe(viewLifecycleOwner, Observer { carPropertyModel ->
                if (carPropertyModel.code == 6) {
                    binding.progressBar5.visibility = View.GONE
                    val adapter = CarPropertyAdapter(carPropertyModel.data!!) { itemHandler(it) }
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    binding.recyclerView.adapter = adapter

                }
            })
        }

        binding.backEvent.setOnClickListener {
            requireActivity().onBackPressed()
        }
        userViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Utils.showErrorMessage(requireContext(),it)
        })


    }
    private fun itemHandler(carProperty: CarProperty) {
        requireActivity().onBackPressed()

        Utils.handler.postValue(CarInfo(type, carProperty))

    }

}