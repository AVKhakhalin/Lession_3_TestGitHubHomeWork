package ru.geekbrains.tests.lession_3_githubhomework.presenter.search

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.geekbrains.tests.lession_3_githubhomework.presenter.SchedulerProvider

internal class SearchSchedulerProvider: SchedulerProvider {
    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun io(): Scheduler {
        return Schedulers.io()
    }
}