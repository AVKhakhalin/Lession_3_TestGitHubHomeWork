package ru.geekbrains.tests.lession_3_githubhomework.view.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.geekbrains.tests.lession_3_githubhomework.BuildConfig
import ru.geekbrains.tests.lession_3_githubhomework.R
import ru.geekbrains.tests.lession_3_githubhomework.model.SearchResult
import ru.geekbrains.tests.lession_3_githubhomework.presenter.RepositoryContract
import ru.geekbrains.tests.lession_3_githubhomework.presenter.search.PresenterSearchContract
import ru.geekbrains.tests.lession_3_githubhomework.presenter.search.SearchPresenter
import ru.geekbrains.tests.lession_3_githubhomework.repository.FakeGitHubRepository
import ru.geekbrains.tests.lession_3_githubhomework.repository.GitHubApi
import ru.geekbrains.tests.lession_3_githubhomework.repository.GitHubRepository
import ru.geekbrains.tests.lession_3_githubhomework.view.details.DetailsActivity
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity: AppCompatActivity(), ViewSearchContract {

    private val adapter = SearchResultAdapter()
    private val presenter: PresenterSearchContract = SearchPresenter(this, createRepository())
    private var totalCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUI()
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
        searchEditText.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = searchEditText.text.toString()
                if (query.isNotBlank()) {
                    presenter.searchGitHub(query)
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

    private fun createRepository(): RepositoryContract {
        return if (BuildConfig.TYPE == FAKE) {
            FakeGitHubRepository()
        } else {
            GitHubRepository(createRetrofit().create(GitHubApi::class.java))
        }
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
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
        const val FAKE = "FAKE"
    }
}
