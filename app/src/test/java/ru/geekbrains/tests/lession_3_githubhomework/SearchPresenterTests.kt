package ru.geekbrains.tests.lession_3_githubhomework

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

    private lateinit var searchPresenter: SearchPresenter

    @Mock
    private lateinit var repository: GitHubRepository

    @Mock
    private lateinit var viewContract: ViewSearchContract

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
        val searchQuery = "some query"
        searchPresenter.searchGitHub("some query")
        verify(repository, times(1)).searchGithub(searchQuery, searchPresenter)
    }

    @Test //Проверяем работу метода handleGitHubError()
    fun handleGitHubError_Test() {
        //Вызываем у Презентера метод handleGitHubError()
        searchPresenter.handleGitHubError()
        //Проверяем, что у viewContract вызывается метод displayError()
        Mockito.verify(viewContract, Mockito.times(1)).displayError()
    }

    @Test // Проверяем работу метода onDetach()
    fun onDetach_Test() {
        searchPresenter.onDetach()
        searchPresenter.handleGitHubError()
        Mockito.verify(viewContract, Mockito.times(0)).displayError()
    }

    @Test // Проверяем работу метода onAttach()
    fun onAttach_Test() {
        searchPresenter.onDetach()
        searchPresenter.onAttach(viewContract)
        searchPresenter.handleGitHubError()
        Mockito.verify(viewContract, Mockito.times(1)).displayError()
    }
}