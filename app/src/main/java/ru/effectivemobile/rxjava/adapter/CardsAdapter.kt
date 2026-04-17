package ru.effectivemobile.rxjava.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.effectivemobile.rxjava.model.DiscountCard

class CardsAdapter(private var cards: List<DiscountCard>) :
    RecyclerView.Adapter<CardsAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cards[position])
    }

    override fun getItemCount(): Int = cards.size

    fun updateList(newList: List<DiscountCard>) {
        cards = newList
        notifyDataSetChanged()
    }

    class CardViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        private val text1 = itemView.findViewById<android.widget.TextView>(android.R.id.text1)
        private val text2 = itemView.findViewById<android.widget.TextView>(android.R.id.text2)

        fun bind(card: DiscountCard) {
            text1.text = "${card.owner} - ${card.discountPercent}%"
            text2.text = "ID: ${card.id} (${card.server})"
        }
    }
}