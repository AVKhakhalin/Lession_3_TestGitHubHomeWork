package ru.geekbrains.tests.lession_3_githubhomework.presenter

import ru.geekbrains.tests.lession_3_githubhomework.repository.RepositoryCallback

internal interface RepositoryContract {
    fun searchGithub(
        query: String,
        callback: RepositoryCallback
    )
}