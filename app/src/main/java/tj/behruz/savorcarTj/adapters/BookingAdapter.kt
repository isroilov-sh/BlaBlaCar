package tj.behruz.savorcarTj.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tj.behruz.savorcarTj.R
import tj.behruz.savorcarTj.databinding.BookingItemBinding
import tj.behruz.savorcarTj.helpers.PreferenceHelper
import tj.behruz.savorcarTj.helpers.PreferenceHelper.username
import tj.behruz.savorcarTj.models.Booking
import tj.behruz.savorcarTj.models.BookingAction
import tj.behruz.savorcarTj.models.enum.Action
import tj.behruz.savorcarTj.models.enum.TripDetailsAction

class BookingAdapter(val context: Context, var type:String,private var bookings: List<Booking>, val handler: (BookingAction) -> Unit): RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    private val sharedPreferences = PreferenceHelper.defaultPreference(context)

    override fun getItemCount() = bookings.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val binding = BookingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val item = bookings[position]
        holder.setBooking(item)
    }

    fun dataChanged(newList: List<Booking>) {
        bookings = newList
        notifyDataSetChanged()
    }

    inner class BookingViewHolder(private val binding: BookingItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun setBooking(booking: Booking) {
            binding.name.text = booking.name
Log.d("DEVELOPER", booking.name)
            if (booking.name == sharedPreferences.username) {
                binding.cancel.visibility = View.GONE
                binding.confirm.visibility = View.GONE
            }

            when (type) {
                TripDetailsAction.DRIVER.name -> {
                    binding.cancel.visibility = View.VISIBLE
                    binding.confirm.visibility = View.VISIBLE
                }
                else->{
                    binding.cancel.visibility = View.GONE
                    binding.confirm.visibility = View.GONE
                }


            }


            binding.cancel.setOnClickListener {
                handler.invoke(BookingAction(Action.DELETE.name, booking))
            }

            if (booking.status=="1"){
                binding.confirm.setImageResource(R.drawable.ic_check)
            }

            Glide.with(context).load(booking.pimg).into(binding.userImage)
            binding.confirm.setOnClickListener {
                handler.invoke(BookingAction(Action.CONFIRM.name, booking))
            }

            binding.name.setOnClickListener {
                handler.invoke(BookingAction(Action.INFO.name, booking))
            }

            binding.executePendingBindings()
        }

    }

}