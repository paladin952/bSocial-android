package com.clpstudio.bsocial.core.dagger

import android.app.Application
import android.content.Context
import com.clpstudio.bsocial.core.persistance.ISharedPreferencesUtils
import com.clpstudio.bsocial.core.persistance.SharedPreferencesUtils
import com.clpstudio.bsocial.data.Session
import com.clpstudio.bsocial.presentation.BSocialApplication
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(internal var application: Application) {
    internal var refWatcher: RefWatcher

    init {
        refWatcher = LeakCanary.install(application)
    }

    @Provides
    @Singleton
    internal fun provideApplication(): Application {
        return application
    }

    @Provides
    @Singleton
    internal fun providesDealdashApplication(): BSocialApplication {
        return application as BSocialApplication
    }

    @Provides
    @Singleton
    internal fun providesContext(): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    internal fun providesSharedPreferences(context: Context, session: Session): ISharedPreferencesUtils {
        return SharedPreferencesUtils(context, session)
    }


    @Provides
    @Singleton
    internal fun provideRefWatcher(): RefWatcher {
        return refWatcher
    }

}
