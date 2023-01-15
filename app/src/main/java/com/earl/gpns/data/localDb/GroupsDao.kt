package com.earl.gpns.data.localDb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface GroupsDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertNewCounter(groupCounter: GroupMessagesCountDb)

    @Query("select * from groupMessagesCount where groupId =:group_id")
    suspend fun selectCounterForGroup(group_id: String) : GroupMessagesCountDb?

    @Query("update groupMessagesCount set counter =:newCount where groupId =:group_id")
    suspend fun updateReadMessagesCount(group_id: String, newCount: Int)

    @Query("delete from groupMessagesCount where groupId =:group_id")
    suspend fun deleteMessagesCounterInGroup(group_id: String)
}