package com.example.zd7_v5_yungman


import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object GeoapifyService {
    private const val API_KEY = "9e23cf516fbc451b9349788977bad860"

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun getStaticMapUrl(
        waypoints: List<Pair<Double, Double>>,
        mode: String = "drive"
    ): String? {
        if (waypoints.size < 2) return null

        val markers = waypoints.joinToString("|") { "lonlat:${it.second},${it.first}" }
        return "https://maps.geoapify.com/v1/staticmap?style=osm-bright&width=600&height=400&markers=$markers&apiKey=$API_KEY"
    }
}