package com.example.zd7_v5_yungman


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.zd7_v5_yungman.databinding.FragmentBusesTabsBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class BusesFragment : Fragment() {

    private var _binding: FragmentBusesTabsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBusesTabsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Используем предопределённые диапазоны
        val groups = DepreciationGroup.all

        binding.btnAddBus.setOnClickListener {
            addNewBus()
        }

        binding.viewPager.adapter = DepreciationPagerAdapter(this, groups)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = groups[position].label
        }.attach()
    }

    private fun addNewBus() {
        val newBus = Bus(
            nomenclatureNumber = "BUS-${System.currentTimeMillis() % 1000}",
            condition = "Новое",
            depreciationPercentage = 0f
        )

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            db.busDao().insert(newBus)
            Toast.makeText(context, "Автобус добавлен", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // ✅ ВЛОЖЕННЫЙ КЛАСС — внутри BusesFragment
    sealed class DepreciationGroup(val label: String, val min: Float, val max: Float) {
        object Low : DepreciationGroup("0–20%", 0f, 20f)
        object Medium : DepreciationGroup("20–50%", 20f, 50f)
        object High : DepreciationGroup("50–100%", 50f, 100f)

        companion object {
            val all = listOf(Low, Medium, High)
        }
    }

    class DepreciationPagerAdapter(
        fragment: Fragment,
        private val groups: List<DepreciationGroup>
    ) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = groups.size

        override fun createFragment(position: Int): Fragment {
            val group = groups[position]
            return BusesByDepreciationFragment.newInstance(group.min, group.max)
        }
    }
}