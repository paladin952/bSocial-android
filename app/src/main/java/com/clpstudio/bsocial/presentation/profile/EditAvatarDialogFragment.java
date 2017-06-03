package com.clpstudio.bsocial.presentation.profile;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clpstudio.bsocial.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EditAvatarDialogFragment extends BottomSheetDialogFragment {

    public static final String BUNDLE_KEY_TITLE = "title";
    public static final String BUNDLE_KEY_SHOULD_REMOVE_PICTURE = "should_remove_picture";
    private static final String FRAGMENT_TAG = "EditAvatarDialogFragment";


    @BindView(R.id.title)
    TextView titleView;

    @BindView(R.id.edit_avatar_remove)
    View editAvatarRemoveRow;

    public static void showFromConversationDetail(FragmentManager fragmentManager, String title, boolean shouldRemovePicture) {
        BottomSheetDialogFragment fragment = new EditAvatarDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_TITLE, title);
        bundle.putBoolean(BUNDLE_KEY_SHOULD_REMOVE_PICTURE, shouldRemovePicture);
        fragment.setArguments(bundle);
        fragment.show(fragmentManager, FRAGMENT_TAG);
    }

    public static void show(FragmentManager fragmentManager) {
        BottomSheetDialogFragment fragment = new EditAvatarDialogFragment();
        fragment.show(fragmentManager, FRAGMENT_TAG);
    }

    private Unbinder unbinder;
    private boolean actionSelected;

    private OnItemsClickListener onItemsClickListener;

    private String title;
    private boolean shouldRemovePicture;

    public interface OnItemsClickListener {

        void onClickTakePhoto();

        void onClickSelectPhoto();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemsClickListener) {
            this.onItemsClickListener = (OnItemsClickListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle args = getArguments();
        if (args != null) {
            title = args.getString(BUNDLE_KEY_TITLE);
            shouldRemovePicture = args.getBoolean(BUNDLE_KEY_SHOULD_REMOVE_PICTURE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_avatar_bottom_sheet, container, false);
        unbinder = ButterKnife.bind(this, view);

        if(!TextUtils.isEmpty(title)) {
            titleView.setText(title);
        }

        if (shouldRemovePicture) {
            editAvatarRemoveRow.setVisibility(View.GONE);
        }
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
        if(onItemsClickListener != null) {
            onItemsClickListener.onClickTakePhoto();
            dismiss();
            return;
        }

        if (getParentFragment() instanceof EditAvatarFragment) {
            ((EditAvatarFragment) getParentFragment()).takePhoto();
        }
        actionSelected = true;
        dismiss();
    }

    @OnClick(R.id.edit_avatar_select)
    void onSelect() {
        if(onItemsClickListener != null) {
            onItemsClickListener.onClickSelectPhoto();
            dismiss();
            return;
        }

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
