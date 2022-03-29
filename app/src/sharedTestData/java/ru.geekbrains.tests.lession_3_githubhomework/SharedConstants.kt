import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher

/** Константы для тестов */ //region
internal const val MIN_SDK_VALUE = 18
internal const val SAMPLE_REPOSITORY_NAME_TEXT: String = "algol"
internal const val RESULT_SAMPLE_FAKE_REPOSITORY_TEXT: String = "Number of results: 42"
internal const val RESULT_SAMPLE_REPOSITORY_TEXT: String = "Number of results: 2961"
internal const val RESULT_ZERO_REPOSITORY_TEXT: String = "Number of results: 0"
internal const val RESULT_ONE_REPOSITORY_TEXT: String = "Number of results: 1"
internal const val RESULT_MINUS_ONE_TEXT: String = "Number of results: -1"
internal const val RESULT_UIAUTOMATOR_REPOSITORY_TEXT: String = "Number of results: 701"
internal const val SEARCH_REPOSITORIES_TEXT: String = "ПОИСК РЕПОЗИТОРИЕВ"
internal const val DELAY_TIME: Long = 5000
internal const val EMPTY_TEXT: String = ""
internal const val HINT_TEXT: String = "Enter keyword e.g. android"
internal const val TO_DETAILS_TEXT: String = "ДЕТАЛИЗАЦИЯ ЗАПРОСА"
internal const val TO_DETAILS_SMALL_TEXT: String = "Детализация запроса"
internal const val WAITING_DURING_TEXT: String = "Ожидание в течение"
internal const val SECOND_TEXT: String = "секунд"
internal const val SOME_QUERY_TEXT: String = "some query"
internal const val SCIENTIFIC_CALCULATOR_TEXT: String = "Scientific calculator"
internal const val SETTINGS_TEXT: String = "Settings"
internal const val SETTINGS_PACKAGE_NAME_TEXT: String = "com.android.settings"
internal const val SEARCH_EDIT_TEXT_TEXT: String = "searchEditText"
internal const val MY_REPO_NAME_TEXT: String = "avkhakhalin"
internal const val MY_REPO_RESULT_NAME_TEXT: String = "AVKhakhalin/AVKhakhalin"
internal const val UI_AUTOMATOR_TEXT: String = "UiAutomator"
internal const val TO_DETAILS_ACTIVITY_BUTTON_TEXT: String = "toDetailsActivityButton"
internal const val TOTAL_COUNT_TEXT_VIEW_TEXT: String = "totalCountTextView"
internal const val TO_SEARCH_ACTIVITY_BUTTON_TEXT: String = "toSearchActivityButton"
internal const val DETAILED_QUERY_TEXT: String = "ДЕТАЛИЗАЦИЯ ЗАПРОСА"
internal const val DECREMENT_BUTTON_TEXT: String = "decrementButton"
internal const val INCREMENT_BUTTON_TEXT: String = "incrementButton"
internal const val ONE_TEXT: String = "1"
internal const val ZERO_INT_VALUE: Int = 0
internal const val ONE_INT_VALUE: Int = 1
internal const val TWO_INT_VALUE: Int = 2
internal const val MINUS_ONE_INT_VALUE: Int = -1
internal const val START_X_INT_VALUE: Int = 500
internal const val START_Y_INT_VALUE: Int = 1500
internal const val END_X_INT_VALUE: Int = 500
internal const val END_Y_INT_VALUE: Int = 0
internal const val STEPS_INT_VALUE: Int = 5
//endregion

// Функция для реализации ожидания
internal fun delayTime(waitTime: Long): ViewAction? {
    return object: ViewAction {
        override fun getConstraints(): Matcher<View> = ViewMatchers.isRoot()
        override fun getDescription(): String = "$WAITING_DURING_TEXT $waitTime $SECOND_TEXT"
        override fun perform(uiController: UiController, v: View?) {
            uiController.loopMainThreadForAtLeast(waitTime)
        }
    }
}