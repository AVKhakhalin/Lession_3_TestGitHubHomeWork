package ru.geekbrains.tests.lession_3_githubhomework.presenter

import ru.geekbrains.tests.lession_3_githubhomework.view.ViewContract

internal interface PresenterContract {
    fun onAttach(viewContract: ViewContract)
    fun onDetach()
}
