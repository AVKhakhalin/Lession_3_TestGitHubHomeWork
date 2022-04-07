package ru.geekbrains.tests.lession_3_githubhomework.view.search

import ru.geekbrains.tests.lession_3_githubhomework.model.SearchResponse

sealed class ScreenState {
    object Loading : ScreenState()
    data class Working(val searchResponse: SearchResponse) : ScreenState()
    data class Error(val error: Throwable) : ScreenState()
}