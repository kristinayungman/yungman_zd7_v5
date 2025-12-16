package com.example.zd7_v5_yungman

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zd7_v5_yungman.AppDatabase
import com.example.zd7_v5_yungman.Driver
import com.example.zd7_v5_yungman.DriverAdapter
import com.example.zd7_v5_yungman.DriverRouteCrossRef
import com.example.zd7_v5_yungman.DriverWithRoutes
import com.example.zd7_v5_yungman.R
import com.example.zd7_v5_yungman.databinding.FragmentDriversContentBinding
import kotlinx.coroutines.launch

class DriversFragment : Fragment() {

    private var _binding: FragmentDriversContentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: DriverAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDriversContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DriverAdapter(
            clickListener = { driver -> editDriver(driver) },
            longClickListener = { driver ->
                showDeleteDialog(driver.driver)
                true
            }
        )

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        binding.btnAddDriver.setOnClickListener {
            showAddDriverDialog()
        }

        // ✅ Автоматическая загрузка и обновление через LiveData
        val db = AppDatabase.getDatabase(requireContext())
        db.driverDao().getAllDriversWithRoutesLiveData()
            .observe(viewLifecycleOwner) { drivers ->
                adapter.submitList(drivers)
            }
    }

    // === ДОБАВЛЕНИЕ ===
    private fun showAddDriverDialog() {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            val buses = db.driverDao().getAllBuses()
            val routes = db.driverDao().getAllRoutes()

            if (buses.isEmpty()) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Сначала добавьте автобусы!", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }
            if (routes.isEmpty()) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Сначала добавьте маршруты!", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }

            // ✅ Вернуться на главный поток для показа диалога
            withContext(Dispatchers.Main) {
                createDriverDialog(null, buses, routes) { name, busId, routeIds ->
                    lifecycleScope.launch {
                        val driverId = db.driverDao().insert(Driver(name = name, busId = busId))
                        db.driverRouteDao().clearRoutesForDriver(driverId)
                        routeIds.forEach { routeId ->
                            db.driverRouteDao().insertDriverRoute(DriverRouteCrossRef(driverId, routeId))
                        }
                        Toast.makeText(context, "Водитель добавлен", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // === РЕДАКТИРОВАНИЕ ===
    private fun editDriver(driverWithRoutes: DriverWithRoutes) {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            val buses = db.driverDao().getAllBuses()
            val routes = db.driverDao().getAllRoutes()

            withContext(Dispatchers.Main) {
                createDriverDialog(driverWithRoutes, buses, routes) { name, busId, routeIds ->
                    lifecycleScope.launch {
                        val updated = driverWithRoutes.driver.copy(name = name, busId = busId)
                        db.driverDao().update(updated)
                        db.driverRouteDao().clearRoutesForDriver(updated.id)
                        for (routeId in routeIds) {
                            db.driverRouteDao().insertDriverRoute(DriverRouteCrossRef(updated.id, routeId))
                        }
                        Toast.makeText(context, "Водитель обновлён", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // === УНИВЕРСАЛЬНЫЙ ДИАЛОГ ===
    private fun createDriverDialog(
        initialData: DriverWithRoutes?,
        buses: List<Bus>,
        routes: List<Route>,
        onSave: (name: String, busId: Long, routeIds: List<Long>) -> Unit
    ) {
        val context = requireContext()
        val scrollView = NestedScrollView(context)
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 16, 32, 16)
        }

        val etName = EditText(context).apply {
            hint = "Имя водителя"
            setText(initialData?.driver?.name ?: "")
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        layout.addView(etName)

        val busNames = buses.map { "№${it.id} (${it.nomenclatureNumber})" }
        val busSpinner = Spinner(context).apply {
            adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, busNames)
            if (initialData != null) {
                val index = buses.indexOfFirst { it.id == initialData.driver.busId }
                if (index >= 0) setSelection(index)
            }
        }
        layout.addView(TextView(context).apply { text = "Автобус:" })
        layout.addView(busSpinner)

        val routeNames = routes.map { it.name }
        val checked = BooleanArray(routes.size) { i ->
            initialData?.routes?.any { it.id == routes[i].id } ?: false
        }
        val listView = ListView(context).apply {
            adapter = ArrayAdapter(context, android.R.layout.simple_list_item_multiple_choice, routeNames)
            choiceMode = ListView.CHOICE_MODE_MULTIPLE
            for (i in routes.indices) {
                setItemChecked(i, checked[i])
            }
        }
        layout.addView(TextView(context).apply { text = "Маршруты (минимум 1):" })
        layout.addView(listView, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300))

        scrollView.addView(layout)

        AlertDialog.Builder(context)
            .setTitle(if (initialData == null) "Добавить водителя" else "Редактировать водителя")
            .setView(scrollView)
            .setPositiveButton("Сохранить") { _, _ ->
                val name = etName.text.toString().trim()
                if (name.isEmpty()) {
                    Toast.makeText(context, "Введите имя", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val busId = buses[busSpinner.selectedItemPosition].id

                val selectedRouteIds = mutableListOf<Long>()
                for (i in routes.indices) {
                    if (checked[i]) selectedRouteIds.add(routes[i].id)
                }
                if (selectedRouteIds.isEmpty()) {
                    Toast.makeText(context, "Выберите хотя бы один маршрут", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                onSave(name, busId, selectedRouteIds)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    // === УДАЛЕНИЕ ===
    private fun showDeleteDialog(driver: Driver) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Удалить водителя?")
            .setMessage("Все назначенные маршруты будут удалены.")
            .setPositiveButton("Удалить") { _, _ ->
                lifecycleScope.launch {
                    val db = AppDatabase.getDatabase(requireContext())
                    db.driverDao().delete(driver)
                    // ✅ LiveData обновит список автоматически
                    Toast.makeText(context, "Водитель удалён", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(Color.BLACK)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(Color.BLACK)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}