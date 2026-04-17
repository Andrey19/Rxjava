package ru.effectivemobile.rxjava.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.CompositeDisposable
import ru.effectivemobile.rxjava.R
import ru.effectivemobile.rxjava.adapter.ListItemAdapter

class SecondTabFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListItemAdapter
    private val disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_second_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rv_items)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val items = (1..20).map { "Элемент №$it" }
        adapter = ListItemAdapter(items)
        recyclerView.adapter = adapter

        disposables.add(
            adapter.getItemClickObservable()
                .subscribe { position ->
                    Toast.makeText(requireContext(), "Нажата позиция: ${position + 1}", Toast.LENGTH_SHORT).show()
                }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
    }
}