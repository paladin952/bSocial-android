package com.clpstudio.bsocial.presentation.profile;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clpstudio.bsocial.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EditAvatarDialogFragment extends BottomSheetDialogFragment {

    private static final String FRAGMENT_TAG = "EditAvatarDialogFragment";

    public static void show(FragmentManager fragmentManager) {
        BottomSheetDialogFragment fragment = new EditAvatarDialogFragment();
        fragment.show(fragmentManager, FRAGMENT_TAG);
    }

    private Unbinder unbinder;
    private boolean actionSelected;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_avatar_bottom_sheet, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (!actionSelected && getParentFragment() instanceof EditAvatarFragment) {
            ((EditAvatarFragment) getParentFragment()).dialogCancelled();
        }
    }

    @OnClick(R.id.edit_avatar_take_photo)
    void onTakePhoto() {
        if (getParentFragment() instanceof EditAvatarFragment) {
            ((EditAvatarFragment) getParentFragment()).takePhoto();
        }
        actionSelected = true;
        dismiss();
    }

    @OnClick(R.id.edit_avatar_select)
    void onSelect() {
        if (getParentFragment() instanceof EditAvatarFragment) {
            ((EditAvatarFragment) getParentFragment()).selectPhoto();
        }
        actionSelected = true;
        dismiss();
    }

    @OnClick(R.id.edit_avatar_remove)
    void onRemoveAvatar() {
        if (getParentFragment() instanceof EditAvatarFragment) {
            ((EditAvatarFragment) getParentFragment()).removeAvatar();
        }
        actionSelected = true;
        dismiss();
    }
}
