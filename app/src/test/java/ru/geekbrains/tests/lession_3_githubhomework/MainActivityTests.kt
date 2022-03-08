package ru.geekbrains.tests.lession_3_githubhomework

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import junit.framework.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ru.geekbrains.tests.lession_3_githubhomework.view.search.MainActivity
import org.mockito.MockitoAnnotations
import org.robolectric.shadows.ShadowToast
import ru.geekbrains.tests.lession_3_githubhomework.view.details.DetailsActivity
import ru.geekbrains.tests.lession_3_githubhomework.view.details.DetailsActivity.Companion.TOTAL_COUNT_EXTRA


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.R])
class MainActivityTests {
    // Создание сценария для активити
    private lateinit var scenarioMainActivity: ActivityScenario<MainActivity>
    private lateinit var context: Context

    @Before
    fun setup() {
        // launch виртуально стартует MainActivity активити
        scenarioMainActivity = ActivityScenario.launch(MainActivity::class.java)
        // Получаем контекст
        context = ApplicationProvider.getApplicationContext()
        // Настройка аннотации "@Mock"
        MockitoAnnotations.initMocks(this)
    }

    //region Тестирование корректности загрузки MainActivity
    @Test // Проверка корректности создания класса MainActivity
    fun activity_AssertNotNull() {
        // Получаем объект MainActivity
        scenarioMainActivity.onActivity { mainActivity ->
            assertNotNull(mainActivity)
        }
    }
    @Test // Проверка прохождения MainActivity через состояние onResume()
    fun activity_IsResumed() {
        // В момент, когда тест запускается, активити находится
        // не в созданном состоянии, а в состоянии Resume
        assertEquals(Lifecycle.State.RESUMED, scenarioMainActivity.state)
    }
    //endregion

    //region Проверка наличия элементов в макете
    @Test // Проверка наличия в макете элемента с id "searchEditText"
    fun activitySearchEditText_NotNull() {
        scenarioMainActivity.onActivity {
            val searchEditTextView = it.findViewById<EditText>(R.id.searchEditText)
            assertNotNull(searchEditTextView)
        }
    }
    @Test // Проверка наличия в макете элемента с id "toDetailsActivityButton"
    fun activityToDetailsActivityButton_NotNull() {
        scenarioMainActivity.onActivity {
            val toDetailsActivityButton = it.findViewById<Button>(R.id.toDetailsActivityButton)
            assertNotNull(toDetailsActivityButton)
        }
    }
    @Test // Проверка наличия в макете элемента с id "progressBar"
    fun activityProgressBar_NotNull() {
        scenarioMainActivity.onActivity {
            val progressBar = it.findViewById<ProgressBar>(R.id.progressBar)
            assertNotNull(progressBar)
        }
    }
    //endregion

    //region Проверка наличия корректного текста в элементах
    @Test // Проверка наличия текста в поле text на элементе "toDetailsActivityButton"
    fun activityToDetailsActivityButton_HasText() {
        scenarioMainActivity.onActivity {
            val toDetailsActivityButton = it.findViewById<Button>(R.id.toDetailsActivityButton)
            assertEquals("to details", toDetailsActivityButton.text)
        }
    }
    @Test // Проверка наличия текста в поле hint на элементе "searchEditText"
    fun activitySearchEditText_HasText() {
        scenarioMainActivity.onActivity {
            val searchEditText = it.findViewById<EditText>(R.id.searchEditText)
            assertEquals("Enter keyword e.g. android", searchEditText.hint)
        }
    }
    //endregion

    //region Проверка видимости элементов на макете MainActivity
    @Test
    fun activityProgressBar_IsVisible() {
        scenarioMainActivity.onActivity {
            val progressBar = it.findViewById<ProgressBar>(R.id.progressBar)
            assertEquals(View.GONE, progressBar.visibility)
        }
    }
    @Test
    fun activitySearchEditText_IsVisible() {
        scenarioMainActivity.onActivity {
            val searchEditText = it.findViewById<EditText>(R.id.searchEditText)
            assertEquals(View.VISIBLE, searchEditText.visibility)
        }
    }
    @Test
    fun activityToDetailsActivityButton_IsVisible() {
        scenarioMainActivity.onActivity {
            val toDetailsActivityButton = it.findViewById<Button>(R.id.toDetailsActivityButton)
            assertEquals(View.VISIBLE, toDetailsActivityButton.visibility)
        }
    }
    @Test
    fun activityRecyclerView_IsVisible() {
        scenarioMainActivity.onActivity {
            val recyclerView = it.findViewById<RecyclerView>(R.id.recyclerView)
            assertEquals(View.VISIBLE, recyclerView.visibility)
        }
    }
    @Test
    fun activityElements_AreVisible() {
        scenarioMainActivity.onActivity {
            val progressBar = it.findViewById<ProgressBar>(R.id.progressBar)
            assertEquals(View.GONE, progressBar.visibility)
            val searchEditText = it.findViewById<EditText>(R.id.searchEditText)
            assertEquals(View.VISIBLE, searchEditText.visibility)
            val toDetailsActivityButton = it.findViewById<Button>(R.id.toDetailsActivityButton)
            assertEquals(View.VISIBLE, toDetailsActivityButton.visibility)
            val recyclerView = it.findViewById<RecyclerView>(R.id.recyclerView)
            assertEquals(View.VISIBLE, recyclerView.visibility)
        }
    }
    @Test // Проверка работоспособности метода displayLoading()
    fun acitivityDisplayLoading_Test() {
        scenarioMainActivity.onActivity {
            it.displayLoading(true)
            val progressBar = it.findViewById<ProgressBar>(R.id.progressBar)
            assertEquals(View.VISIBLE, progressBar.visibility)
            it.displayLoading(false)
            assertEquals(View.GONE, progressBar.visibility)
        }
    }
    //endregion

    //region Проверка работоспособности элементов
    @Test // Проверка нажатия на кнопку с id "toDetailsActivityButton"
    fun activityToDetailsActivityButton_IsWorking() {
        scenarioMainActivity.onActivity {
            val toDetailsActivityButton = it.findViewById<Button>(R.id.toDetailsActivityButton)
            assertEquals(toDetailsActivityButton.performClick(), true)
        }
    }
    //endregion

    //region Проверка отправки сообщений в DetailsActivity через Intent
    @Test
    fun activityCreateIntent_NotNull() {
        scenarioMainActivity.onActivity {
            val intent: Intent = it.intent.putExtra(TOTAL_COUNT_EXTRA, 0)
            assertNotNull(intent)
        }
    }
    @Test
    fun activityCreateIntent_HasExtras() {
        scenarioMainActivity.onActivity {
            val intent: Intent = it.intent.putExtra(TOTAL_COUNT_EXTRA, 0)
            val bundle: Bundle? = intent.extras
            assertNotNull(bundle)
        }
    }
    @Test
    fun activityCreateIntent_HasCount() {
        scenarioMainActivity.onActivity {
            val count: Int = 4
            val intent: Intent = it.intent.putExtra(TOTAL_COUNT_EXTRA, count)
            val bundle: Bundle? = intent.extras
            assertEquals(count, bundle?.getInt(TOTAL_COUNT_EXTRA, 0))
        }
    }
    //endregion

    @After
    fun close() {
        // Сценарий обязательно должен закончиться
        // иначе другие тесты будут происходить на незачищенных данных
        scenarioMainActivity.close()
    }
}