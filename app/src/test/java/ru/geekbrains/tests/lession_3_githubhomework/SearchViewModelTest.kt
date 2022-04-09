package ru.geekbrains.tests.lession_3_githubhomework

import ERROR_TEXT
import SOME_QUERY_TEXT
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.*
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config
import ru.geekbrains.tests.lession_3_githubhomework.model.SearchResponse
import ru.geekbrains.tests.lession_3_githubhomework.model.SearchResult
import ru.geekbrains.tests.lession_3_githubhomework.repository.FakeGitHubRepository
import ru.geekbrains.tests.lession_3_githubhomework.view.search.ScreenState
import ru.geekbrains.tests.lession_3_githubhomework.view.search.SearchViewModel
import java.util.*
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class SearchViewModelTest {
    /** Задание переменных */ //region
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var searchViewModel: SearchViewModel
    @Mock
    private lateinit var repository: FakeGitHubRepository
    //endregion

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        searchViewModel = SearchViewModel(repository, ScheduleProviderStub())
    }

    @Test //Проверим вызов метода searchGitHub() у нашей ВьюМодели
    fun search_Test() {
        Mockito.`when`(repository.searchGithub(SOME_QUERY_TEXT)).thenReturn(
            Observable.just(
                SearchResponse(
                    1,
                    listOf()
                )
            )
        )

        searchViewModel.searchGitHub(SOME_QUERY_TEXT)
        Mockito.verify(repository, Mockito.times(1))
            .searchGithub(SOME_QUERY_TEXT)
    }

    @Test // Проверка на возврат через liveData корректных данных
    fun liveData_TestReturnValueIsNotNull() {
        //При вызове Репозитория возвращаем шаблонные данные
        Mockito.`when`(repository.searchGithub(SOME_QUERY_TEXT)).thenReturn(
            Observable.just(
                SearchResponse(
                    1,
                    listOf()
                )
            )
        )

        //Создаем обсервер. В лямбде мы не вызываем никакие методы - в этом нет необходимости
        //так как мы проверяем работу LiveData и не собираемся ничего делать с данными, которые она возвращает
        val observer = Observer<ScreenState> {}
        //Получаем LiveData
        val liveData = searchViewModel.subscribeToLiveData()

        try {
            //Подписываемся на LiveData без учета жизненного цикла
            liveData.observeForever(observer)
            searchViewModel.searchGitHub(SOME_QUERY_TEXT)
            //Убеждаемся, что Репозиторий вернул данные и LiveData передала их Наблюдателям
            Assert.assertNotNull(liveData.value)
        } finally {
            //Тест закончен, снимаем Наблюдателя
            liveData.removeObserver(observer)
        }
    }

    @Test // Проверка, что liveData возвращает ошибку
    fun liveData_TestReturnValueIsError() {
        val observer = Observer<ScreenState> {}
        val liveData = searchViewModel.subscribeToLiveData()
        val error = Throwable(ERROR_TEXT)

        //При вызове Репозитория возвращаем ошибку
        Mockito.`when`(repository.searchGithub(SOME_QUERY_TEXT)).thenReturn(
            Observable.error(error)
        )

        try {
            liveData.observeForever(observer)
            searchViewModel.searchGitHub(SOME_QUERY_TEXT)
            //Убеждаемся, что Репозиторий вернул ошибку и LiveData возвращает ошибку
            val value: ScreenState.Error = liveData.value as ScreenState.Error
            Assert.assertEquals(value.error.message, error.message)
        } finally {
            liveData.removeObserver(observer)
        }
    }

    @Test // Проверяем, что вьюмодель пришла в состояние корректных данных
    fun viewModel_StateCorrectData() {
        //При вызове Репозитория возвращаем шаблонные данные
        val index: Int = 1
        Mockito.`when`(repository.searchGithub(SOME_QUERY_TEXT)).thenReturn(
            Observable.just(
                SearchResponse(
                    579,
                    listOf(
                        SearchResult(
                            id = index,
                            name = "Name: $index",
                            fullName = "FullName: $index",
                            private = false,
                            description = "Description: $index",
                            updatedAt = "Updated: $index",
                            size = index,
                            stargazersCount = 100,
                            language = "",
                            hasWiki = true,
                            archived = true,
                            score = 7.0
                        )
                    )
                )
            )
        )

        //Создаем обсервер. В лямбде мы не вызываем никакие методы - в этом нет необходимости
        //так как мы проверяем работу LiveData и не собираемся ничего делать с данными, которые она возвращает
        val observer = Observer<ScreenState> {}
        //Получаем LiveData
        val liveData = searchViewModel.subscribeToLiveData()

        try {
            //Подписываемся на LiveData без учета жизненного цикла
            liveData.observeForever(observer)
            searchViewModel.searchGitHub(SOME_QUERY_TEXT)
            // Анализируем результа, возвращаемый liveData
            var totalCount: Int? = 0
            val value: ScreenState.Working = liveData.value as ScreenState.Working
            val searchResponse = (value as ScreenState.Working).searchResponse
            totalCount = searchResponse.totalCount
            Assert.assertEquals(totalCount, 579)
            searchResponse.searchResults?.let { searchResult ->
                Assert.assertEquals(searchResult[0].id, index)
                Assert.assertEquals(searchResult[0].name, "Name: $index")
                Assert.assertEquals(searchResult[0].fullName, "FullName: $index")
                Assert.assertEquals(searchResult[0].description, "Description: $index")
                Assert.assertEquals(searchResult[0].updatedAt, "Updated: $index")
                Assert.assertEquals(searchResult[0].size, index)
                Assert.assertEquals(searchResult[0].stargazersCount, 100)
                Assert.assertEquals(searchResult[0].language, "")
                Assert.assertEquals(searchResult[0].hasWiki,true)
                Assert.assertEquals(searchResult[0].archived,true)
                Assert.assertEquals(searchResult[0].score,7.0)
            }
        } finally {
            //Тест закончен, снимаем Наблюдателя
            liveData.removeObserver(observer)
        }
    }

    @Test // Проверка, что вьюмодель пришла в состояние ошибки, потому что liveData.value = null
    fun viewModel_StateErrorByNullLiveData() {
        val observer = Observer<ScreenState> {}
        val liveData = searchViewModel.subscribeToLiveData()
        try {
            liveData.observeForever(observer)
            // Проверяем, что liveData.value = null
            Assert.assertNull(liveData.value)
        } finally {
            liveData.removeObserver(observer)
        }
    }

    @Test // Проверка, что вьюмодель пришла в состояние ошибки
    fun viewModel_StateError() {
        val observer = Observer<ScreenState> {}
        val liveData = searchViewModel.subscribeToLiveData()
        val error = Throwable(ERROR_TEXT)

        //При вызове Репозитория возвращаем ошибку
        Mockito.`when`(repository.searchGithub(SOME_QUERY_TEXT)).thenReturn(
            Observable.error(error)
        )

        try {
            liveData.observeForever(observer)
            searchViewModel.searchGitHub(SOME_QUERY_TEXT)
            // Убеждаемся, что Репозиторий вернул ошибку и LiveData возвращает именно ошибку
            Assert.assertTrue(liveData.value is ScreenState.Error)
        } finally {
            liveData.removeObserver(observer)
        }
    }

    @Test // Проверка, что вьюмодель пришла в состояние загрузки
    fun viewModel_StateLoad() {
        val loading = ScreenState.Loading
        //При вызове Репозитория возвращаем шаблонные данные
        Mockito.`when`(repository.searchGithub(SOME_QUERY_TEXT))
            .thenReturn(Observable.create { loading })

        //Создаем обсервер. В лямбде мы не вызываем никакие методы - в этом нет необходимости
        //так как мы проверяем работу LiveData и не собираемся ничего делать с данными, которые она возвращает
        val observer = Observer<ScreenState> {}
        //Получаем LiveData
        val liveData = searchViewModel.subscribeToLiveData()

        try {
            //Подписываемся на LiveData без учета жизненного цикла
            liveData.observeForever(observer)
            searchViewModel.searchGitHub(SOME_QUERY_TEXT)
            // Проверяем на появления состояния загрузки
            Assert.assertTrue(liveData.value is ScreenState.Loading)
        } finally {
            //Тест закончен, снимаем Наблюдателя
            liveData.removeObserver(observer)
        }
    }

    @After
    fun close() {
        stopKoin()
    }
}