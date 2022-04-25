package dev.acuon.dailyquotesapp.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import dev.acuon.dailyquotesapp.R
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    var home = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // logic to switch between fragments
        myBookmarksImgBtn.setOnClickListener {
            val action = QuoteFragmentDirections
                .actionQuoteFragmentToBookmarkFragment()
            val navController = Navigation.findNavController(quotesNavHostFragment)

            navController.navigate(action)
            it.visibility = View.GONE
            backToQuotePage.visibility = View.VISIBLE
            activity_title.text = resources.getText(R.string.myBookMarks)
            home = false
        }

        backToQuotePage.setOnClickListener {
            super.onBackPressed()

            it.visibility = View.GONE
            myBookmarksImgBtn.visibility = View.VISIBLE
            activity_title.text = resources.getText(R.string.app_name)
            home = true
        }

        // Update theme once everything is set up
        setTheme(R.style.Theme_DailyQuotesApp)

    }

    // implementation to handle error cases regarding navigation icons
    // this function updates the icons and sets variables according
    // to how navigation was carried out
    override fun onBackPressed() {
        super.onBackPressed()
        if (!home) {
            backToQuotePage.visibility = View.GONE
            myBookmarksImgBtn.visibility = View.VISIBLE
            activity_title.text = resources.getText(R.string.app_name)
            home = true
        }
    }
}