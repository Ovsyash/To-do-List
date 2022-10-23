package com.ovsyannikov.to_dolist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ovsyannikov.to_dolist.item.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Item::class], version = 1)
abstract class ItemDataBase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var INSTANCE: ItemDataBase? = null

        fun getDataBase(
            context: Context,
            scope: CoroutineScope
        ): ItemDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemDataBase::class.java,
                    "item_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(ItemDatabaseCallback(scope))
                    .build()

                INSTANCE = instance
                instance
            }
        }

        fun populateDatabase(itemDao: ItemDao) {
            itemDao.insert(Item("Title 1", "Description 1", true))
            itemDao.insert(Item("Title 2", "Description 2", false))
            itemDao.insert(Item("Title 3", "Description 3", false))
        }

        private class ItemDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.itemDao())
                    }
                }
            }
        }
    }
}