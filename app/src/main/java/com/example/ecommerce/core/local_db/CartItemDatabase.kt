package com.example.ecommerce.core.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ecommerce.core.models.CartItem

@Database(
    entities = [CartItem::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(CartItemConverters::class)
abstract class CartItemDatabase: RoomDatabase() {
    abstract fun getProductDao():CartItemsDAO

    companion object{
        @Volatile
        private var instance:CartItemDatabase? = null
        private var LOCK = Any()

        operator fun invoke(context: Context)= instance?: synchronized(LOCK){
            instance?:createDatabase(context).also{ instance=it}
        }

        private fun createDatabase(context:Context)=
            Room.databaseBuilder(
                context.applicationContext,
                CartItemDatabase::class.java,
                "cart_items_db.db"
            ).build()
    }
}