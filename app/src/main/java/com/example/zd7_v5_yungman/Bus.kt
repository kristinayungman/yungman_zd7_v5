package com.example.zd7_v5_yungman

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "buses")
data class Bus(
    @PrimaryKey(autoGenerate = true) val id: Long = 0, // ← ДОЛЖНО БЫТЬ autoGenerate = true
    @ColumnInfo(name = "nomenclature_number") val nomenclatureNumber: String,
    @ColumnInfo(name = "condition") val condition: String,
    @ColumnInfo(name = "depreciation_percentage") val depreciationPercentage: Float
)
