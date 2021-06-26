package tj.behruz.savorcarTj.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tj.behruz.savorcarTj.databinding.PassengerHistoryItemBinding
import tj.behruz.savorcarTj.helpers.Utils
import tj.behruz.savorcarTj.models.Passenger


class PassengerHistoryAdapter(private val context: Context, private val history: List<Passenger>, val handler: (Passenger) -> Unit): RecyclerView.Adapter<PassengerHistoryAdapter.PassengerHistoryViewHolder>() {

    inner class PassengerHistoryViewHolder(private val binding: PassengerHistoryItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun setTravel(passengerHistory: Passenger) {
            binding.driverName.text = passengerHistory.name
            if (binding.driverName.text.toString().isEmpty()) {
                binding.driverName.visibility = View.GONE
            }

            if (passengerHistory.placefrom==null) {
                binding.fromCityDriver.text = passengerHistory.cityfrom
            } else {
                binding.fromCityDriver.text = passengerHistory.cityfrom.plus(", ").plus(passengerHistory.placefrom)
            }

            if (passengerHistory.placeto!=null) {
                binding.toCityDriver.text = passengerHistory.cityto.plus(", ").plus(passengerHistory.placeto)
            } else {
                binding.toCityDriver.text = passengerHistory.cityto
            }


            binding.historyDriverPrice.text = passengerHistory.cost.plus(" c")
            val dates = passengerHistory.datetime.split(" ")
            val days = dates[0].split("-")
            binding.historyDriverDate.text = days[2].plus(" ${Utils.getMonthByNumber(days[1])}").plus(" в ").plus(dates[1].removeRange(dates[1].length - 3, dates[1].length))


            binding.root.setOnClickListener {
                handler.invoke(passengerHistory)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassengerHistoryViewHolder {
        return PassengerHistoryViewHolder(PassengerHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PassengerHistoryViewHolder, position: Int) {
        val item = history[position]
        holder.setTravel(item)
    }

    override fun getItemCount(): Int {
        return history.size
    }
}