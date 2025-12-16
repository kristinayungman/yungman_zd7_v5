package com.example.zd7_v5_yungman


import androidx.room.Embedded
import androidx.room.Relation
import androidx.room.Junction

data class DriverWithRoutes(
    @Embedded val driver: Driver,
    @Relation(
        parentColumn = "id",          // колонка в Driver
        entityColumn = "id",          // колонка в Route
        associateBy = Junction(
            value = DriverRouteCrossRef::class,
            parentColumn = "driver_id",   // → в cross-ref
            entityColumn = "route_id"     // → в cross-ref
        )
    )
    val routes: List<Route>
) {
    val hasBonus: Boolean get() = routes.size >= 2
    val bonusAmount: Float get() = if (hasBonus) 5000f else 0f
}