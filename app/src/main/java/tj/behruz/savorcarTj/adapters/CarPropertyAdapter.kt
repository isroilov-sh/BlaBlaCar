package tj.behruz.savorcarTj.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tj.behruz.savorcarTj.databinding.CarPropertyItemBinding
import tj.behruz.savorcarTj.models.CarProperty


class CarPropertyAdapter(private val properties:List<CarProperty>, val itemHandler:(CarProperty)->Unit):RecyclerView.Adapter<CarPropertyAdapter.CarPropertyViewHolder>() {

    override fun getItemCount()=properties.size


    override fun onBindViewHolder(holder: CarPropertyViewHolder, position: Int) {

        val property=properties[position]
        holder.setProperty(property)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarPropertyViewHolder {
        val binding= CarPropertyItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CarPropertyViewHolder(binding)
    }



    inner class CarPropertyViewHolder(private val binding:CarPropertyItemBinding):RecyclerView.ViewHolder(binding.root){

        fun setProperty(carProperty: CarProperty){
            binding.propertyName.text=carProperty.name
            binding.root.setOnClickListener {
                itemHandler.invoke(carProperty)
            }
            binding.executePendingBindings()
        }

    }

}