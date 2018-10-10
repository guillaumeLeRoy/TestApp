package gleroy.com.mybaseapplication.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import gleroy.com.mybaseapplication.R
import gleroy.com.mybaseapplication.data.remote.entity.Todo
import gleroy.com.mybaseapplication.presentation.adapter.base.ARecyclerAdapter
import gleroy.com.mybaseapplication.presentation.adapter.viewholder.TodoVH

class TodosRecyclerViewAdapter : ARecyclerAdapter<Todo, TodoVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoVH {
        return TodoVH(LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false))
    }

    override fun onBindViewHolder(holder: TodoVH, position: Int) {
        holder.bind(data[position])
    }

}