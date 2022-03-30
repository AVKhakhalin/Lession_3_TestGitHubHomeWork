package ru.geekbrains.tests.lession_3_githubhomework

import ONE_INT_VALUE
import SOME_QUERY_TEXT
import ZERO_INT_VALUE
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import ru.geekbrains.tests.lession_3_githubhomework.presenter.search.SearchPresenter
import ru.geekbrains.tests.lession_3_githubhomework.repository.GitHubRepository
import ru.geekbrains.tests.lession_3_githubhomework.view.search.ViewSearchContract

class SearchPresenterTests {
    /** Задание переменных */ //region
    private lateinit var searchPresenter: SearchPresenter
    @Mock
    private lateinit var repository: GitHubRepository
    @Mock
    private lateinit var viewContract: ViewSearchContract
    //endregion

    @Before
    fun setUp() {
        // Настраиваем аннотацию "@Mock"
        MockitoAnnotations.initMocks(this)
        // Создаём  SearchPresenter
        searchPresenter = SearchPresenter(repository)
        // Устанавливаем в SearchPresenter вью ViewSearchContract
        searchPresenter.onAttach(viewContract)
    }

    @Test // Проверяем вызов метода searchGitHub() у GitHubRepository
    fun searchGitHub_Test() {
        val searchQuery = SOME_QUERY_TEXT
        searchPresenter.searchGitHub(SOME_QUERY_TEXT)
        verify(repository, times(1)).searchGithub(searchQuery, searchPresenter)
    }

    @Test //Проверяем работу метода handleGitHubError()
    fun handleGitHubError_Test() {
        //Вызываем у Презентера метод handleGitHubError()
        searchPresenter.handleGitHubError()
        //Проверяем, что у viewContract вызывается метод displayError()
        Mockito.verify(viewContract, Mockito.times(ONE_INT_VALUE)).displayError()
    }

    @Test // Проверяем работу метода onDetach()
    fun onDetach_Test() {
        searchPresenter.onDetach()
        searchPresenter.handleGitHubError()
        Mockito.verify(viewContract, Mockito.times(ZERO_INT_VALUE)).displayError()
    }

    @Test // Проверяем работу метода onAttach()
    fun onAttach_Test() {
        searchPresenter.onDetach()
        searchPresenter.onAttach(viewContract)
        searchPresenter.handleGitHubError()
        Mockito.verify(viewContract, Mockito.times(ONE_INT_VALUE)).displayError()
    }
}