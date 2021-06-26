package tj.behruz.savorcarTj.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tj.behruz.savorcarTj.databinding.ReasonItemBinding

class ReasonAdapter(private var reasons: List<String>, private val handle: (String) -> Unit): RecyclerView.Adapter<ReasonAdapter.ReasonViewHolder>() {

    inner class ReasonViewHolder(private val binding: ReasonItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun setReason(reason: String) {
            binding.reasonTitle.text = reason
            binding.root.setOnClickListener {
                handle.invoke(reason)
            }
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReasonViewHolder {
        val binding = ReasonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReasonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReasonViewHolder, position: Int) {
        holder.setReason(reasons[position])
    }

    override fun getItemCount()=reasons.size

}