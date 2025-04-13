package com.task.assetratesapp.data.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.task.assetratesapp.domain.cache.entities.AssetEntity

@Dao
interface AssetDao {
    @Query("SELECT * FROM assets")
    suspend fun getAssets(): List<AssetEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAsset(assetEntity: AssetEntity)

    @Delete
    suspend fun deleteAsset(assetEntity: AssetEntity)
}