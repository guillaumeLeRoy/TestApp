package gleroy.com.mybaseapplication.data.local.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [TodoDao::class], version = 1)
abstract class MyDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao


}