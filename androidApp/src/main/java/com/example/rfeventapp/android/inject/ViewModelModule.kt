package com.example.rfeventapp.android.inject

import com.example.rfeventapp.android.ui.splash.SplashViewModel
import com.example.rfeventapp.android.ui.login.LoginViewModel
import com.example.rfeventapp.android.ui.login.SignUpViewModel
import com.example.rfeventapp.android.ui.login.ForgotViewModel
import com.example.rfeventapp.android.ui.home.HomeViewModel
import com.example.rfeventapp.android.ui.calender.CalenderViewModel
import com.example.rfeventapp.android.ui.profile.ProfileViewModel
import com.example.rfeventapp.android.ui.home.EventDetailViewModel
import com.example.rfeventapp.android.ui.event.CreateEventViewModel
import com.example.rfeventapp.android.ui.event.MyEventsViewModel
import com.example.rfeventapp.android.ui.profile.EditProfileViewModel
import com.example.rfeventapp.android.ui.profile.EditPassViewModel
import com.example.rfeventapp.android.ui.map.MapViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SplashViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::ForgotViewModel)

    viewModelOf(::MapViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::CalenderViewModel)
    viewModelOf(::ProfileViewModel)

    viewModel { EventDetailViewModel(get()) }
    viewModelOf(::MyEventsViewModel)
    viewModel { CreateEventViewModel(get()) }
    viewModelOf(::EditProfileViewModel)
    viewModelOf(::EditPassViewModel)
}