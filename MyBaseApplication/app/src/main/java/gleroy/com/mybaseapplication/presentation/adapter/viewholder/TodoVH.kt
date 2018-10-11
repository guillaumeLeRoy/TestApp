package gleroy.com.mybaseapplication.presentation.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import gleroy.com.mybaseapplication.data.local.entity.TodoEntity
import kotlinx.android.synthetic.main.item_todo.view.*

class TodoVH(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(todo: TodoEntity) {
        itemView.ui_textview.text = todo.title
    }

}
