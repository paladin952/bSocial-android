package com.clpstudio.domainlib.dagger;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by clapalucian on 03/06/2017.
 */

@Component(
        modules = {
                LibraryModule.class,
                RetrofitModule.class,
                FirebaseModule.class
        }
)
@Singleton
public interface DiComponent {

}
