package ee.ut.cs.shoppinglist.data.local

import android.content.Context
import androidx.room.Room

object AppDatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase =
        INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context.applicationContext).also { INSTANCE = it }
        }

    private fun buildDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "shopping.db")
            .fallbackToDestructiveMigration(false)
            .build()

    fun close() {
        INSTANCE?.close()
        INSTANCE = null
    }
}