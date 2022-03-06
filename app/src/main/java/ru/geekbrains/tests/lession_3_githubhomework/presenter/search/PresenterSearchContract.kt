package ru.geekbrains.tests.lession_3_githubhomework.presenter.search

import ru.geekbrains.tests.lession_3_githubhomework.presenter.PresenterContract

internal interface PresenterSearchContract: PresenterContract {
    fun searchGitHub(searchQuery: String)
    //onAttach
    // Добавлять сюда метод onAttach не вижу смысла,
    // потому что он добавлен в интерфейс PresenterContract по условию 1 пункта ДЗ
    //onDetach
    // Добавлять сюда метод onDetach не вижу смысла,
    // потому что он добавлен в интерфейс PresenterContract по условию 1 пункта ДЗ
}
