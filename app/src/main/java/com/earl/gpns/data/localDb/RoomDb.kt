package com.earl.gpns.data.localDb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.earl.gpns.data.mappers.RoomDbToDataMapper

@Entity(tableName = "roomDB")
data class RoomDb(
    @PrimaryKey(autoGenerate = true) val databaseId: Int,
    @ColumnInfo(name = "roomId") val roomId: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "lastMessage") val lastMessage: String,
    @ColumnInfo(name = "lastMessageAuthor") val lastMessageAuthor: String,
    @ColumnInfo(name = "deletable") val deletable: Boolean,
    @ColumnInfo(name = "unreadMsgCounter") val unreadMsgCounter: Int,
    @ColumnInfo(name = "lastMsgRead") val lastMsgRead: Int,
    @ColumnInfo(name = "contactIsOnline") val contactIsOnline: Int,
    @ColumnInfo(name = "contactLastAuth") val contactLastAuth: String,
    @ColumnInfo(name = "lastMsgTimestamp") val lastMsgTimestamp: String
) {
    fun <T> map(mapper: RoomDbToDataMapper<T>) =
        mapper.map(roomId, image, title, lastMessage, lastMessageAuthor, deletable, unreadMsgCounter, lastMsgRead, contactIsOnline, contactLastAuth, lastMsgTimestamp)
}