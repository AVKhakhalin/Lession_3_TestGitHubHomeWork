package ru.geekbrains.tests.lession_3_githubhomework

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import ru.geekbrains.tests.lession_3_githubhomework.presenter.SchedulerProvider

class ScheduleProviderStub: SchedulerProvider {
    override fun ui(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun io(): Scheduler {
        return Schedulers.trampoline()
    }
}