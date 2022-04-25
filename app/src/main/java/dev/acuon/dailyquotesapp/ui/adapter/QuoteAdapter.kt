package dev.acuon.dailyquotesapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.acuon.dailyquotesapp.R
import dev.acuon.dailyquotesapp.data.model.Quote
import dev.acuon.dailyquotesapp.utils.ShareUtils
import kotlinx.android.synthetic.main.item_layout.view.*

// Adapter of RecyclerView present in Bookmarked Quotes Fragment
class QuoteAdapter : RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder>() {

    inner class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    // differ callback that checks if elements are same of not
    private val differCallback = object : DiffUtil.ItemCallback<Quote>() {
        override fun areItemsTheSame(oldItem: Quote, newItem: Quote): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Quote, newItem: Quote): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    // inflate layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        return QuoteViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    // Custom item onLongClick listener
    private var onItemLongClickListener: ((Quote) -> Boolean)? = null

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val quote = differ.currentList[position]
        holder.itemView.apply {

            // set up each item in the recycler view
            rvQuoteTv.text = resources.getString(R.string.quote, quote.quote)
            rvAuthorTv.text = resources.getString(R.string.author, quote.author)

            rvQuoteTv.visibility = View.VISIBLE
            rvAuthorTv.visibility = View.VISIBLE

            rvQuoteLoading.visibility = View.GONE

            rvQuoteShare.setOnClickListener {
                // Hide share image to not get included in the image
                rvQuoteShare.visibility = View.GONE

                ShareUtils.share(rvItemHolder, context)

                // Restore the hidden share button back
                rvQuoteShare.visibility = View.VISIBLE
            }

            // onLongClick Listner definition
            setOnLongClickListener {
                onItemLongClickListener.let {
                    if (it != null) {
                        it(quote)
                    }
                    true
                }
            }
        }
    }

    // actual method to set custom defined listener
    fun setOnItemLongClickListener(listener: (Quote) -> Boolean) {
        onItemLongClickListener = listener
    }
}
