package com.farukyildiz.travelbook.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity
class Place(
    @ColumnInfo(name="name")
    var name: String,
    @ColumnInfo(name="latitude")
    var latitude:Double,
    @ColumnInfo(name="longtitude")
    var longtitude:Double
    ) :Serializable{
    @PrimaryKey(autoGenerate = true)
    var id=0

}