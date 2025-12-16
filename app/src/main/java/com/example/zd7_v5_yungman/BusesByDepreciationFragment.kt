package com.example.zd7_v5_yungman


import android.R
import android.widget.ArrayAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zd7_v5_yungman.databinding.FragmentBusesListBinding
import com.example.zd7_v5_yungman.databinding.DialogEditBusBinding
import kotlinx.coroutines.launch
import android.graphics.Color

class BusesByDepreciationFragment : Fragment() {

    private var _binding: FragmentBusesListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: BusAdapter
    private var minDepreciation: Float = 0f
    private var maxDepreciation: Float = 100f

    companion object {
        fun newInstance(min: Float, max: Float): BusesByDepreciationFragment {
            val fragment = BusesByDepreciationFragment()
            fragment.minDepreciation = min
            fragment.maxDepreciation = max
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
            clickListener = { bus -> editBus(bus) }
        ) { bus ->
            showDeleteDialog(bus)
            true
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            db.busDao()
                .getBusesByDepreciationRangeFlow(minDepreciation, maxDepreciation)
                .collect { buses ->
                    adapter.submitList(buses)
                }
        }
    }

    private fun editBus(bus: Bus) {
        val dialogView = DialogEditBusBinding.inflate(layoutInflater)
        dialogView.etNomenclature.setText(bus.nomenclatureNumber)
        dialogView.etDepreciation.setText(bus.depreciationPercentage.toString())

        val conditions = listOf("Новое", "Хорошее", "Отредактировано", "Требует ремонта", "Неисправлено")

        // ✅ Используем системные layout-ы
        val conditionAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item, // ✅ android.R, не R
            conditions
        )
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
          dialogView.spCondition.adapter = conditionAdapter

        val currentIndex = conditions.indexOf(bus.condition)
        if (currentIndex >= 0) {
            dialogView.spCondition.setSelection(currentIndex)
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Редактировать автобус №${bus.id}")
            .setView(dialogView.root)
            .setPositiveButton("Сохранить") { _, _ ->
                val nomenclature = dialogView.etNomenclature.text.toString()
                val depreciation = dialogView.etDepreciation.text.toString().toFloatOrNull() ?: 0f
                val condition = dialogView.spCondition.selectedItem.toString()

                val updatedBus = bus.copy(
                    nomenclatureNumber = nomenclature,
                    depreciationPercentage = depreciation,
                    condition = condition
                )

                lifecycleScope.launch {
                    val db = AppDatabase.getDatabase(requireContext())
                    db.busDao().update(updatedBus)
                    Toast.makeText(context, "Автобус обновлён", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showDeleteDialog(bus: Bus) {
        val builder = AlertDialog.Builder(requireContext(), R.style.Theme_Dialog)

        builder.setTitle("Удалить автобус?")
        builder.setMessage("Автобус №${bus.id} будет удалён. Это действие нельзя отменить.")
        builder.setPositiveButton("Удалить") { _, _ ->
            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(requireContext())
                db.busDao().delete(bus)
                Toast.makeText(context, "Автобус удалён", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Отмена", null)

        val dialog = builder.create()
        dialog.show()

        // Меняем цвет текста кнопок
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(Color.BLACK)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(Color.BLACK)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}