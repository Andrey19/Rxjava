package ru.effectivemobile.rxjava.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.effectivemobile.rxjava.databinding.ItemDiscountCardBinding
import ru.effectivemobile.rxjava.model.DiscountCard

class CardsAdapter(
    private val onInteractionListener: OnInteractionListener? = null
) : ListAdapter<DiscountCard, CardsAdapter.CardViewHolder>(DiscountCardDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ItemDiscountCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CardViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = getItem(position)
        holder.bind(card)
    }

    fun updateList(newList: List<DiscountCard>) {
        submitList(newList.toList())
    }

    class CardViewHolder(
        private val binding: ItemDiscountCardBinding,
        private val onInteractionListener: OnInteractionListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(card: DiscountCard) {
            binding.apply {
                ownerName.text = "${card.owner} - ${card.discountPercent}%"
                cardDetails.text = "ID: ${card.id} (${card.server})"

                root.setOnClickListener {
                    onInteractionListener?.onCardClick(card)
                }
            }
        }
    }

    interface OnInteractionListener {
        fun onCardClick(card: DiscountCard)
    }
}

class DiscountCardDiffCallback : DiffUtil.ItemCallback<DiscountCard>() {
    override fun areItemsTheSame(oldItem: DiscountCard, newItem: DiscountCard): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DiscountCard, newItem: DiscountCard): Boolean {
        return oldItem == newItem
    }
}