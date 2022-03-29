package ru.geekbrains.tests.lession_3_githubhomework.view.search

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.java.KoinJavaComponent.getKoin
import ru.geekbrains.tests.lession_3_githubhomework.R
import ru.geekbrains.tests.lession_3_githubhomework.model.SearchResult
import ru.geekbrains.tests.lession_3_githubhomework.presenter.search.PresenterSearchContract
import ru.geekbrains.tests.lession_3_githubhomework.presenter.search.SearchPresenter
import ru.geekbrains.tests.lession_3_githubhomework.view.details.DetailsActivity

class MainActivity {
    class MainActivity: AppCompatActivity(), ViewSearchContract {

        private val adapter = SearchResultAdapter()
        private val presenter: PresenterSearchContract = SearchPresenter(getKoin().get())
        private var totalCount: Int = 0

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            setUI()
        }

        private fun setUI() {
            presenter.onAttach(this)
            toDetailsActivityButton.setOnClickListener {
                startActivity(DetailsActivity.getIntent(this, totalCount))
            }
            setQueryListener()
            setRecyclerView()
        }

        private fun setRecyclerView() {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter
        }

        private fun setQueryListener() {
            toSearchActivityButton.setOnClickListener {
                val query = searchEditText.text.toString()
                if (query.isNotBlank()) {
                    presenter.searchGitHub(query)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.enter_search_word),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        override fun displaySearchResults(
            searchResults: List<SearchResult>,
            totalCount: Int
        ) {
            with(totalCountTextView) {
                visibility = android.view.View.VISIBLE
                text =
                    kotlin.String.format(java.util.Locale.getDefault(), getString(R.string.results_count), totalCount)
            }

            this.totalCount = totalCount
            adapter.updateResults(searchResults)
        }


        override fun displayError() {
            Toast.makeText(this, getString(R.string.undefined_error), Toast.LENGTH_SHORT).show()
        }

        override fun displayError(error: String) {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }

        override fun displayLoading(show: Boolean) {
            if (show) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }

        companion object {
            const val BASE_URL = "https://api.github.com"
        }

        override fun onDestroy() {
            super.onDestroy()
            presenter.onDetach()
        }
    }
}