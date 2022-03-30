package ru.geekbrains.tests.lession_3_githubhomework.espresso

import HINT_TEXT
import RESULT_SAMPLE_FAKE_REPOSITORY_TEXT
import SAMPLE_REPOSITORY_NAME_TEXT
import TO_DETAILS_TEXT
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.geekbrains.tests.lession_3_githubhomework.R
import ru.geekbrains.tests.lession_3_githubhomework.view.search.MainActivity

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
//@RunWith(AndroidJUnit4ClassRunner::class)
@RunWith(AndroidJUnit4::class)
class MainActivityFakeEspressoTest {

    /** Задание переменных */ //region
    private lateinit var scenario: ActivityScenario<MainActivity.MainActivity>
    //endregion

    @Before // Установка действия до начала выполнения всех тестов
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity.MainActivity::class.java)
    }

    @Test // Проверка на существование активити
    fun activity_AssertNotNull() {
        scenario.onActivity {
            TestCase.assertNotNull(it)
        }
    }

    @Test // Проверка на прохождение в активити метода onResume()
    fun activity_IsResumed() {
        TestCase.assertEquals(Lifecycle.State.RESUMED, scenario.state)
    }

    @Test // Проверка наличия элемента с id "searchEditText"
    fun activityEditText_NotNull() {
        scenario.onActivity {
            val searchEditText: EditText = it.findViewById<EditText>(R.id.searchEditText)
            TestCase.assertNotNull(searchEditText)
        }
    }

    @Test // Проверка наличия элемента с id "toDetailsActivityButton"
    fun activityToDetailsActivityButton_NotNull() {
        scenario.onActivity {
            val toDetailsActivityButton: Button =
                it.findViewById<Button>(R.id.toDetailsActivityButton)
            TestCase.assertNotNull(toDetailsActivityButton)
        }
    }

    @Test // Проверка наличия элемента с id "totalCountTextView"
    fun activityTotalCountTextView_NotNull() {
        scenario.onActivity {
            val totalCountTextView: TextView =
                it.findViewById<TextView>(R.id.totalCountTextView)
            TestCase.assertNotNull(totalCountTextView)
        }
    }

    @Test // Проверка наличия элемента с id "recyclerView"
    fun activityRecyclerView_NotNull() {
        scenario.onActivity {
            val recyclerView: RecyclerView =
                it.findViewById<RecyclerView>(R.id.recyclerView)
            TestCase.assertNotNull(recyclerView)
        }
    }

    @Test // Проверка корректности работы поискового запроса
    fun activitySearch_IsSearchWorking() {
        Espresso.onView(withId(R.id.searchEditText)).perform(click())
        Espresso.onView(withId(R.id.searchEditText))
            .perform(replaceText(SAMPLE_REPOSITORY_NAME_TEXT))
        Espresso.onView(withId(R.id.toSearchActivityButton))
            .perform(click())
        Espresso.onView(withId(R.id.totalCountTextView)).
        check(matches(withText(RESULT_SAMPLE_FAKE_REPOSITORY_TEXT)))
    }

    @Test // Проверка частичного отображения элемента с id "searchEditText"
    fun activityIsEditText_Displayed() {
        Espresso.onView(withId(R.id.searchEditText)).check(matches(isDisplayed()))
    }

    @Test // Проверка полного отображения элемента с id "searchEditText"
    fun activityIsEditText_CompletelyDisplayed() {
        Espresso.onView(withId(R.id.searchEditText)).check(matches(isCompletelyDisplayed()))
    }

    // Проверка свойства видимости элементов с id "progressBar", "searchEditText",
    // "toDetailsActivityButton", "totalCountTextView", "recyclerView"
    @Test
    fun activityButtons_AreEffectiveVisible() {
        Espresso.onView(withId(R.id.progressBar))
            .check(ViewAssertions.matches(ViewMatchers
                .withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        Espresso.onView(withId(R.id.searchEditText))
            .check(ViewAssertions.matches(ViewMatchers
                .withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        Espresso.onView(withId(R.id.toDetailsActivityButton))
            .check(ViewAssertions.matches(ViewMatchers
                .withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        Espresso.onView(withId(R.id.totalCountTextView))
            .check(ViewAssertions.matches(ViewMatchers
                .withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)))
        Espresso.onView(withId(R.id.recyclerView))
            .check(ViewAssertions.matches(ViewMatchers
                .withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    @Test // Проверка отображения корректного текста на элементе с id "toDetailsActivityButton"
    fun activityEditText_IsCorrectText() {
        Espresso.onView(withId(R.id.searchEditText)).check(matches(withText("")))
    }

    // Проверка отображения корректного текста
    // с подсказкой на элементе с id "toDetailsActivityButton"
    @Test
    fun activityEditText_IsCorrectHintText() {
        Espresso.onView(withId(R.id.searchEditText)).check(matches(withHint(HINT_TEXT)))
    }


    @Test // Проверка отображения корректного текста на элементе с id "toDetailsActivityButton"
    fun activityButton_IsCorrectText() {
        onView(withId(R.id.toDetailsActivityButton)).check(matches(withText(TO_DETAILS_TEXT)))
    }

    @After // Установка действия после завершения выполнения всех тестов
    fun close() {
        scenario.close()
    }
}