package com.ulpgc.uniMatch.data.infrastructure.viewModels

import androidx.lifecycle.ViewModel
import com.ulpgc.uniMatch.data.application.services.NotificationsService

class NotificationsViewModel (
    private val notificationsService: NotificationsService,
    private val errorViewModel: ErrorViewModel,
    private val authViewModel: AuthViewModel
) : ViewModel() {

}