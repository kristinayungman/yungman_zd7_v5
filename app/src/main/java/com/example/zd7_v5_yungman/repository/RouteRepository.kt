package com.example.zd7_v5_yungman.repository


import com.example.zd7_v5_yungman.Route
import com.example.zd7_v5_yungman.dao.RouteDao
import kotlinx.coroutines.flow.Flow

class RouteRepository(private val routeDao: RouteDao) {

    val allRoutes: Flow<List<Route>> = routeDao.getAllRoutesFlow()

    suspend fun insert(route: Route): Long = routeDao.insert(route)

    suspend fun update(route: Route) = routeDao.update(route)

    suspend fun delete(route: Route) = routeDao.delete(route)
}