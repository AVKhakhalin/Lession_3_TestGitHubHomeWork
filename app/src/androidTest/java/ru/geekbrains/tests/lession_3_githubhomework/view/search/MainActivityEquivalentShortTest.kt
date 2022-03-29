package ru.geekbrains.tests.lession_3_githubhomework.view.search

import PLUS_TEXT
import RESULT_ONE_REPOSITORY_TEXT
import TO_DETAILS_TEXT
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityEquivalentShortTest {
    /** Задание переменных */ //region
    private lateinit var scenario: ActivityScenario<MainActivity.MainActivity>
    //endregion

    @Before // Установка действий до начала выполнения всех тестов
    fun setup () {
        scenario = ActivityScenario.launch(MainActivity.MainActivity::class.java)
    }

    @Test // Аналог теста "MainActivityTest"
    fun mainActivityTest() {
        var materialButton = Espresso.onView(ViewMatchers.withText(TO_DETAILS_TEXT))
        materialButton.perform(ViewActions.click())
        materialButton = Espresso.onView(ViewMatchers.withText(PLUS_TEXT))
        materialButton.perform(ViewActions.click())
        val textView = Espresso.onView(ViewMatchers.withText(RESULT_ONE_REPOSITORY_TEXT))
        textView.check(ViewAssertions.matches(ViewMatchers.withText(RESULT_ONE_REPOSITORY_TEXT)))
        val button = Espresso.onView(ViewMatchers.withText(PLUS_TEXT))
        button.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}