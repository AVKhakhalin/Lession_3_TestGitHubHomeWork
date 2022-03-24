package ru.geekbrains.tests.lession_3_githubhomework.di

import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.geekbrains.tests.lession_3_githubhomework.presenter.RepositoryContract
import ru.geekbrains.tests.lession_3_githubhomework.repository.GitHubApi
import ru.geekbrains.tests.lession_3_githubhomework.repository.GitHubRepository
import ru.geekbrains.tests.lession_3_githubhomework.view.search.MainActivityReal.MainActivity.Companion.BASE_URL

val application = module {
    single<RepositoryContract> {
        GitHubRepository(
            // Retrofit
            Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                .create(GitHubApi::class.java)
        )
    }
}