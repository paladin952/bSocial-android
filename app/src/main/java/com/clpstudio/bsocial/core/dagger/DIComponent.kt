package com.clpstudio.bsocial.core.dagger

import com.clpstudio.bsocial.presentation.MainActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = arrayOf(ApplicationModule::class))
@Singleton
interface DIComponent {
    // AutoFactory classes used in modules
    //    ApiClientFactory provideApiClientFactory();

    // injects
    fun inject(inject: MainActivity): MainActivity
}
