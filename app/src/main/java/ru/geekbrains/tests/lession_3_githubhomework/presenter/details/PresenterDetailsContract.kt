package ru.geekbrains.tests.lession_3_githubhomework.presenter.details

import ru.geekbrains.tests.lession_3_githubhomework.presenter.PresenterContract
import ru.geekbrains.tests.lession_3_githubhomework.view.ViewContract

internal interface PresenterDetailsContract: PresenterContract {
    fun setCounter(count: Int)
    fun onIncrement()
    fun onDecrement()
}
