package com.example.zd7_v5_yungman

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.zd7_v5_yungman.databinding.ItemBusBinding

// Обязательный импорт класса Bus
import com.example.zd7_v5_yungman.Bus

class BusAdapter(
    private val clickListener: (bus: Bus) -> Unit,
    private val longClickListener: (bus: Bus) -> Boolean  // ✅ Возвращает Boolean
) : ListAdapter<Bus, BusAdapter.BusViewHolder>(BusDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusViewHolder {
        val binding = ItemBusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BusViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BusViewHolder, position: Int) {
        val bus = getItem(position)
        holder.bind(bus, clickListener, longClickListener)
    }

    class BusViewHolder(private val binding: ItemBusBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            bus: Bus,
            clickListener: (Bus) -> Unit,
            longClickListener: (Bus) -> Boolean  // ✅ Тот же тип
        ) {
            binding.tvNomenclature.text = "Номер: ${bus.nomenclatureNumber}"
            binding.tvCondition.text = "Состояние: ${bus.condition}"
            binding.tvDepreciation.text = "Амортизация: ${bus.depreciationPercentage}%"

            binding.root.setOnClickListener { clickListener(bus) }
            binding.root.setOnLongClickListener {
                longClickListener(bus)  // ✅ Возвращает Boolean
            }
        }
    }
}

class BusDiffCallback : DiffUtil.ItemCallback<Bus>() {
    override fun areItemsTheSame(oldItem: Bus, newItem: Bus): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Bus, newItem: Bus): Boolean = oldItem == newItem
}