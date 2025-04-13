package com.task.assetratesapp.domain.cache.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assets")
data class AssetEntity(
    @PrimaryKey val code: String
)