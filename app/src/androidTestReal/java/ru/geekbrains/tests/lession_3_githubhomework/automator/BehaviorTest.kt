package ru.geekbrains.tests.lession_3_githubhomework.automator

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class BehaviorTest {

    //Класс UiDevice предоставляет доступ к вашему устройству.
    //Именно через UiDevice вы можете управлять устройством, открывать приложения
    //и находить нужные элементы на экране
    private val uiDevice: UiDevice = UiDevice.getInstance(getInstrumentation())

    //Контекст нам понадобится для запуска нужных экранов и получения packageName
    private val context = ApplicationProvider.getApplicationContext<Context>()

    //Путь к классам нашего приложения, которые мы будем тестировать
    private val packageName = context.packageName

    @Before
    fun setup() {
        //Для начала сворачиваем все приложения, если у нас что-то запущено
        uiDevice.pressHome()

        //Запускаем наше приложение
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        //Мы уже проверяли Интент на null в предыдущем тесте,
        // поэтому допускаем, что Интент у нас не null
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)  // Чистим бэкстек
                                                            // от запущенных ранее Активити
        context.startActivity(intent)

        //Ждем, когда приложение откроется на смартфоне чтобы начать тестировать его элементы
        uiDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), TIMEOUT)
    }

    //Убеждаемся, что приложение открыто. Для этого достаточно найти на экране любой элемент
    //и проверить его на null
    @Test
    fun test_MainActivityIsStarted() {
        //Через uiDevice находим editText
        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
        //Проверяем на null
        Assert.assertNotNull(editText)
    }

    @Test //Убеждаемся, что поиск работает как ожидается
    fun test_SearchIsPositive() {
        //Через uiDevice находим editText
        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
        //Устанавливаем значение
        editText.text = "UiAutomator"
        //Отправляем запрос через UiAutomator
            // Пишем заглавные буквы, потому что у кнопки по-умолчанию
            // стоит атрибут android:textAllCaps="true"
//        uiDevice.findObject(UiSelector().textMatches("ПОИСК РЕПОЗИТОРИЕВ")).click()
            //Находим кнопку с запуском поиска информации
        val toDetails: UiObject2 = uiDevice.findObject(
            By.res(
                packageName,
                "toSearchActivityButton"
            )
        )
            //Кликаем по кнопке поиска информации
        toDetails.click()

        //Ожидаем конкретного события: появления текстового поля totalCountTextView.
        //Это будет означать, что сервер вернул ответ с какими-то данными, то есть запрос отработал.
        val changedText =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountTextView")),
                TIMEOUT
            )
        //Убеждаемся, что сервер вернул корректный результат. Обратите внимание, что количество
        //результатов может варьироваться во времени,
        // потому что количество репозиториев постоянно меняется.
        Assert.assertEquals(changedText.text.toString(), "Number of results: 701")
    }

    @Test //Убеждаемся, что DetailsScreen открывается
    fun test_OpenDetailsScreen() {
        //Находим кнопку
        val toDetails: UiObject2 = uiDevice.findObject(
            By.res(
                packageName,
                "toDetailsActivityButton"
            )
        )
        //Кликаем по ней
        toDetails.click()

        //Ожидаем конкретного события: появления текстового поля totalCountTextView.
        //Это будет означать, что DetailsScreen открылся и это поле видно на экране.
        val changedText =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountTextView")),
                TIMEOUT
            )
        //Убеждаемся, что поле видно и содержит предполагаемый текст.
        //Обратите внимание, что текст должен быть "Number of results: 0",
        //так как мы кликаем по кнопке не отправляя никаких поисковых запросов.
        //Чтобы проверить отображение определенного количества репозиториев,
        //вам в одном и том же методе нужно отправить запрос на сервер и открыть DetailsScreen.
        Assert.assertEquals(changedText.text, "Number of results: 0")
    }

    // Проверяем отображения одинакового количества репозиториев
    // в MainActivity и в DetailsActivity
    @Test
    fun test_SimilarResultMainActivityAndDetailsActivity() {
        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
        //Устанавливаем значение
        editText.text = "avkhakhalin"
        //Отправляем запрос через UiAutomator
        //Находим кнопку с запуском поиска информации
        var toDetails: UiObject2 = uiDevice.findObject(
            By.res(
                packageName,
                "toSearchActivityButton"
            )
        )
        //Кликаем по кнопке поиска информации
        toDetails.click()
        // Сохраняем информацию о количестве запрошенных репозиториев
        val mainActivityChangedText =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountTextView")),
                TIMEOUT
            )
        val mainActivityResult: String = mainActivityChangedText.text

        //Находим кнопку для перехода на DetailsActivity
        toDetails = uiDevice.findObject(
            By.res(
                packageName,
                "toDetailsActivityButton"
            )
        )
        //Кликаем по ней
        toDetails.click()
        // Получаем информацию с текстового поля DetailsActivity
        val detailsActivityChangedText =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountTextView")),
                TIMEOUT
            )
        val detailsActivityResult: String = detailsActivityChangedText.text
        // Получение результата сравнения двух строк
        val result: Long = mainActivityResult.indexOf(detailsActivityResult).toLong()

        // Оцениваем полученный результат сравнения двух строк
        Assert.assertTrue(result == 0L)
    }

    companion object {
        private const val TIMEOUT = 5000L
    }
}