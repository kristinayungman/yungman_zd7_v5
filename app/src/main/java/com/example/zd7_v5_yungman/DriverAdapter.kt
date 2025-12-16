package com.example.zd7_v5_yungman


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.zd7_v5_yungman.databinding.ItemDriverBinding

class DriverAdapter(
    private val clickListener: (driver: DriverWithRoutes) -> Unit,
    private val longClickListener: (driver: DriverWithRoutes) -> Boolean
) : ListAdapter<DriverWithRoutes, DriverAdapter.ViewHolder>(DriverDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDriverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener, longClickListener)
    }

    class ViewHolder(private val binding: ItemDriverBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            driver: DriverWithRoutes,
            clickListener: (DriverWithRoutes) -> Unit,
            longClickListener: (DriverWithRoutes) -> Boolean
        ) {
            // Отображаем премию только если маршрутов >= 2
            val bonusText = if (driver.hasBonus) {
                "Премия: 5000 ₽"
            } else {
                "Без премии"
            }
            binding.tvBonus.text = bonusText

            binding.tvDriverName.text = driver.driver.name
            binding.tvBus.text = "Автобус: №${driver.driver.busId}"
            val routeNames = driver.routes.joinToString { it.name }
            binding.tvRoutes.text = "Маршруты: $routeNames"

            binding.root.setOnClickListener { clickListener(driver) }
            binding.root.setOnLongClickListener {
                longClickListener(driver)
                true
            }
        }
    }
}

class DriverDiffCallback : DiffUtil.ItemCallback<DriverWithRoutes>() {
    override fun areItemsTheSame(oldItem: DriverWithRoutes, newItem: DriverWithRoutes): Boolean =
        oldItem.driver.id == newItem.driver.id

    override fun areContentsTheSame(oldItem: DriverWithRoutes, newItem: DriverWithRoutes): Boolean =
        oldItem == newItem
}