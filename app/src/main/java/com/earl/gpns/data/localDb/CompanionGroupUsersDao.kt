package com.earl.gpns.data.localDb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CompanionGroupUsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewUserIntoCompanionGroup(user: CompanionGroupUser)

    @Query("select * from companionGroupUser")
    suspend fun fetchAllCompanionUsers() : List<CompanionGroupUser>

    @Query("delete from companionGroupUser where name =:username")
    suspend fun removeUserFromCompanionGroupInLocalDb(username: String)

    @Query("delete from companionGroupUser")
    suspend fun clearLocalDbCompanionGroupUsersList()
}