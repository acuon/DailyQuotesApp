package dev.acuon.dailyquotesapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import dev.acuon.dailyquotesapp.R
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.acuon.dailyquotesapp.ui.adapter.QuoteAdapter
import dev.acuon.dailyquotesapp.viewmodel.QuoteViewModel
import kotlinx.android.synthetic.main.fragment_bookmark.*

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.M)
class BookmarkFragment : Fragment(R.layout.fragment_bookmark) {

    private val viewModel by activityViewModels<QuoteViewModel>()
    lateinit var quotesAdapter: QuoteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        // copy quote when the item is long clicked
        quotesAdapter.setOnItemLongClickListener {
            val clipBoardManager = (activity as MainActivity)
                .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val clipdata = ClipData.newPlainText(
                "quote",
                "\"${it.quote}\"\n\n- ${it.author}"
            )

            clipBoardManager.setPrimaryClip(clipdata)

            if (!(activity as MainActivity).home) Snackbar.make(
                view,
                "Quote Copied!",
                Snackbar.LENGTH_SHORT
            ).show()
            true
        }

        // callback which defines what should be done when items are swiped
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            // delete the quote on Swipe
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val quote = quotesAdapter.differ.currentList[position]
                viewModel.deleteQuote(quote)
                Snackbar.make(view, "Removed Bookmark!", Snackbar.LENGTH_SHORT)
                    .apply {
                        // if the click was in error, then provide re-saving option
                        setAction("Undo") {
                            viewModel.saveQuote(quote)
                            if ((activity as MainActivity?) != null && !(activity as MainActivity).home) Snackbar.make(
                                view,
                                "Re-saved!",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                        setActionTextColor(ContextCompat.getColor(view.context, R.color.light_blue))
                        show()
                    }
            }
        }

        // attach the swipe behavior to each recycler view item
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rvSavedQuotes)
        }

        // observe data changes and apply them to the recycler view
        viewModel.getSavedQuotes().observe(viewLifecycleOwner) { articles ->
            quotesAdapter.differ.submitList(articles)

            // if no quotes present, then show textview and hide recyclerview
            if (articles.isEmpty()) {
                rvSavedQuotes.visibility = View.GONE
                tvNoBookmarks.visibility = View.VISIBLE
            } else {
                rvSavedQuotes.visibility = View.VISIBLE
                tvNoBookmarks.visibility = View.GONE
            }
        }
    }

    // function to set adapter and layout manager on the recycler view
    private fun setupRecyclerView() {
        quotesAdapter = QuoteAdapter()
        rvSavedQuotes.apply {
            adapter = quotesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}