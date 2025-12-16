package com.example.zd7_v5_yungman


import androidx.room.*

@Entity(
    tableName = "driver_routes",
    primaryKeys = ["driver_id", "route_id"],
    foreignKeys = [
        ForeignKey(
            entity = Driver::class,
            parentColumns = ["id"],
            childColumns = ["driver_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Route::class,
            parentColumns = ["id"],
            childColumns = ["route_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DriverRouteCrossRef(
    @ColumnInfo(name = "driver_id") val driverId: Long,
    @ColumnInfo(name = "route_id") val routeId: Long
)