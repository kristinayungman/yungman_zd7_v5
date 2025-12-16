package com.example.zd7_v5_yungman

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class People : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_people, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val routes = listOf(
            "Маршрут 1: Центр-Вокзал",
            "Маршрут 2: Аэропорт-Отель",
            "Маршрут 3: Больница-Рынок"
        )

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val adapter = object : RecyclerView.Adapter<RouteViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
                val textView = TextView(parent.context).apply {
                    setPadding(32, 24, 32, 24)
                    textSize = 16f
                    setTextColor(android.graphics.Color.BLACK)
                }
                return RouteViewHolder(textView)
            }

            override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
                holder.bind(routes[position])
            }

            override fun getItemCount() = routes.size
        }

        recyclerView.adapter = adapter
    }

    class RouteViewHolder(private val textView: TextView) : RecyclerView.ViewHolder(textView) {
        fun bind(routeName: String) {
            textView.text = routeName
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = People()
    }
}