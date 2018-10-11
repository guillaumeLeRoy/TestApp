package gleroy.com.mybaseapplication.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = TodoEntity.TABLE_NAME)
class TodoEntity(@field:PrimaryKey val id: Int,
                 val userId: Int,
                 val title: String,
                 val completed: Boolean) {

    companion object {
        const val TABLE_NAME = "todos"
    }
}