package com.clpstudio.domainlib.dagger;

/**
 * Created by clapalucian on 03/06/2017.
 */

public class DaggerWrapper {

    private static DiComponent mComponent;

    public static DiComponent getComponent() {
        if (mComponent == null) {
            initComponent();
        }
        return mComponent;
    }

    private static void initComponent () {
        mComponent = DaggerDiComponent
                .builder()
                .libraryModule(new LibraryModule())
                .build();
    }

}
