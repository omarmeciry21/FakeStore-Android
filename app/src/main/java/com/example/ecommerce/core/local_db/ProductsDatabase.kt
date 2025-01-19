package com.example.ecommerce.core.local_db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ecommerce.core.models.products_response.Product
import android.content.Context

@Database(
    entities = [Product::class],
    version = 1,
    exportSchema = false
)
abstract class ProductsDatabase: RoomDatabase() {
    abstract fun getProductDao():ProductsDAO

    companion object{
        @Volatile
        private var instance:ProductsDatabase? = null
        private var LOCK = Any()

        operator fun invoke(context: Context)= instance?: synchronized(LOCK){
            instance?:createDatabase(context).also{ instance=it}
        }

        private fun createDatabase(context:Context)=
            Room.databaseBuilder(
                context.applicationContext,
                ProductsDatabase::class.java,
                "products_db.db"
            ).build()
    }
}