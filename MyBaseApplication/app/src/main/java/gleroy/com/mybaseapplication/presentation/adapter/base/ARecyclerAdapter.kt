package gleroy.com.mybaseapplication.presentation.adapter.base

import android.support.v7.widget.RecyclerView
import timber.log.Timber

abstract class ARecyclerAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    protected val data = ArrayList<T>()

    fun setData(newData: List<T>?) {
        Timber.d("setData, data size: ${data.size}")

        // it's a set, so we can first clear previous data
        data.clear()
        newData?.let {
            data.addAll(it)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun isEmpty(): Boolean {
        return itemCount == 0
    }

}
