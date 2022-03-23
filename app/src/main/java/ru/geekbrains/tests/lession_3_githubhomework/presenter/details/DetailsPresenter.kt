package ru.geekbrains.tests.lession_3_githubhomework.presenter.details

import ru.geekbrains.tests.lession_3_githubhomework.view.ViewContract
import ru.geekbrains.tests.lession_3_githubhomework.view.details.ViewDetailsContract

internal class DetailsPresenter internal constructor(
    private var count: Int = 0
): PresenterDetailsContract {
    /** Задание переменных */ //region
    private var viewContract: ViewDetailsContract? = null
    //endregion

    override fun onAttach(viewContract: ViewContract) {
        this.viewContract = viewContract as ViewDetailsContract
    }

    override fun onDetach() {
        viewContract = null
    }

    override fun setCounter(count: Int) {
        this.count = count
    }

    override fun onIncrement() {
        count++
        viewContract?.let { viewContract ->
            viewContract.setCount(count)
        }
    }

    override fun onDecrement() {
        count--
        viewContract?.let { viewContract ->
            viewContract.setCount(count)
        }
    }
}
