package ru.geekbrains.tests.lession_3_githubhomework.view.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.java.KoinJavaComponent.getKoin
import ru.geekbrains.tests.lession_3_githubhomework.R
import ru.geekbrains.tests.lession_3_githubhomework.model.SearchResult
import ru.geekbrains.tests.lession_3_githubhomework.presenter.search.PresenterSearchContract
import ru.geekbrains.tests.lession_3_githubhomework.presenter.search.SearchPresenter
import ru.geekbrains.tests.lession_3_githubhomework.view.details.DetailsActivity
import java.util.*

class MainActivity {
    class MainActivity: AppCompatActivity(), ViewSearchContract {

        private val adapter = SearchResultAdapter()
        private val viewModel: SearchViewModel by lazy {
            ViewModelProvider(this).get(SearchViewModel::class.java)
        }
        private var totalCount: Int = 0

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            setUI()
            viewModel.subscribeToLiveData().observe(this) { onStateChange(it) }
        }

        private fun onStateChange(screenState: ScreenState) {
            when (screenState) {
                is ScreenState.Working -> {
                    val searchResponse = screenState.searchResponse
                    val totalCount = searchResponse.totalCount
                    progressBar.visibility = View.GONE
                    with(totalCountTextView) {
                        visibility = View.VISIBLE
                        text =
                            String.format(
                                Locale.getDefault(),
                                getString(R.string.results_count),
                                totalCount
                            )
                    }

                    this.totalCount = totalCount!!
                    adapter.updateResults(searchResponse.searchResults!!)
                }
                is ScreenState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is ScreenState.Error -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, screenState.error.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun setUI() {
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
            // Установка события нажатия на кнопку "Поиск репозиториев"
            toSearchActivityButton.setOnClickListener {
                val query = searchEditText.text.toString()
                if (query.isNotBlank()) {
                    viewModel.searchGitHub(query)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.enter_search_word),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            // Установка события нажатия на поисковый элемент (лупа) на клавиатуре
            searchEditText.setOnEditorActionListener(
                TextView.OnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        val query = searchEditText.text.toString()
                        if (query.isNotBlank()) {
                            viewModel.searchGitHub(query)
                            return@OnEditorActionListener true
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                getString(R.string.enter_search_word),
                                Toast.LENGTH_SHORT
                            ).show()
                            return@OnEditorActionListener false
                        }
                    }
                    false
                })
        }

        override fun displaySearchResults(
            searchResults: List<SearchResult>,
            totalCount: Int
        ) {
            with(totalCountTextView) {
                visibility = android.view.View.VISIBLE
                text =
                    kotlin.String.format(java.util.Locale.getDefault(),
                        getString(R.string.results_count), totalCount)
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
        }
    }
}