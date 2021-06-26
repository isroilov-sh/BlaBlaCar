package tj.behruz.savorcarTj.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tj.behruz.savorcarTj.R
import tj.behruz.savorcarTj.databinding.PassengerItemBinding



class PassengerAdapter(val context: Context, private var allCount: Int, private val booked: Int): RecyclerView.Adapter<PassengerAdapter.PassengerViewHolder>() {

    private var localBooked = booked
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassengerViewHolder {
        val binding = PassengerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PassengerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PassengerViewHolder, position: Int) {

        holder.setItem(allCount)
    }

    override fun getItemCount() = allCount


    inner class PassengerViewHolder(private val binding: PassengerItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun setItem(position: Int) {
            if (localBooked == allCount) {
                binding.passengerIcon.setImageResource(R.drawable.ic_user_fill)
                localBooked = localBooked.minus(1)
            }

            if (localBooked in 1 until allCount) {
                binding.passengerIcon.setImageResource(R.drawable.ic_user_fill)
                localBooked = localBooked.minus(1)
            }
            binding.executePendingBindings()
        }
    }

}