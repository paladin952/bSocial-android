package com.clpstudio.domainlib.dagger;

import dagger.Module;

/**
 * Created by clapalucian on 03/06/2017.
 */

@Module(includes = {
        FirebaseModule.class,
        RetrofitModule.class
})
public class LibraryModule {



}
