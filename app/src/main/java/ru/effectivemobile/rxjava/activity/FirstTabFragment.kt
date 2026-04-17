package ru.effectivemobile.rxjava.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.effectivemobile.rxjava.R
import ru.effectivemobile.rxjava.api.ApiClient
import java.util.concurrent.TimeUnit

class FirstTabFragment : Fragment() {

    private lateinit var timerTextView: TextView
    private lateinit var editText: EditText
    private lateinit var fetchButton: Button
    private lateinit var resultTextView: TextView

    private val disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timerTextView = view.findViewById(R.id.tv_timer)
        editText = view.findViewById(R.id.et_debounce)
        fetchButton = view.findViewById(R.id.btn_fetch)
        resultTextView = view.findViewById(R.id.tv_result)

        startTimer()
        setupDebounce()
        setupNetworkRequest()
    }

    private fun startTimer() {
        var counter = 0
        disposables.add(
            Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    timerTextView.text = "Таймер: ${++counter} сек"
                }
        )
    }

    private fun setupDebounce() {
        disposables.add(
            RxTextView.textChanges(editText)
                .skipInitialValue()
                .debounce(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { text ->
                    Log.d("Debounce", "Пользователь ввел: $text (пауза 3 сек)")
                }
        )
    }

    private fun setupNetworkRequest() {
        fetchButton.setOnClickListener {
            disposables.add(
                ApiClient.simpleApi.getPost()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ post ->
                        resultTextView.text = "Получено: ${post.title}"
                    }, { error ->
                        resultTextView.text = "Ошибка: ${error.message}"
                    })
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}