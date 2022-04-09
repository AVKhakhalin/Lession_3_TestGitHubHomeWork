package ru.geekbrains.tests.lession_3_githubhomework

import ERROR_TEXT
import NULL_RESULT_TEXT
import SOME_QUERY_TEXT
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.*
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config
import ru.geekbrains.tests.lession_3_githubhomework.Constants.Companion.UNSUCCESSFUL_RESULT_TEXT
import ru.geekbrains.tests.lession_3_githubhomework.model.SearchResponse
import ru.geekbrains.tests.lession_3_githubhomework.model.SearchResult
import ru.geekbrains.tests.lession_3_githubhomework.repository.GitHubRepository
import ru.geekbrains.tests.lession_3_githubhomework.view.search.ScreenState
import ru.geekbrains.tests.lession_3_githubhomework.view.search.SearchViewModel
import java.util.*

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
@ExperimentalCoroutinesApi
class SearchViewModelCoroutineTest {
    /** Задание переменных */ //region
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    var testCoroutineRule = TestCoroutineRule()
    private lateinit var searchViewModel: SearchViewModel
    @Mock
    private lateinit var repository: GitHubRepository
    //endregion

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        searchViewModel = SearchViewModel(repository)
    }

    @Test //Проверим вызов метода searchGitHub() у нашей ВьюМодели
    fun coroutines_SearchGitHubTest() {
        testCoroutineRule.runBlockingTest {
            Mockito.`when`(repository.searchGithubAsync(SOME_QUERY_TEXT)).thenReturn(
                SearchResponse(1, listOf())
            )

            searchViewModel.searchGitHub(SOME_QUERY_TEXT)
            Mockito.verify(repository, Mockito.times(1))
                .searchGithubAsync(SOME_QUERY_TEXT)
        }
    }

    @Test // Проверка на возврат через liveData корректных данных
    fun coroutines_TestReturnValueIsNotNull() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<ScreenState> {}
            val liveData = searchViewModel.subscribeToLiveData()
            //При вызове Репозитория возвращаем шаблонные данные
            Mockito.`when`(repository.searchGithubAsync(SOME_QUERY_TEXT)).thenReturn(
                SearchResponse(1, listOf())
            )

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
    }

    @Test // Проверка, что liveData возвращает ошибку
    fun coroutines_TestReturnValueIsError() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<ScreenState> {}
            val liveData = searchViewModel.subscribeToLiveData()
            //При вызове Репозитория возвращаем ошибку
            Mockito.`when`(repository.searchGithubAsync(SOME_QUERY_TEXT)).thenReturn(
                SearchResponse(null, listOf())
            )

            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SOME_QUERY_TEXT)
                //Убеждаемся, что Репозиторий вернул ошибку и LiveData возвращает ошибку
                val value: ScreenState.Error = liveData.value as ScreenState.Error
                Assert.assertEquals(value.error.message, NULL_RESULT_TEXT)
            } finally {
                liveData.removeObserver(observer)
            }
        }
    }

    @Test // Проверка обработки выбрасывания исключения
    fun coroutines_TestException() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<ScreenState> {}
            val liveData = searchViewModel.subscribeToLiveData()

            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SOME_QUERY_TEXT)
                // Проверка получения нужного исключения
                val value: ScreenState.Error = liveData.value as ScreenState.Error
                Assert.assertEquals(value.error.message, UNSUCCESSFUL_RESULT_TEXT)
            } finally {
                liveData.removeObserver(observer)
            }
        }
    }

    @Test // Проверяем, что вьюмодель пришла в состояние корректных данных
    fun viewModel_StateCorrectData() {
        testCoroutineRule.runBlockingTest {
            //При вызове Репозитория возвращаем шаблонные данные
            val index: Int = 1
            Mockito.`when`(repository.searchGithubAsync(SOME_QUERY_TEXT)).thenReturn(
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

            //Создаем обсервер. В лямбде мы не вызываем никакие методы - в этом нет необходимости
            //так как мы проверяем работу LiveData и не собираемся ничего делать с данными,
            // которые она возвращает
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
                    Assert.assertEquals(searchResult[0].hasWiki, true)
                    Assert.assertEquals(searchResult[0].archived, true)
                    Assert.assertEquals(searchResult[0].score, 7.0)
                }
            } finally {
                //Тест закончен, снимаем Наблюдателя
                liveData.removeObserver(observer)
            }
        }
    }

    @Test // Проверка, что вьюмодель пришла в состояние ошибки, потому что liveData.value = null
    fun viewModel_StateErrorByNullLiveData() {
        testCoroutineRule.runBlockingTest {
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
    }

    @Test // Проверка, что вьюмодель пришла в состояние ошибки
    fun viewModel_StateErrorNull() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<ScreenState> {}
            val liveData = searchViewModel.subscribeToLiveData()
            //При вызове Репозитория возвращаем null
            Mockito.`when`(repository.searchGithubAsync(SOME_QUERY_TEXT)).thenReturn(null)

            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SOME_QUERY_TEXT)
                // Убеждаемся, что Репозиторий вернул ошибку и LiveData возвращает именно ошибку
                Assert.assertTrue(liveData.value is ScreenState.Error)
            } finally {
                liveData.removeObserver(observer)
            }
        }
    }

    @Test // Проверка, что вьюмодель пришла в состояние ошибки
    fun viewModel_StateError() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<ScreenState> {}
            val liveData = searchViewModel.subscribeToLiveData()
            // Задание объекта с состоянием "Ошибка"
            val error = Throwable(ERROR_TEXT)
            //При вызове Репозитория возвращаем ошибку
            Mockito.`when`(repository.searchGithubAsync(SOME_QUERY_TEXT)).then {
                Optional.of(error(error))
            }

            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SOME_QUERY_TEXT)
                // Убеждаемся, что Репозиторий вернул ошибку и LiveData возвращает именно ошибку
                Assert.assertTrue(liveData.value is ScreenState.Error)
            } finally {
                liveData.removeObserver(observer)
            }
        }
    }

    @Test // Проверка, что вьюмодель пришла в состояние загрузки (НЕ УДАЛОСЬ РЕАЛИЗОВАТЬ)
    fun viewModel_StateLoad() {
        testCoroutineRule.runBlockingTest {
            // Создание класса состояния "Загрузка"
            val loading: ScreenState.Loading = ScreenState.Loading
            //При вызове Репозитория возвращаем шаблонные данные
            Mockito.`when`(repository.searchGithubAsync(SOME_QUERY_TEXT))
                .then {
                    Optional.of( loading )
                }
            //Создаем обсервер. В лямбде мы не вызываем никакие методы - в этом нет необходимости
            //так как мы проверяем работу LiveData и не собираемся ничего делать с данными,
            // которые она возвращает
            val observer = Observer<ScreenState> {}
            //Получаем LiveData
            val liveData = searchViewModel.subscribeToLiveData()

            try {
                //Подписываемся на LiveData без учета жизненного цикла
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SOME_QUERY_TEXT)
                // Проверяем на появления состояния загрузки
// Этот код работает
                Assert.assertTrue(liveData.value is ScreenState)
/** Этот код НЕ РАБОТАЕТ БУДУ РАД ЛЮБЫМ ПОДСКАЗКАМ ПО ЭТОМУ ПОВОДУ */
                Assert.assertTrue(liveData.value is ScreenState.Loading)
// Появляется следующее сообщение об ошибке:
/*
java.lang.AssertionError
	at org.junit.Assert.fail(Assert.java:87)
	at org.junit.Assert.assertTrue(Assert.java:42)
	at org.junit.Assert.assertTrue(Assert.java:53)
	at ru.geekbrains.tests.lession_3_githubhomework.SearchViewModelCoroutineTest$viewModel_StateLoad$1.invokeSuspend(SearchViewModelCoroutineTest.kt:268)
	at ru.geekbrains.tests.lession_3_githubhomework.SearchViewModelCoroutineTest$viewModel_StateLoad$1.invoke(SearchViewModelCoroutineTest.kt)
	at ru.geekbrains.tests.lession_3_githubhomework.SearchViewModelCoroutineTest$viewModel_StateLoad$1.invoke(SearchViewModelCoroutineTest.kt)
	at ru.geekbrains.tests.lession_3_githubhomework.TestCoroutineRule$runBlockingTest$1.invokeSuspend(TestCoroutineRule.kt:34)
	at ru.geekbrains.tests.lession_3_githubhomework.TestCoroutineRule$runBlockingTest$1.invoke(TestCoroutineRule.kt)
	at ru.geekbrains.tests.lession_3_githubhomework.TestCoroutineRule$runBlockingTest$1.invoke(TestCoroutineRule.kt)
	at kotlinx.coroutines.test.TestBuildersKt$runBlockingTest$deferred$1.invokeSuspend(TestBuilders.kt:50)
	at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:33)
	at kotlinx.coroutines.DispatchedTask.run(DispatchedTask.kt:106)
	at kotlinx.coroutines.test.TestCoroutineDispatcher.dispatch(TestCoroutineDispatcher.kt:50)
	at kotlinx.coroutines.internal.DispatchedContinuationKt.resumeCancellableWith(DispatchedContinuation.kt:305)
	at kotlinx.coroutines.intrinsics.CancellableKt.startCoroutineCancellable(Cancellable.kt:30)
	at kotlinx.coroutines.intrinsics.CancellableKt.startCoroutineCancellable$default(Cancellable.kt:27)
	at kotlinx.coroutines.CoroutineStart.invoke(CoroutineStart.kt:110)
	at kotlinx.coroutines.AbstractCoroutine.start(AbstractCoroutine.kt:158)
	at kotlinx.coroutines.BuildersKt__Builders_commonKt.async(Builders.common.kt:91)
	at kotlinx.coroutines.BuildersKt.async(Unknown Source)
	at kotlinx.coroutines.BuildersKt__Builders_commonKt.async$default(Builders.common.kt:84)
	at kotlinx.coroutines.BuildersKt.async$default(Unknown Source)
	at kotlinx.coroutines.test.TestBuildersKt.runBlockingTest(TestBuilders.kt:49)
	at kotlinx.coroutines.test.TestBuildersKt.runBlockingTest(TestBuilders.kt:73)
	at ru.geekbrains.tests.lession_3_githubhomework.TestCoroutineRule.runBlockingTest(TestCoroutineRule.kt:34)
	at ru.geekbrains.tests.lession_3_githubhomework.SearchViewModelCoroutineTest.viewModel_StateLoad(SearchViewModelCoroutineTest.kt:245)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:566)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:59)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:56)
	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at org.junit.internal.runners.statements.RunBefores.evaluate(RunBefores.java:26)
	at org.junit.internal.runners.statements.RunAfters.evaluate(RunAfters.java:27)
	at org.junit.rules.TestWatcher$1.evaluate(TestWatcher.java:61)
	at ru.geekbrains.tests.lession_3_githubhomework.TestCoroutineRule$apply$1.evaluate(TestCoroutineRule.kt:26)
	at org.junit.runners.ParentRunner$3.evaluate(ParentRunner.java:306)
	at org.robolectric.RobolectricTestRunner$HelperTestRunner$1.evaluate(RobolectricTestRunner.java:575)
	at org.robolectric.internal.SandboxTestRunner$2.lambda$evaluate$0(SandboxTestRunner.java:278)
	at org.robolectric.internal.bytecode.Sandbox.lambda$runOnMainThread$0(Sandbox.java:89)
	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
	at java.base/java.lang.Thread.run(Thread.java:834)
 */
            } finally {
                //Тест закончен, снимаем Наблюдателя
                liveData.removeObserver(observer)
            }
        }
    }

    @After
    fun close() {
        // Остановка Koin
        stopKoin()
    }
}