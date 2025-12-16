package com.example.zd7_v5_yungman

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zd7_v5_yungman.databinding.FragmentBusesListBinding
import kotlinx.coroutines.launch

class BusesByConditionFragment : Fragment() {

    private var _binding: FragmentBusesListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: BusAdapter
    private var condition: String = ""

    companion object {
        fun newInstance(condition: String): BusesByConditionFragment {
            val fragment = BusesByConditionFragment()
            fragment.condition = condition
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBusesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BusAdapter(
            clickListener = { bus ->
                // Пока пусто или оставьте комментарий
            },
            longClickListener = { bus ->
                // Пока пусто или оставьте комментарий
                true  // ✅ Обязательно!
            }
        )
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            val buses = db.busDao().getBusesByCondition(condition)
            adapter.submitList(buses)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}