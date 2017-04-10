package com.clpstudio.bsocial.core.dagger;

import com.clpstudio.bsocial.presentation.BSocialApplication;
import com.clpstudio.bsocial.presentation.SplashScreen;
import com.clpstudio.bsocial.presentation.login.LoginActivity;
import com.clpstudio.bsocial.presentation.register.RegisterActivity;

import javax.inject.Singleton;

import dagger.Component;

@Component(
        modules = {
                ApplicationModule.class
        }
)
@Singleton
public interface DIComponent {
    void inject(BSocialApplication inject);

    void inject(SplashScreen splashScreen);

    void inject(LoginActivity loginActivity);

    void inject(RegisterActivity registerActivity);
}
