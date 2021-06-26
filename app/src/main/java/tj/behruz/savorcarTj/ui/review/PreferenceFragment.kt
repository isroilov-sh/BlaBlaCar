package tj.behruz.savorcarTj.ui.review

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.ArrayMap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import tj.behruz.savorcarTj.adapters.PreferenceAdapter
import tj.behruz.savorcarTj.databinding.PrefrenceFragmentBinding
import tj.behruz.savorcarTj.helpers.PreferenceHelper
import tj.behruz.savorcarTj.helpers.PreferenceHelper.hashbyId
import tj.behruz.savorcarTj.helpers.PreferenceHelper.userId
import tj.behruz.savorcarTj.models.Preference
import tj.behruz.savorcarTj.models.Refrence
import tj.behruz.savorcarTj.ui.profile.UserViewModel


class PreferenceFragment: Fragment() {

    private val binding by lazy { PrefrenceFragmentBinding.inflate(layoutInflater) }
    private val preference by lazy { PreferenceHelper.defaultPreference(requireContext()) }
    private val viewModel by viewModels<UserViewModel>()
    private val listPreference = mutableListOf<Preference>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getPreference(preference.userId, preference.hashbyId.toString()).observe(viewLifecycleOwner) { it ->
            binding.progressBar3.visibility = View.GONE
            if (it.code == 6 && it.data.isNotEmpty()) {
                binding.content.visibility = View.VISIBLE
                val adapter = PreferenceAdapter(it.data) { handler(it) }
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                listPreference.addAll(it.data)
                binding.recyclerView.adapter = adapter
            }


        }

        binding.backEvent.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.save.setOnClickListener {
            val countries = ArrayList<String>()
            var index = 0
            val params = JSONObject()
            for (i in listPreference) {
                if (i.isChecked) {
                    params.put(index.toString(), i.id.toString())
                    index++
                }
            }


            val body = params.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())



            Log.d("DEVELOPER", params.toString())
            val test = params.toString().replace(":","=").replace("{","").replace("}","")

            viewModel.addPreference(preference.userId, preference.hashbyId.toString(), params.toString()).observe(viewLifecycleOwner, Observer {
Log.d("DEVELOPER",it.toString())

            })


        }


    }

    @SuppressLint("NewApi")
    private fun handler(item: Preference) {


    }

}
