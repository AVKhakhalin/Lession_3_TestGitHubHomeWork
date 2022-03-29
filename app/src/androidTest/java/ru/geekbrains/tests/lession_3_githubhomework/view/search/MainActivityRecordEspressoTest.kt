package ru.geekbrains.tests.lession_3_githubhomework.view.search

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.geekbrains.tests.lession_3_githubhomework.R

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityRecordEspressoTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity.MainActivity::class.java)

    @Test
    fun mainActivityTest() {
        val materialButton = Espresso.onView(
            Matchers.allOf(
                withId(R.id.toDetailsActivityButton), ViewMatchers.withText("Детализация запроса"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.content),
                        0
                    ),
                    3
                ),
                ViewMatchers.isDisplayed()
            )
        )

        val materialButton2 = Espresso.onView(
            Matchers.allOf(
                withId(R.id.incrementButton), ViewMatchers.withText("+"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.content),
                        0
                    ),
                    2
                ),
                ViewMatchers.isDisplayed()
            )
        )
        materialButton2.perform(ViewActions.click())

        val textView = Espresso.onView(
            Matchers.allOf(
                withId(R.id.totalCountTextView), ViewMatchers.withText("Number of results: 1"),
                ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.content))),
                ViewMatchers.isDisplayed()
            )
        )

        textView.check(ViewAssertions.matches(ViewMatchers.withText("Number of results: 1")))

        val button = Espresso.onView(
            Matchers.allOf(
                withId(R.id.incrementButton), ViewMatchers.withText("+"),
                ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.content))),
                ViewMatchers.isDisplayed()
            )
        )
        button.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}