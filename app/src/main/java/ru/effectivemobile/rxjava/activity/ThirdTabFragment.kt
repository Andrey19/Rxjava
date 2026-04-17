package ru.effectivemobile.rxjava.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.effectivemobile.rxjava.R
import ru.effectivemobile.rxjava.adapter.CardsAdapter
import ru.effectivemobile.rxjava.model.DiscountCard
import ru.effectivemobile.rxjava.repository.CardRepository

class ThirdTabFragment : Fragment() {

    private lateinit var btnMerge: Button
    private lateinit var btnZip: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var statusText: TextView
    private lateinit var adapter: CardsAdapter

    private val disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_third_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnMerge = view.findViewById(R.id.btn_merge_delay)
        btnZip = view.findViewById(R.id.btn_zip)
        recyclerView = view.findViewById(R.id.rv_cards)
        statusText = view.findViewById(R.id.tv_status)


        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        adapter = CardsAdapter(object : CardsAdapter.OnInteractionListener {
            override fun onCardClick(card: DiscountCard) {
                Toast.makeText(requireContext(),
                    "Нажата карта: ${card.owner} - ${card.discountPercent}%",
                    Toast.LENGTH_SHORT).show()
            }
        })
        recyclerView.adapter = adapter

        btnMerge.setOnClickListener { loadWithMergeDelayError() }
        btnZip.setOnClickListener { loadWithZip() }
    }

    private fun loadWithMergeDelayError() {
        statusText.text = "Загрузка (даже если один сервер упал)..."

        val requestA = CardRepository.getCardsFromServerA()
            .subscribeOn(Schedulers.io())
            .onErrorReturn { error ->
                emptyList()
            }

        val requestB = CardRepository.getCardsFromServerB(simulateError = true)
            .subscribeOn(Schedulers.io())
            .onErrorReturn { error ->
                emptyList()
            }

        disposables.add(
            Observable.merge(
                requestA.toObservable(),
                requestB.toObservable()
            )
                .reduce(mutableListOf<DiscountCard>()) { acc, list ->
                    acc.addAll(list)
                    acc
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { combinedList ->
                        recyclerView.post {
                            adapter.updateList(combinedList)
                            statusText.text = "Загружено карт: ${combinedList.size} (с игнорированием ошибок)"
                        }
                    },
                    { error ->
                        statusText.text = "Ошибка: ${error.message}"
                    }
                )
        )
    }

    private fun loadWithZip() {
        statusText.text = "Загрузка (требуются оба сервера)..."

        val requestA = CardRepository.getCardsFromServerA()
            .subscribeOn(Schedulers.io())

        val requestB = CardRepository.getCardsFromServerB(simulateError = true)
            .subscribeOn(Schedulers.io())

        disposables.add(
            Observable.zip(
                requestA.toObservable(),
                requestB.toObservable()
            ) { cardsA, cardsB ->
                val combined = mutableListOf<DiscountCard>()
                combined.addAll(cardsA)
                combined.addAll(cardsB)
                combined
            }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { combinedList ->
                        recyclerView.post {
                            adapter.updateList(combinedList)
                            statusText.text = "Загружено карт: ${combinedList.size} (оба сервера успешны)"
                        }
                    },
                    { error ->
                        statusText.text = "Ошибка: ${error.message} - один из серверов недоступен, список не показан"
                        recyclerView.post {
                            adapter.updateList(emptyList())
                        }
                    }
                )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}