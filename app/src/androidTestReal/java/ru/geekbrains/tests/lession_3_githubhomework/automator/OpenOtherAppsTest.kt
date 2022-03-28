package ru.geekbrains.tests.lession_3_githubhomework.automator

import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.*
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class OpenOtherAppsTest {

    /** Задание переменных */ //region
    private val uiDevice: UiDevice = UiDevice.getInstance(getInstrumentation())
    //endregion

    @Test // Открытие приложения с настройками на смартфоне
    fun test_OpenSettings() {
        // Нажимаем на системную кнопку Home()
        uiDevice.pressHome()
        // Реализация смахивания для открытия нижнего системного меню с приложениями
        uiDevice.swipe(500, 1500, 500, 0, 5)
        // Установка скроллинга приложений
        val appViews = UiScrollable(UiSelector().scrollable(true))
        // Находим в контейнере настройки по названию иконки "Settings"
        val settingsApp = appViews
            .getChildByText(
                UiSelector()
                    .className(TextView::class.java.name),
                "Settings")
        // Открываем приложение "Настройки"
        settingsApp.clickAndWaitForNewWindow()
        // Убеждаемся, что приложение "Настройки" открыты
        val settingsValidation =
            uiDevice.findObject(UiSelector().packageName("com.android.settings"))
        Assert.assertTrue(settingsValidation.exists())
    }

    @Test // Открытие приложение ScientificCalculator
    fun test_OpenScientificCalculator() {
        // Нажимаем на системную кнопку Home()
        uiDevice.pressHome()
        // Реализация смахивания для открытия нижнего системного меню с приложениями
        uiDevice.swipe(500, 1500, 500, 0, 5)
        // Установка скроллинга приложений
        val appViews = UiScrollable(UiSelector().scrollable(true))
        // Находим в контейнере настройки по названию иконки "Settings"
        val settingsApp = appViews
            .getChildByText(
                UiSelector()
                    .className(TextView::class.java.name),
                "Scientific calculator") // Название хранится в манифесте в поле android:label
        // Открываем приложение "Настройки"
        settingsApp.clickAndWaitForNewWindow()
        // Находим кнопку 1
        val buttonOne: UiObject = uiDevice.findObject(UiSelector().text("1"))
        //Кликаем по кнопке 1
        buttonOne.click()
    }
}