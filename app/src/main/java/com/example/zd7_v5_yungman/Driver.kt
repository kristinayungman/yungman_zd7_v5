package com.example.zd7_v5_yungman

import androidx.room.*

@Entity(
    tableName = "drivers",
    foreignKeys = [
        ForeignKey(
            entity = Bus::class,
            parentColumns = ["id"],
            childColumns = ["bus_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("bus_id")]
)
data class Driver(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    @ColumnInfo(name = "bus_id") val busId: Long,
    @ColumnInfo(name = "salary_bonus") val salaryBonus: Float = 0f
)