package ru.geekbrains.tests.lession_3_githubhomework.automator

import android.content.Context
import android.content.Intent
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
    /** Задание переменных */ //region
    //Класс UiDevice предоставляет доступ к вашему устройству.
    //Именно через UiDevice вы можете управлять устройством, открывать приложения
    //и находить нужные элементы на экране
    private val uiDevice: UiDevice = UiDevice.getInstance(getInstrumentation())

    //Контекст нам понадобится для запуска нужных экранов и получения packageName
    private val context = ApplicationProvider.getApplicationContext<Context>()

    //Путь к классам нашего приложения, которые мы будем тестировать
    private val packageName = context.packageName
    //endregion

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
        Assert.assertNull(uiDevice.findObject(By.res(packageName, "searchEditText1")))
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
            By.res(packageName,"toSearchActivityButton"))
            //Кликаем по кнопке поиска информации
        toDetails.click()

        //Ожидаем конкретного события: появления текстового поля totalCountTextView.
        //Это будет означать, что сервер вернул ответ с какими-то данными, то есть запрос отработал.
        val changedText = uiDevice.wait(
            Until.findObject(By.res(packageName, "totalCountTextView")), TIMEOUT)
        //Убеждаемся, что сервер вернул корректный результат. Обратите внимание, что количество
        //результатов может варьироваться во времени,
        // потому что количество репозиториев постоянно меняется.
        Assert.assertEquals(changedText.text.toString(), "Number of results: 701")
        Assert.assertNotEquals(changedText.text.toString(), "Number of results: 700")
    }

    @Test //Убеждаемся, что DetailsScreen открывается
    fun test_OpenDetailsScreen() {
        //Находим кнопку
        val toDetails: UiObject2 = uiDevice.findObject(
            By.res(packageName,"toDetailsActivityButton"))
        //Кликаем по ней
        toDetails.click()

        //Ожидаем конкретного события: появления текстового поля totalCountTextView.
        //Это будет означать, что DetailsScreen открылся и это поле видно на экране.
        val changedText = uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountTextView")), TIMEOUT)
        //Убеждаемся, что поле видно и содержит предполагаемый текст.
        //Обратите внимание, что текст должен быть "Number of results: 0",
        //так как мы кликаем по кнопке не отправляя никаких поисковых запросов.
        //Чтобы проверить отображение определенного количества репозиториев,
        //вам в одном и том же методе нужно отправить запрос на сервер и открыть DetailsScreen.
        Assert.assertEquals(changedText.text, "Number of results: 0")
        Assert.assertNotEquals(changedText.text, "Number of results: 1")
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
        var toDetails: UiObject2 = uiDevice.wait(Until.findObject(
            By.res(packageName,"toSearchActivityButton")), TIMEOUT)
        //Кликаем по кнопке поиска информации
        toDetails.click()
        // Сохраняем информацию о количестве запрошенных репозиториев
        val mainActivityChangedText = uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountTextView")), TIMEOUT)
        // Проверяем, что отображается корректный результат в MainActivity
        Assert.assertNotNull(uiDevice
            .wait(Until.findObject(By.text("Number of results: 1")), TIMEOUT))
        Assert.assertNull(uiDevice
            .wait(Until.findObject(By.text("Number of results: 0")), TIMEOUT))
//        val mainActivityResult: String = mainActivityChangedText.text.toString()

        //Находим кнопку для перехода на DetailsActivity
        toDetails = uiDevice.wait(Until.findObject(
            By.res(packageName,"toDetailsActivityButton")), TIMEOUT)
        //Кликаем по ней
        toDetails.click()
        // Получаем информацию с текстового поля DetailsActivity
        val detailsActivityChangedText = uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountTextView")), TIMEOUT)
        // Проверяем, что отображается корректный результат в DetailsActivity
        Assert.assertNotNull(uiDevice
            .wait(Until.findObject(By.text("Number of results: 1")), TIMEOUT))
        Assert.assertNull(uiDevice
            .wait(Until.findObject(By.text("Number of results: 0")), TIMEOUT))

//        val detailsActivityResult: String = detailsActivityChangedText.text.toString()
        // Получение результата сравнения двух строк
//        val result: Long = mainActivityResult.indexOf(detailsActivityResult).toLong()

        // Оцениваем полученный результат сравнения двух строк
//        Assert.assertTrue(result > -1L)

    }

    @Test // Проверка корректности работы системной кнопки back()
    fun activity_CheckWorkBackButton() {
        //Находим кнопку для перехода на DetailsActivity
        val toDetails = uiDevice.findObject(By.res(packageName,"toDetailsActivityButton"))
        //Кликаем по ней
        toDetails.click()
        // Проверяем, что отображается DetailsActivity
        Assert.assertNotNull(uiDevice
            .wait(Until.findObject(By.text("Number of results: 0")), TIMEOUT))
        Assert.assertNull(uiDevice
            .wait(Until.findObject(By.text("Number of results: 1")), TIMEOUT))
        // Кликаем на системную кнопку back()
        uiDevice.pressBack()
        // Проверяем, что отображается MainActivity
        Assert.assertNotNull(uiDevice.findObject(By.text("ПОИСК РЕПОЗИТОРИЕВ")))
        Assert.assertNull(uiDevice.findObject(By.text("ПОИСК РЕПОЗИТОРИЕВ1")))
    }

    @Test // Проверка наличия элементов на экране MainActivity
    fun activity_ExistsElementsOnMainActivity() {
        // Проверяем, что отображается элемент с текстом подсказки
        Assert.assertNotNull(uiDevice.findObject(By.text("Enter keyword e.g. android")))
        Assert.assertNull(uiDevice.findObject(By.text("Enter keyword e.g. android1")))
        // Проверяем, что отображается кнопка с поиском репозиториев
        Assert.assertNotNull(uiDevice.findObject(By.text("ПОИСК РЕПОЗИТОРИЕВ")))
        Assert.assertNull(uiDevice.findObject(By.text("ПОИСК РЕПОЗИТОРИЕВ1")))
        // Проверяем, что отображается кнопка с детализацией запроса
        Assert.assertNotNull(uiDevice.findObject(By.text("ДЕТАЛИЗАЦИЯ ЗАПРОСА")))
        Assert.assertNull(uiDevice.findObject(By.text("ДЕТАЛИЗАЦИЯ ЗАПРОСА1")))
    }

    @Test // Проверка наличия элементов на экране DetailsActivity
    fun activity_ExistsElementsOnDetailsActivity() {
        //Находим кнопку для перехода на DetailsActivity
        val toDetails = uiDevice.wait(Until.findObject(
            By.res(packageName,"toDetailsActivityButton")), TIMEOUT)
        //Кликаем по ней
        toDetails.click()
        // Проверяем, что отображается элемент с текстом подсказки
        Assert.assertNotNull(uiDevice.wait(
            Until.findObject(By.res(packageName, "decrementButton")), TIMEOUT))
        Assert.assertNull(uiDevice.wait(
            Until.findObject(By.res(packageName, "decrementButton1")), TIMEOUT))
        // Проверяем, что отображается кнопка с поиском репозиториев
        Assert.assertNotNull(uiDevice.findObject(By.text("Number of results: 0")))
        Assert.assertNull(uiDevice.findObject(By.text("Number of results: 1")))
        // Проверяем, что отображается кнопка с детализацией запроса
        Assert.assertNotNull(uiDevice.wait(
            Until.findObject(By.res(packageName, "incrementButton")), TIMEOUT))
        Assert.assertNull(uiDevice.wait(
            Until.findObject(By.res(packageName, "incrementButton1")), TIMEOUT))
    }

    @Test // Проверка функционала кнопок на DetailsScreen
    fun activity_DetailsScreenButtonsIsWorking() {
        //Находим кнопку для перехода на DetailsActivity
        val toDetails = uiDevice.wait(Until.findObject(
            By.res(packageName,"toDetailsActivityButton")), TIMEOUT)
        //Кликаем по ней
        toDetails.click()
        //Находим кнопку для декремента
        val decrementButton = uiDevice.wait(Until.findObject(
            By.res(packageName,"decrementButton")), TIMEOUT)
        //Находим кнопку для инкремента
        val incrementButton = uiDevice.wait(Until.findObject(
            By.res(packageName,"incrementButton")), TIMEOUT)
        //Кликаем по кнопке с инкрементом
        incrementButton.click()
        // Проверяем изменение значения текстового поля
        Assert.assertNotNull(uiDevice.findObject(By.text("Number of results: 1")))
        Assert.assertNull(uiDevice.findObject(By.text("Number of results: 0")))
        //Кликаем по кнопке с декрементом
        decrementButton.click()
        // Проверяем изменение значения текстового поля
        Assert.assertNotNull(uiDevice.findObject(By.text("Number of results: 0")))
        Assert.assertNull(uiDevice.findObject(By.text("Number of results: 1")))
    }

    @Test // Проверка ввода текста в текстовое поле на MainActivity
    fun activity_SuccessInsertText() {
        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
        //Устанавливаем значение
        editText.text = "avkhakhalin"
        // Проверяем, что отображается элемент с текстом подсказки
        Assert.assertNotNull(uiDevice.findObject(By.text("avkhakhalin")))
        // Проверяем, что отображается элемент с текстом подсказки
        Assert.assertNull(uiDevice.findObject(By.text("avkhakhalin1")))
    }

    @Test // Проверка отображения на экране названия найденного репозитория avkhakhalin
    fun activity_CorrectShowRepositoryName() {
        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
        //Устанавливаем значение
        editText.text = "avkhakhalin"
        //Отправляем запрос через UiAutomator
        //Находим кнопку с запуском поиска информации
        var toDetails: UiObject2 = uiDevice.wait(Until.findObject(
            By.res(packageName,"toSearchActivityButton")), TIMEOUT)
        //Кликаем по кнопке поиска информации
        toDetails.click()
        // Сбрасываем текстовое значение в поисковой строке
        editText.text = ""
        // Проверяем, что отображается элемент списка с названием запрошенного репозитория
        Assert.assertNotNull(uiDevice.wait(Until.findObject(By.text("AVKhakhalin/AVKhakhalin")), TIMEOUT))
        // Проверяем, что отображается элемент списка с названием запрошенного репозитория
        Assert.assertNull(uiDevice.wait(Until.findObject(By.text("avkhakhalin")), TIMEOUT))
    }

    companion object {
        private const val TIMEOUT = 5000L
    }
}