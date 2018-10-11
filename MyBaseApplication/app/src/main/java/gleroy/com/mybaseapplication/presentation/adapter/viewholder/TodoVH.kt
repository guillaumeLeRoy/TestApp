package gleroy.com.mybaseapplication.presentation.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import gleroy.com.mybaseapplication.data.local.entity.TodoEntity
import kotlinx.android.synthetic.main.item_todo.view.*
import timber.log.Timber

class TodoVH(view: View) : RecyclerView.ViewHolder(view) {

    init {
        itemView.setOnClickListener { Timber.d("clicked") }
    }

    fun bind(todo: TodoEntity) {
        itemView.ui_textview.text = todo.title
    }

}
