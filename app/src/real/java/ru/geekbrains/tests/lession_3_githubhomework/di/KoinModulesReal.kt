package ru.geekbrains.tests.lession_3_githubhomework.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.geekbrains.tests.lession_3_githubhomework.presenter.RepositoryContract
import ru.geekbrains.tests.lession_3_githubhomework.repository.GitHubApi
import ru.geekbrains.tests.lession_3_githubhomework.repository.GitHubRepository
import ru.geekbrains.tests.lession_3_githubhomework.view.search.MainActivity.MainActivity.Companion.BASE_URL

val application = module {
    single<RepositoryContract> {
        GitHubRepository(
            // Retrofit
            Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                .create(GitHubApi::class.java)
        )
    }
}