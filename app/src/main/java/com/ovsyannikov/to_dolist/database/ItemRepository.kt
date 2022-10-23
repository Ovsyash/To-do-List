package com.ovsyannikov.to_dolist.database

import android.content.Context
import androidx.lifecycle.LiveData
import com.ovsyannikov.to_dolist.item.Item
import kotlinx.coroutines.CoroutineScope

class ItemRepository(context: Context, scope: CoroutineScope) {
    private val itemDao: ItemDao
    val allItems: LiveData<List<Item>>

    init {
        val dataBase: ItemDataBase = ItemDataBase.getDataBase(context, scope)
        itemDao = dataBase.itemDao()
        allItems = itemDao.allItems()
    }

    fun insert(item: Item) {
        itemDao.insert(item)
    }

    fun update(item: Item) {
        itemDao.update(item)
    }

    fun delete(item: Item) {
        itemDao.delete(item)
    }
}