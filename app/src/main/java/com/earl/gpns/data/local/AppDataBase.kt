package com.earl.gpns.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoomDb::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun roomsDao() : RoomsDao
}