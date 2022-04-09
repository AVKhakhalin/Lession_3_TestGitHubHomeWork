package ru.geekbrains.tests.lession_3_githubhomework.view.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import kotlinx.coroutines.*
import org.koin.java.KoinJavaComponent.getKoin
import ru.geekbrains.tests.lession_3_githubhomework.Constants.Companion.NULL_RESULT_TEXT
import ru.geekbrains.tests.lession_3_githubhomework.Constants.Companion.UNSUCCESSFUL_RESULT_TEXT
import ru.geekbrains.tests.lession_3_githubhomework.model.SearchResponse
import ru.geekbrains.tests.lession_3_githubhomework.presenter.RepositoryContract
import ru.geekbrains.tests.lession_3_githubhomework.presenter.SchedulerProvider
import ru.geekbrains.tests.lession_3_githubhomework.presenter.search.SearchSchedulerProvider

class SearchViewModel(
    private val repository: RepositoryContract = getKoin().get(),
    private val appSchedulerProvider: SchedulerProvider = SearchSchedulerProvider()
): ViewModel() {
    /** Задание переменных */ //region
    private val _liveData = MutableLiveData<ScreenState>()
    private val liveData: LiveData<ScreenState> = _liveData
    private val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.Main
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable -> handleError(throwable) }
    )
    //endregion

    fun subscribeToLiveData() = liveData

 /*   // Подход RxJava
    fun searchGitHub(searchQuery: String) {
        //Dispose
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            repository.searchGithub(searchQuery)
                .subscribeOn(appSchedulerProvider.io())
                .observeOn(appSchedulerProvider.ui())
                .doOnSubscribe { _liveData.value = ScreenState.Loading }
                .subscribeWith(object : DisposableObserver<SearchResponse>() {

                    override fun onNext(searchResponse: SearchResponse) {
                        val searchResults = searchResponse.searchResults
                        val totalCount = searchResponse.totalCount
                        if (searchResults != null && totalCount != null) {
                            _liveData.value = ScreenState.Working(searchResponse)
                        } else {
                            _liveData.value =
                                ScreenState.Error(Throwable(NULL_RESULT_TEXT))
                        }
                    }

                    override fun onError(e: Throwable) {
                        _liveData.value =
                            ScreenState.Error(
                                Throwable(
                                    e.message ?: UNSUCCESSFUL_RESULT_TEXT
                                )
                            )
                    }

                    override fun onComplete() {}
                }
                )
        )
    }
*/
    /** Подход на Coroutine */ //region
     fun searchGitHub(searchQuery: String) {
         _liveData.value = ScreenState.Loading
         viewModelCoroutineScope.launch {
             val searchResponse = repository.searchGithubAsync(searchQuery)
             val searchResults = searchResponse.searchResults
             val totalCount = searchResponse.totalCount
             if (searchResults != null && totalCount != null) {
                 _liveData.value = ScreenState.Working(searchResponse)
             } else {
                 _liveData.value =
                     ScreenState.Error(Throwable("Search results or total count are null"))
             }
         }
     }
    private fun handleError(error: Throwable) {
        _liveData.value =
            ScreenState.Error(
                Throwable(
                    error.message ?: "Response is null or unsuccessful"
                )
            )
    }
    override fun onCleared() {
        super.onCleared()
        viewModelCoroutineScope.coroutineContext.cancelChildren()
    }
    //endregion
}

