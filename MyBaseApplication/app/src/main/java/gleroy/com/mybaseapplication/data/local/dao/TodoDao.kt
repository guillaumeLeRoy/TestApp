package gleroy.com.mybaseapplication.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import gleroy.com.mybaseapplication.data.local.entity.TodoEntity
import io.reactivex.Single

@Dao
interface TodoDao {

    @Query("SELECT count() FROM " + TodoEntity.TABLE_NAME)
    fun countAll(): Single<Int>

    @Query("SELECT * FROM " + TodoEntity.TABLE_NAME)
    fun getAll(): Single<List<TodoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(entities: List<TodoEntity>)
}