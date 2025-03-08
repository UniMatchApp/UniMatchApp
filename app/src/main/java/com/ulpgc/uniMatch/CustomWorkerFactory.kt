package com.ulpgc.uniMatch

import android.content.Context
import androidx.work.DelegatingWorkerFactory
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.infrastructure.services.notification.ProfileWork
import javax.inject.Inject
import javax.inject.Singleton

class ProfileWorkerFactory @Inject constructor(
    private val profileService: ProfileService
) : WorkerFactory() {
    override fun createWorker(appContext: Context, workerClassName: String, workerParameters: WorkerParameters): ListenableWorker? {
        return when (workerClassName) {
            ProfileWork::class.java.name -> ProfileWork(appContext, workerParameters, profileService)
            else -> null
        }
    }
}

@Singleton
class CustomWorkerFactory @Inject constructor(
    profileWorkerFactory: ProfileWorkerFactory
) : DelegatingWorkerFactory() {
    init {
        addFactory(profileWorkerFactory)
    }
}
