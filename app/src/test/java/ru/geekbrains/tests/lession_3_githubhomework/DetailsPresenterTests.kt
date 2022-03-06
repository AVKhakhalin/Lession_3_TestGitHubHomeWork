package ru.geekbrains.tests.lession_3_githubhomework

import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import ru.geekbrains.tests.lession_3_githubhomework.presenter.details.DetailsPresenter
import ru.geekbrains.tests.lession_3_githubhomework.view.details.ViewDetailsContract

class DetailsPresenterTests {

    private lateinit var detailsPresenter: DetailsPresenter

    @Mock
    private lateinit var viewContract: ViewDetailsContract

    @Before
    fun setUp() {
        // Настройка аннотации "@Mock"
        MockitoAnnotations.initMocks(this)
        // Создаём класс DetailsPresenter
        detailsPresenter = DetailsPresenter(0)
        // Передаём в "DetailsPresenter" вью "ViewDetailsContract"
        detailsPresenter.onAttach(viewContract)
    }

    @Test // Проверка метода onDetach()
    fun onDetach() {
        detailsPresenter.onDetach()
        detailsPresenter.onIncrement()
        verify(viewContract, times(0)).setCount(1)
    }

    @Test // Проверка метода onAttach()
    fun onAttach() {
        detailsPresenter.onDetach()
        detailsPresenter.onAttach(viewContract)
        detailsPresenter.onIncrement()
        verify(viewContract, times(1)).setCount(1)
    }

    @Test // Проверка метода onIncrement()
    fun onIncrement_Test() {
        detailsPresenter.onIncrement()
        verify(viewContract, times(1)).setCount(1)
    }

    @Test // Проверка метода onDecrement()
    fun onDecrement_Test() {
        detailsPresenter.onDecrement()
        verify(viewContract, times(1)).setCount(-1)
    }

    @Test // Проверка метода setCounter()
    fun setCounter_Test() {
        detailsPresenter.setCounter(1)
        detailsPresenter.onIncrement()
        verify(viewContract, times(1)).setCount(2)
    }
}