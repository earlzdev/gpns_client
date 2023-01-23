package com.earl.gpns.data.localDb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TripFormDao {

    @Query("delete from companionFormDb")
    suspend fun clearCompanionFormDb()

    @Query("delete from driverFormDb")
    suspend fun clearDriverFormDb()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewDriverForm(driverFormDb: DriverFormDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewCompanionForm(companionFormDb: CompanionFormDb)

    @Query("select * from driverFormDb")
    suspend fun fetchDriverFormFromLocalDb() : DriverFormDb

    @Query("select * from companionFormDb")
    suspend fun fetchCompanionFormFromLocalDb() : CompanionFormDb
}