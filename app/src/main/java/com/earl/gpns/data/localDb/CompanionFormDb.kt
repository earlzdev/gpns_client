package com.earl.gpns.data.localDb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.earl.gpns.data.mappers.CompanionFormDbToDataMapper
import com.earl.gpns.data.models.CompanionFormData

@Entity(tableName = "companionFormDb")
data class CompanionFormDb(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "userImage") val userImage: String,
    @ColumnInfo(name = "from") val from: String,
    @ColumnInfo(name = "to") val to: String,
    @ColumnInfo(name = "schedule") val schedule: String,
    @ColumnInfo(name = "actualTripTime") val actualTripTime: String,
    @ColumnInfo(name = "ableToPay") val ableToPay: String?,
    @ColumnInfo(name = "comment") val comment: String,
) {
    fun mapToData(mapper: CompanionFormDbToDataMapper<CompanionFormData>) =
        mapper.map(username, userImage, from, to, schedule, actualTripTime, ableToPay, comment, 1)
}
