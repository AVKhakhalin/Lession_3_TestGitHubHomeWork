package ru.geekbrains.tests.lession_3_githubhomework

import MINUS_ONE_INT_VALUE
import ONE_INT_VALUE
import TWO_INT_VALUE
import ZERO_INT_VALUE
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import ru.geekbrains.tests.lession_3_githubhomework.presenter.details.DetailsPresenter
import ru.geekbrains.tests.lession_3_githubhomework.view.details.ViewDetailsContract

class DetailsPresenterTests {
    /** Задание переменных */ //region
    private lateinit var detailsPresenter: DetailsPresenter
    @Mock
    private lateinit var viewContract: ViewDetailsContract
    //endregion

    @Before
    fun setUp() {
        // Настройка аннотации "@Mock"
        MockitoAnnotations.initMocks(this)
        // Создаём класс DetailsPresenter
        detailsPresenter = DetailsPresenter(ZERO_INT_VALUE)
        // Передаём в "DetailsPresenter" вью "ViewDetailsContract"
        detailsPresenter.onAttach(viewContract)
    }

    @Test // Проверка метода onDetach()
    fun onDetach() {
        detailsPresenter.onDetach()
        detailsPresenter.onIncrement()
        verify(viewContract, times(ZERO_INT_VALUE)).setCount(ONE_INT_VALUE)
    }

    @Test // Проверка метода onAttach()
    fun onAttach() {
        detailsPresenter.onDetach()
        detailsPresenter.onAttach(viewContract)
        detailsPresenter.onIncrement()
        verify(viewContract, times(ONE_INT_VALUE)).setCount(ONE_INT_VALUE)
    }

    @Test // Проверка метода onIncrement()
    fun onIncrement_Test() {
        detailsPresenter.onIncrement()
        verify(viewContract, times(ONE_INT_VALUE)).setCount(ONE_INT_VALUE)
    }

    @Test // Проверка метода onDecrement()
    fun onDecrement_Test() {
        detailsPresenter.onDecrement()
        verify(viewContract, times(ONE_INT_VALUE)).setCount(MINUS_ONE_INT_VALUE)
    }

    @Test // Проверка метода setCounter()
    fun setCounter_Test() {
        detailsPresenter.setCounter(ONE_INT_VALUE)
        detailsPresenter.onIncrement()
        verify(viewContract, times(ONE_INT_VALUE)).setCount(TWO_INT_VALUE)
    }
}