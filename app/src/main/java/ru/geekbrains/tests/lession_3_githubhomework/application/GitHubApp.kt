package ru.geekbrains.tests.lession_3_githubhomework.application

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.geekbrains.tests.lession_3_githubhomework.di.application

class GitHubApp: Application() {
    /** Задание переменных */ //region
    companion object {
        lateinit var instance: GitHubApp
    }
    //endregion

    override fun onCreate() {
        super.onCreate()
        // Инициализация класса GitHubApp
        instance = this

        // Koin
        startKoin {
            androidContext(applicationContext)
            modules(listOf(application))
        }
    }
}