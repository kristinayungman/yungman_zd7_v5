package com.example.zd7_v5_yungman

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class Dispetcher : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dispetcher, container, false)

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Диспетчер"

        // Кнопка "Назад" — возвращает к предыдущему фрагменту
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)

        viewPager.adapter = DispetcherPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Автобусы"
                1 -> "Маршруты"
                2 -> "Водители"
                else -> "Tab $position"
            }
        }.attach()

        return view
    }

    // Адаптер для ViewPager2
    class DispetcherPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            val fragment = when (position) {
                0 -> BusesFragment()
                1 -> RoutesFragment()
                2 -> DriversFragment()
                else -> throw IllegalStateException("Unexpected position: $position")
            }
            Log.d("Dispetcher", "Created fragment for position $position: ${fragment.javaClass.simpleName}")
            return fragment
        }
    }
}