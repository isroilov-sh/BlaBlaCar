package tj.behruz.savorcarTj.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tj.behruz.savorcarTj.databinding.PrefrenceItemBinding
import tj.behruz.savorcarTj.models.Preference

class PreferenceAdapter(private val preference: List<Preference>, val preferenceHandler: (Preference) -> Unit): RecyclerView.Adapter<PreferenceAdapter.PreferenceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreferenceViewHolder {
        val binding = PrefrenceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PreferenceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder, position: Int) {
        holder.setPreference(preference[position])
    }

    override fun getItemCount() = preference.size


    inner class PreferenceViewHolder(private val binding: PrefrenceItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun setPreference(item: Preference) {
            binding.checkBox2.text = item.text
            binding.checkBox2.setOnCheckedChangeListener { buttonView, isChecked ->
                item.isChecked = isChecked
                preferenceHandler.invoke(item)
            }
        }
    }


}