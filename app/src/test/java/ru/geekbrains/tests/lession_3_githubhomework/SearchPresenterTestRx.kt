package ru.geekbrains.tests.lession_3_githubhomework

import ERROR_TEXT
import NULL_RESULT_TEXT
import SOME_QUERY_TEXT
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import ru.geekbrains.tests.lession_3_githubhomework.model.SearchResponse
import ru.geekbrains.tests.lession_3_githubhomework.presenter.search.SearchPresenter
import ru.geekbrains.tests.lession_3_githubhomework.repository.GitHubRepository
import ru.geekbrains.tests.lession_3_githubhomework.view.search.ViewSearchContract

class SearchPresenterTestRx {
    /** Задание переменных */ //region
    private lateinit var presenter: SearchPresenter
    @Mock
    private lateinit var repository: GitHubRepository
    @Mock
    private lateinit var viewContract: ViewSearchContract
    //endregion

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = SearchPresenter(repository, ScheduleProviderStub())
        presenter.onAttach(viewContract)
    }

    @Test //Проверим вызов метода searchGitHub() у нашего Репозитория
    fun searchGitHub_Test() {
        Mockito.`when`(repository.searchGithub(SOME_QUERY_TEXT)).thenReturn(
            Observable.just(
                SearchResponse(
                    1,
                    listOf()
                )
            )
        )
        presenter.searchGitHub(SOME_QUERY_TEXT)
        Mockito.verify(repository, Mockito.times(1))
            .searchGithub(SOME_QUERY_TEXT)
    }

    @Test //Проверяем как обрабатывается ошибка запроса
    fun handleRequestError_Test() {
        Mockito.`when`(repository.searchGithub(SOME_QUERY_TEXT)).thenReturn(
            Observable.error(Throwable(ERROR_TEXT))
        )
        presenter.searchGitHub(SOME_QUERY_TEXT)
        Mockito.verify(viewContract, Mockito.times(1))
            .displayError(ERROR_TEXT)
    }

    @Test //Проверяем как обрабатываются неполные данные
    fun handleResponseError_TotalCountIsNull() {
        Mockito.`when`(repository.searchGithub(SOME_QUERY_TEXT)).thenReturn(
            Observable.just(
                SearchResponse(
                    null,
                    listOf()
                )
            )
        )
        presenter.searchGitHub(SOME_QUERY_TEXT)
        Mockito.verify(viewContract, Mockito.times(1))
            .displayError(NULL_RESULT_TEXT)
    }

    @Test //Проверим порядок вызова методов viewContract при ошибке
    fun handleResponseError_TotalCountIsNull_ViewContractMethodOrder() {
        Mockito.`when`(repository.searchGithub(SOME_QUERY_TEXT)).thenReturn(
            Observable.just(
                SearchResponse(
                    null,
                    listOf()
                )
            )
        )
        presenter.searchGitHub(SOME_QUERY_TEXT)
        val inOrder = Mockito.inOrder(viewContract)
        inOrder.verify(viewContract).displayLoading(true)
        inOrder.verify(viewContract).displayError(NULL_RESULT_TEXT)
        inOrder.verify(viewContract).displayLoading(false)
    }

    @Test //Теперь проверим успешный ответ сервера
    fun handleResponseSuccess() {
        Mockito.`when`(repository.searchGithub(SOME_QUERY_TEXT)).thenReturn(
            Observable.just(
                SearchResponse(
                    42,
                    listOf()
                )
            )
        )
        presenter.searchGitHub(SOME_QUERY_TEXT)
        Mockito.verify(viewContract, Mockito.times(1))
            .displaySearchResults(listOf(), 42)
    }
}