package com.earl.gpns.data.localDb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.earl.gpns.data.mappers.GroupMessagesCounterDbToDataMapper

@Entity(tableName = "groupMessagesCount")
data class GroupMessagesCountDb(
    @PrimaryKey(autoGenerate = true)val id: Int,
    @ColumnInfo(name = "groupId") val groupId: String,
    @ColumnInfo(name = "counter") val counter: Int
) {
    fun <T> map(mapper: GroupMessagesCounterDbToDataMapper<T>) = mapper.map(groupId, counter)
}
