package tj.behruz.savorcarTj.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tj.behruz.savorcarTj.databinding.HistoryPassangerItemBinding
import tj.behruz.savorcarTj.models.Booking

class HistoryPassengerAdapter(val context: Context, private var bookings: List<Booking>, val handler: (Booking) -> Unit): RecyclerView.Adapter<HistoryPassengerAdapter.HistoryPassengerViewHolder>() {

    override fun getItemCount() = bookings.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryPassengerViewHolder {
        val binding = HistoryPassangerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryPassengerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryPassengerViewHolder, position: Int) {
        val item = bookings[position]
        holder.setBooking(item)
    }

    fun dataChanged(newList: List<Booking>) {
        bookings = newList
        notifyDataSetChanged()
    }


    inner class HistoryPassengerViewHolder(private val binding: HistoryPassangerItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun setBooking(booking: Booking) {
            binding.username.text = booking.name
            Glide.with(context).load(booking.pimg).into(binding.profile)
            binding.root.setOnClickListener {
                handler.invoke(booking)
            }
            binding.executePendingBindings()
        }

    }

    

}