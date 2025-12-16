package com.example.zd7_v5_yungman

import coil.load
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.zd7_v5_yungman.databinding.ItemRouteBinding

class RouteAdapter(
    private val clickListener: (Route) -> Unit,
    private val longClickListener: (Route) -> Boolean
) : ListAdapter<Route, RouteAdapter.RouteViewHolder>(RouteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        val binding = ItemRouteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RouteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        val route = getItem(position)
        holder.bind(route, clickListener, longClickListener)
    }

    class RouteViewHolder(private val binding: ItemRouteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(route: Route, clickListener: (Route) -> Unit, longClickListener: (Route) -> Boolean) {
            binding.tvRouteName.text = route.name
            binding.tvMapUrl.text = route.mapImageUrl

            // Загружаем картинку через Coil
            binding.ivMap.load(route.mapImageUrl) {
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_background)
            }

            binding.root.setOnClickListener { clickListener(route) }
            binding.root.setOnLongClickListener {
                longClickListener(route)
                true
            }
        }
    }
}

class RouteDiffCallback : DiffUtil.ItemCallback<Route>() {
    override fun areItemsTheSame(oldItem: Route, newItem: Route): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Route, newItem: Route): Boolean = oldItem == newItem
}