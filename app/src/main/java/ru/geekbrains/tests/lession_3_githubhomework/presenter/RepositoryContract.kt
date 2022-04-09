package ru.geekbrains.tests.lession_3_githubhomework.presenter

import io.reactivex.Observable
import ru.geekbrains.tests.lession_3_githubhomework.model.SearchResponse
import ru.geekbrains.tests.lession_3_githubhomework.repository.RepositoryCallback

interface RepositoryContract {
    fun searchGithub(
        query: String,
        callback: RepositoryCallback
    )

    fun searchGithub(
        query: String
    ): Observable<SearchResponse>

    suspend fun searchGithubAsync(
        query: String
    ): SearchResponse
}