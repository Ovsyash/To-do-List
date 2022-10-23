package com.ovsyannikov.to_dolist.item

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_table")
class Item(
    val title: String,
    val description: String,
    val priority: Boolean
) : java.io.Serializable {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}