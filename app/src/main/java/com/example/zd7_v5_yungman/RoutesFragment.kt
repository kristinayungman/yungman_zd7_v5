package com.example.zd7_v5_yungman

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zd7_v5_yungman.AppDatabase
import com.example.zd7_v5_yungman.GeoapifyService
import com.example.zd7_v5_yungman.Route
import com.example.zd7_v5_yungman.RouteAdapter
import com.example.zd7_v5_yungman.databinding.FragmentRoutesContentBinding
import com.example.zd7_v5_yungman.factory.RouteViewModelFactory
import com.example.zd7_v5_yungman.repository.RouteRepository
import com.example.zd7_v5_yungman.viewmodel.RouteViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class RoutesFragment : Fragment() {

    private var _binding: FragmentRoutesContentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RouteViewModel
    private lateinit var adapter: RouteAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRoutesContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RouteAdapter(
            clickListener = { route ->
                if (route.mapImageUrl.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(route.mapImageUrl))
                    startActivity(intent)
                }
            },
            longClickListener = { route ->
                deleteRoute(route)
                true
            }
        )
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        val db = AppDatabase.getDatabase(requireContext())
        val repository = RouteRepository(db.routeDao())
        val factory = RouteViewModelFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, factory)[RouteViewModel::class.java]

        viewModel.allRoutesLive.observe(viewLifecycleOwner) { routes ->
            adapter.submitList(routes)
        }

        binding.btnAdd.setOnClickListener {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_route, null)
            val etName = dialogView.findViewById<TextInputEditText>(R.id.etName)
            val etStart = dialogView.findViewById<TextInputEditText>(R.id.etStart)
            val etEnd = dialogView.findViewById<TextInputEditText>(R.id.etEnd)
            val btnAdd = dialogView.findViewById<Button>(R.id.btnAddRoute)

            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create()

            btnAdd.setOnClickListener {
                lifecycleScope.launch {
                    val name = etName?.text.toString().trim()
                    val startText = etStart?.text.toString().trim()
                    val endText = etEnd?.text.toString().trim()

                    if (name.isEmpty() || startText.isEmpty() || endText.isEmpty()) {
                        Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    val start = parseCoordinates(startText)
                    val end = parseCoordinates(endText)

                    if (start == null || end == null) {
                        Toast.makeText(context, "Неверный формат координат", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    val imageUrl = GeoapifyService.getStaticMapUrl(listOf(start, end), "drive")
                    Log.d("RoutesFragment", "Полученный URL карты: $imageUrl") // ← ДОБАВЬТЕ ЭТУ СТРОКУ
                    val route = Route(
                        name = name,
                        startLat = start.first,
                        startLon = start.second,
                        endLat = end.first,
                        endLon = end.second,
                        mapImageUrl = imageUrl ?: "https://via.placeholder.com/600x400?text=No+Map"
                    )
                    viewModel.insert(route)
                    dialog.dismiss()
                    Toast.makeText(context, "Маршрут добавлен", Toast.LENGTH_SHORT).show()
                }
            }

            dialog.show()
        }
    }



    private fun editRoute(route: Route) {
        val updated = route.copy(name = "${route.name} (изменён)")
        viewModel.update(updated)
        Toast.makeText(context, "Обновлён маршрут №${route.id}", Toast.LENGTH_SHORT).show()
    }

    private fun deleteRoute(route: Route) {
        viewModel.delete(route)
        Toast.makeText(context, "Удалён маршрут №${route.id}", Toast.LENGTH_SHORT).show()
    }

    private fun parseCoordinates(input: String): Pair<Double, Double>? {
        return try {
            val parts = input.split(",").map { it.trim() }
            if (parts.size != 2) return null
            val lat = parts[0].toDouble()
            val lon = parts[1].toDouble()
            if (lat !in -90.0..90.0 || lon !in -180.0..180.0) return null
            lat to lon
        } catch (e: Exception) {
            null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}