package com.clpstudio.domain.usecases;

import com.clpstudio.database.services.ProfileService;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Completable;

/**
 * Created by clapalucian on 04/06/2017.
 */

public class ProfileUseCases {

    @Inject
    ProfileService profileService;

    @Inject
    public ProfileUseCases() {
    }


    public Completable updateNickname(String nickname) {
        return profileService.updateNickname(nickname);
    }

    public Completable upload(File file) {
        return profileService.upload(file);
    }

    public Completable removeProfilePicture() {
        return profileService.removeProfilePicture();
    }

}
