package ru.geekbrains.tests.lession_3_githubhomework.automator

import END_X_INT_VALUE
import END_Y_INT_VALUE
import MIN_SDK_VALUE
import ONE_TEXT
import SCIENTIFIC_CALCULATOR_TEXT
import SETTINGS_PACKAGE_NAME_TEXT
import SETTINGS_TEXT
import START_X_INT_VALUE
import START_Y_INT_VALUE
import STEPS_INT_VALUE
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.*
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = MIN_SDK_VALUE)
class OpenOtherAppsTest {
    /** Задание переменных */ //region
    private val uiDevice: UiDevice = UiDevice.getInstance(getInstrumentation())
    //endregion

    @Test // Открытие приложения с настройками на смартфоне
    fun test_OpenSettings() {
        // Нажимаем на системную кнопку Home()
        uiDevice.pressHome()
        // Реализация смахивания для открытия нижнего системного меню с приложениями
        uiDevice.swipe(
            START_X_INT_VALUE, START_Y_INT_VALUE, END_X_INT_VALUE, END_Y_INT_VALUE, STEPS_INT_VALUE)
        // Установка скроллинга приложений
        val appViews = UiScrollable(UiSelector().scrollable(true))
        // Находим в контейнере настройки по названию иконки "Settings"
        val settingsApp = appViews
            .getChildByText(UiSelector().className(TextView::class.java.name), SETTINGS_TEXT)
        // Открываем приложение "Настройки"
        settingsApp.clickAndWaitForNewWindow()
        // Убеждаемся, что приложение "Настройки" открыты
        val settingsValidation =
            uiDevice.findObject(UiSelector().packageName(SETTINGS_PACKAGE_NAME_TEXT))
        Assert.assertTrue(settingsValidation.exists())
    }

    @Test // Открытие приложение ScientificCalculator
    fun test_OpenScientificCalculator() {
        // Нажимаем на системную кнопку Home()
        uiDevice.pressHome()
        // Реализация смахивания для открытия нижнего системного меню с приложениями
        uiDevice.swipe(
            START_X_INT_VALUE, START_Y_INT_VALUE, END_X_INT_VALUE, END_Y_INT_VALUE, STEPS_INT_VALUE)
        // Установка скроллинга приложений
        val appViews = UiScrollable(UiSelector().scrollable(true))
        // Находим в контейнере настройки по названию иконки "Settings"
        val settingsApp = appViews
            .getChildByText(
                UiSelector()
                    .className(TextView::class.java.name),
                SCIENTIFIC_CALCULATOR_TEXT) // Название хранится в манифесте в поле android:label
        // Открываем приложение "Настройки"
        settingsApp.clickAndWaitForNewWindow()
        // Находим кнопку 1
        val buttonOne: UiObject = uiDevice.findObject(UiSelector().text(ONE_TEXT))
        //Кликаем по кнопке 1
        buttonOne.click()
    }
}