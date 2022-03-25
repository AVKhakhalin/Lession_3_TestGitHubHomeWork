package ru.geekbrains.tests.lession_3_githubhomework.presenter.details

import ru.geekbrains.tests.lession_3_githubhomework.presenter.PresenterContract

internal interface PresenterDetailsContract: PresenterContract {
    fun setCounter(count: Int)
    fun onIncrement()
    fun onDecrement()
}
