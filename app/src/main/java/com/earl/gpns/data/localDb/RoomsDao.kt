package com.earl.gpns.data.localDb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RoomsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRoom(room: RoomDb)

    @Query("select * from roomDB")
    suspend fun fetchAllRooms() : List<RoomDb>

    @Query("delete from roomDB where roomId =:room_id")
    suspend fun deleteRoomFromDb(room_id: String)

    @Query("delete from roomDB")
    suspend fun clearDatabase()
}