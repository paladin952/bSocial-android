package com.clpstudio.bsocial.presentation.conversation.main.fragments.friends;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.data.models.firebase.RegisteredUserViewModel;
import com.clpstudio.bsocial.presentation.BSocialApplication;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by clapalucian on 16/05/2017.
 */

public class FriendsListFragment extends Fragment implements FriendsListPresenter.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    View progressBar;

    @Inject
    FriendsListPresenter presenter;

    private FriendsListAdapter adapter;
    private AlertDialog alertDialog;
    private OnOpenConversationClicked clickListener;

    public FriendsListFragment() {
    }

    public static FriendsListFragment get() {
        return new FriendsListFragment();
    }

    public interface OnOpenConversationClicked {
        void openConversation(RegisteredUserViewModel user);
    }

    @OnClick(R.id.add_friend_fb)
    public void onAddFriendsClick() {
        presenter.onAddFriendsClick();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BSocialApplication) getActivity().getApplicationContext()).getDiComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment_friends_list, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOpenConversationClicked) {
            clickListener = (OnOpenConversationClicked) context;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        adapter = new FriendsListAdapter();
        adapter.setOnClickListener(element -> {
            if (clickListener != null) {
                clickListener.openConversation(element);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        presenter.bindView(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.loadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unbindView();
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showData(List<RegisteredUserViewModel> data) {
        adapter.addAll(data);
    }

    @Override
    public void refreshData() {
        presenter.loadData();
    }

    @Override
    public void showAddFriendDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        final EditText edittext = new EditText(getActivity());
        edittext.setHint(getString(R.string.search_friend_hint));
        edittext.requestFocus();
        alertDialog = alert.setMessage(getString(R.string.search_friend_message))
                .setView(edittext)
                .setPositiveButton(getString(R.string.add), (dialog, whichButton) -> {
                    String email = edittext.getText().toString();
                    presenter.addFriendIfExists(email);
                })
                .setNegativeButton(getString(R.string.cancel), (dialog, whichButton) -> dialog.dismiss())
                .show();
    }

    @Override
    public void hideAddFriendDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }


}
