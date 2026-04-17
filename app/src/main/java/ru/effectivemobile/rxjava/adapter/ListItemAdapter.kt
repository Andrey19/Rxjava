package ru.effectivemobile.rxjava.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class ListItemAdapter(
    private val items: List<String>
) : RecyclerView.Adapter<ListItemAdapter.ViewHolder>() {

    private val itemClickSubject = PublishSubject.create<Int>()

    fun getItemClickObservable() = itemClickSubject.hide()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position, itemClickSubject)
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView = itemView as TextView

        fun bind(item: String, position: Int, subject: PublishSubject<Int>) {
            textView.text = item
            RxView.clicks(itemView)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe { subject.onNext(position) }
        }
    }
}