package ru.geekbrains.tests.lession_3_githubhomework.repository

import retrofit2.Response
import ru.geekbrains.tests.lession_3_githubhomework.model.SearchResponse

interface RepositoryCallback {
    fun handleGitHubResponse(response: Response<SearchResponse?>?)
    fun handleGitHubError()
}