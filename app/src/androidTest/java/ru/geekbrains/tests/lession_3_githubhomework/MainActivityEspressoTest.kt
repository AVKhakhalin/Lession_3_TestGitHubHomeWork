package ru.geekbrains.tests.lession_3_githubhomework

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import junit.framework.TestCase
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.geekbrains.tests.lession_3_githubhomework.view.search.MainActivity

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
//@RunWith(AndroidJUnit4ClassRunner::class)
@RunWith(AndroidJUnit4::class)
class MainActivityEspressoTest {

    /** Задание переменных */ //region
    private lateinit var scenario: ActivityScenario<MainActivity>
    //endregion

    @Before // Установка действия до начала выполнения всех тестов
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
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
        onView(withId(R.id.searchEditText)).perform(click())
        onView(withId(R.id.searchEditText)).perform(replaceText("algol"),
            closeSoftKeyboard())
        onView(withId(R.id.searchEditText)).perform(pressImeActionButton())
        if (BuildConfig.TYPE == MainActivity.FAKE) {
            onView(withId(R.id.totalCountTextView)).
                check(matches(withText("Number of results: 42")))
        } else {
            onView(isRoot()).perform(delay(2000))
            onView(withId(R.id.totalCountTextView)).
                check(matches(withText("Number of results: 2283")))
        }
    }

    @Test // Проверка частичного отображения элемента с id "searchEditText"
    fun activityIsEditText_Displayed() {
        onView(withId(R.id.searchEditText)).check(matches(isDisplayed()))
    }

    @Test // Проверка полного отображения элемента с id "searchEditText"
    fun activityIsEditText_CompletelyDisplayed() {
        onView(withId(R.id.searchEditText)).check(matches(isCompletelyDisplayed()))
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
        onView(withId(R.id.searchEditText)).check(matches(withText("")))
    }

    // Проверка отображения корректного текста
    // с подсказкой на элементе с id "toDetailsActivityButton"
    @Test
    fun activityEditText_IsCorrectHintText() {
        onView(withId(R.id.searchEditText))
            .check(matches(withHint("Enter keyword e.g. android")))
    }


    @Test // Проверка отображения корректного текста на элементе с id "toDetailsActivityButton"
    fun activityButton_IsCorrectText() {
        onView(withId(R.id.toDetailsActivityButton)).check(matches(withText("to details")))
    }

    // Функция для реализации ожидания
    private fun delay(waitTime: Long): ViewAction? {
        return object: ViewAction {
            override fun getConstraints(): Matcher<View> = isRoot()
            override fun getDescription(): String = "Ожидание в течение $waitTime секунд"
            override fun perform(uiController: UiController, v: View?) {
                uiController.loopMainThreadForAtLeast(waitTime)
            }
        }
    }

    @After // Установка действия после завершения выполнения всех тестов
    fun close() {
        scenario.close()
    }
}