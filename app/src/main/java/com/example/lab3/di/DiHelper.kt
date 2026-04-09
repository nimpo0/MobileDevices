package com.example.lab3.di

import com.example.lab3.data.api.RetrofitApiHelper
import com.example.lab3.data.api.ScheduleApi
import com.example.lab3.data.api.ScheduleApiService
import com.example.lab3.data.source.IDataSource
import com.example.lab3.ui.MainContract
import com.example.lab3.ui.MainPresenter

class DiHelper {

    companion object {
        private var mainPresenter: MainContract.Presenter? = null
        private var service: IDataSource? = null
        private var retrofitHelper: RetrofitApiHelper? = null
        private var scheduleApi: ScheduleApi? = null

        fun getPresenter(view: MainContract.View): MainContract.Presenter {
            if (mainPresenter == null) {
                mainPresenter = MainPresenter(view)
            }
            return mainPresenter!!
        }

        fun getService(): IDataSource {
            if (service == null) {
                service = ScheduleApiService(getScheduleApi())
            }
            return service!!
        }

        fun getRetrofitHelper(): RetrofitApiHelper {
            if (retrofitHelper == null) {
                retrofitHelper = RetrofitApiHelper()
                retrofitHelper!!.init()
            }
            return retrofitHelper!!
        }

        fun getScheduleApi(): ScheduleApi {
            if (scheduleApi == null) {
                scheduleApi = getRetrofitHelper()
                    .getRetrofit()
                    .create(ScheduleApi::class.java)
            }
            return scheduleApi!!
        }
    }
}