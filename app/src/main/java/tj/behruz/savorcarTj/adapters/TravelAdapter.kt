package tj.behruz.savorcarTj.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tj.behruz.savorcarTj.databinding.TravelItemBinding
import tj.behruz.savorcarTj.helpers.Utils
import tj.behruz.savorcarTj.models.TripPayload

class TravelAdapter(val context: Context, private val travels: List<TripPayload>, val handler:(TripPayload)->Unit): RecyclerView.Adapter<TravelAdapter.TravelViewHolder>() {

    override fun getItemCount(): Int {
        return travels.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelViewHolder {
        val binding = TravelItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TravelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TravelViewHolder, position: Int) {
        val trip = travels[position]
        holder.setTravel(trip)
        holder.itemView.setOnClickListener {
            handler.invoke(trip)
        }
    }


    inner class TravelViewHolder(private val binding: TravelItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun setTravel(tripPayload: TripPayload) {
            binding.tripFrom.text = tripPayload.cityfrom.plus(",  ").plus(tripPayload.placefrom)
            binding.tripCity.text = tripPayload.cityto.plus(",  ").plus(tripPayload.placeto)
            if (!tripPayload.placefrom.isNullOrEmpty()){
                binding.tripFrom.text = tripPayload.cityfrom.plus(", ").plus(tripPayload.placefrom)
            }
            if (!tripPayload.placeto.isNullOrEmpty()){
                binding.tripCity.text = tripPayload.cityto.plus(", ").plus(tripPayload.placeto)
            }

            binding.tripPrice.text=tripPayload.cost.plus(" ").plus("c")
            binding.userName.text=tripPayload.name
            binding.userRating.text=tripPayload.rate.toString().plus("/5")
            val dates = tripPayload.datetime.split(" ")
            val days = dates[0].split("-")
            Glide.with(context).load(tripPayload.pimg).into(binding.profileImage)
            binding.date.text = days[2].plus(" ${Utils.getMonthByNumber(days[1])}").plus(" в ").plus(dates[1].removeRange(dates[1].length - 3, dates[1].length))
            val passengerAdapter = PassengerAdapter(context, tripPayload.number.toInt(), tripPayload.booked)
            binding.recyclerView.adapter = passengerAdapter
            binding.executePendingBindings()
        }
    }
}