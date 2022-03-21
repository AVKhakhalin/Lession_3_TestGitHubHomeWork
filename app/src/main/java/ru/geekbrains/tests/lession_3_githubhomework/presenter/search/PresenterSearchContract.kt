package ru.geekbrains.tests.lession_3_githubhomework.presenter.search

import ru.geekbrains.tests.lession_3_githubhomework.presenter.PresenterContract

internal interface PresenterSearchContract: PresenterContract {
    fun searchGitHub(searchQuery: String)
}
