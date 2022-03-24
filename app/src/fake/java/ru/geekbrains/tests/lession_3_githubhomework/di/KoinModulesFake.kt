package ru.geekbrains.tests.lession_3_githubhomework.di

import org.koin.dsl.module
import ru.geekbrains.tests.lession_3_githubhomework.presenter.RepositoryContract
import ru.geekbrains.tests.lession_3_githubhomework.repository.FakeGitHubRepository

val application = module {
    single<RepositoryContract> {
        FakeGitHubRepository()
    }
}