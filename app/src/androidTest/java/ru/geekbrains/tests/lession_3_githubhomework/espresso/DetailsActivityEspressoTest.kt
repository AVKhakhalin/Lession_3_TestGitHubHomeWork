package ru.geekbrains.tests.lession_3_githubhomework.espresso

import RESULT_MINUS_ONE_TEXT
import RESULT_ONE_REPOSITORY_TEXT
import RESULT_ZERO_REPOSITORY_TEXT
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.geekbrains.tests.lession_3_githubhomework.R
import ru.geekbrains.tests.lession_3_githubhomework.view.details.DetailsActivity

//@RunWith(AndroidJUnit4ClassRunner::class)
@RunWith(AndroidJUnit4::class)
class DetailsActivityEspressoTest {

    /** Задание переменных */ //region
    private lateinit var scenario: ActivityScenario<DetailsActivity>
    //endregion

    @Before // Установка действия до начала выполнения всех тестов
    fun setup() {
        scenario = ActivityScenario.launch(DetailsActivity::class.java)
    }

    @Test // Проверка на существование активити
    fun activity_AssertNotNull() {
        scenario.onActivity {
            TestCase.assertNotNull(it)
        }
    }

    @Test // Проверка прохождения активити через метод onResume()
    fun activity_IsResumed() {
        TestCase.assertEquals(Lifecycle.State.RESUMED, scenario.state)
    }

    @Test // Проверка наличия элемента с id "totalCountTextView"
    fun activityTextView_NotNull() {
        scenario.onActivity {
            val totalCountTextView = it.findViewById<TextView>(R.id.totalCountTextView)
            TestCase.assertNotNull(totalCountTextView)
        }
    }

    @Test // Проверка на наличие определённого текста в элементе с id "totalCountTextView"
    fun activityTextView_HasText() {
        val assertion = ViewAssertions.matches(ViewMatchers.withText(RESULT_ZERO_REPOSITORY_TEXT))
        Espresso.onView(withId(R.id.totalCountTextView)).check(assertion)
    }

    @Test // Проверка частичного отображения элемента с id "totalCountTextView"
    fun activityTextView_IsDisplayed() {
        Espresso.onView(withId(R.id.totalCountTextView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test // Проверка полного отображения элемента с id "totalCountTextView"
    fun activityTextView_IsCompletelyDisplayed() {
        Espresso.onView(withId(R.id.totalCountTextView))
            .check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()))
    }

    // Проверка свойства видимости элементов с id "incrementButton",
    // "totalCountTextView", "decrementButton"
    @Test
    fun activityButtons_AreEffectiveVisible() {
        Espresso.onView(withId(R.id.incrementButton))
            .check(ViewAssertions.matches(ViewMatchers
                .withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        Espresso.onView(withId(R.id.totalCountTextView))
            .check(ViewAssertions.matches(ViewMatchers
                .withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        Espresso.onView(withId(R.id.decrementButton))
            .check(ViewAssertions.matches(ViewMatchers
                .withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    // Проверка результата нажатия на элемент с id "incrementButton"
    // и отображения текста на элементе с id "totalCountTextView"
    @Test
    fun activityButtonIncrement_IsWorking() {
        Espresso.onView(withId(R.id.incrementButton)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.totalCountTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText(RESULT_ONE_REPOSITORY_TEXT)))
    }

    // Проверка результата нажатия на элемент с id "decrementButton"
    // и отображения текста на элементе с id "totalCountTextView"
    @Test
    fun activityButtonDecrement_IsWorking() {
        Espresso.onView(withId(R.id.decrementButton)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.totalCountTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText(RESULT_MINUS_ONE_TEXT)))
    }

    @After // Установка действия после завершения выполнения всех тестов
    fun close() {
        scenario.close()
    }
}