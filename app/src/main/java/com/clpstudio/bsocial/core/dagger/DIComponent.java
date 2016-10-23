package com.clpstudio.bsocial.core.dagger;

import com.clpstudio.bsocial.presentation.MainActivity;
import com.dealdash.http.ApiClientFactory;

import javax.inject.Singleton;

import dagger.Component;

@Component(
        modules = {
                ApplicationModule.class,
        }
)
@Singleton
public interface DIComponent {
    // AutoFactory classes used in modules
//    ApiClientFactory provideApiClientFactory();

    // injects
    inject(MainActivity inject);
}
