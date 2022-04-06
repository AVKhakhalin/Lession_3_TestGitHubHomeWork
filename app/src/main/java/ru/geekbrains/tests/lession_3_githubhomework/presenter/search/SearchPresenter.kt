package ru.geekbrains.tests.lession_3_githubhomework.presenter.search

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import retrofit2.Response
import ru.geekbrains.tests.lession_3_githubhomework.model.SearchResponse
import ru.geekbrains.tests.lession_3_githubhomework.presenter.RepositoryContract
import ru.geekbrains.tests.lession_3_githubhomework.presenter.SchedulerProvider
import ru.geekbrains.tests.lession_3_githubhomework.repository.RepositoryCallback
import ru.geekbrains.tests.lession_3_githubhomework.view.ViewContract
import ru.geekbrains.tests.lession_3_githubhomework.view.search.ViewSearchContract

/**
 * В архитектуре MVP все запросы на получение данных адресуются в Репозиторий.
 * Запросы могут проходить через Interactor или UseCase, использовать источники
 * данных (DataSource), но суть от этого не меняется.
 * Непосредственно Презентер отвечает за управление потоками запросов и ответов,
 * выступая в роли регулировщика движения на перекрестке.
 */

internal class SearchPresenter internal constructor(
    private val repository: RepositoryContract
): PresenterSearchContract, RepositoryCallback {

    /** Задание переменных */ //region
    private var viewContract: ViewSearchContract? = null
    private val appSchedulerProvider: SchedulerProvider = SearchSchedulerProvider()
    //endregion

    override fun onAttach(viewContract: ViewContract) {
        this.viewContract = viewContract as ViewSearchContract
    }

    override fun onDetach() {
        viewContract = null
    }

    override fun searchGitHub(searchQuery: String) {
        // Dispose
        viewContract?.let { viewContract ->
            val compositeDisposable = CompositeDisposable()
            compositeDisposable.add(
                repository.searchGithub(searchQuery)
                    .subscribeOn(appSchedulerProvider.io())
                    .observeOn(appSchedulerProvider.ui())
                    .doOnSubscribe { viewContract.displayLoading(true) }
                    .doOnTerminate { viewContract.displayLoading(false) }
                    .subscribeWith(object: DisposableObserver<SearchResponse>() {
                        override fun onNext(searchResponse: SearchResponse) {
                            val searchResults = searchResponse.searchResults
                            val totalCount = searchResponse.totalCount
                            if (searchResults != null && totalCount != null) {
                                viewContract.displaySearchResults(
                                    searchResults,
                                    totalCount
                                )
                            } else {
                                viewContract.displayError("Search results or total count are null")
                            }
                        }

                        override fun onError(e: Throwable) {
                            viewContract.displayError(e.message ?: "Response is null or unsuccessful")
                        }

                        override fun onComplete() {}
                    }
                    )
            )
        }
    }

    override fun handleGitHubResponse(response: Response<SearchResponse?>?) {
        viewContract?.let { viewContract ->
            viewContract.displayLoading(false)
            if (response != null && response.isSuccessful) {
                val searchResponse = response.body()
                val searchResults = searchResponse?.searchResults
                val totalCount = searchResponse?.totalCount
                if (searchResults != null && totalCount != null) {
                    viewContract.displaySearchResults(
                        searchResults,
                        totalCount
                    )
                } else {
                    viewContract.displayError("Search results or total count are null")
                }
            } else {
                viewContract.displayError("Response is null or unsuccessful")
            }
        }
    }

    override fun handleGitHubError() {
        viewContract?.let { viewContract ->
            viewContract.displayLoading(false)
            viewContract.displayError()
        }
    }
}
