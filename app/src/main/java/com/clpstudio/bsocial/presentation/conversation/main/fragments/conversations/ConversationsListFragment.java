package com.clpstudio.bsocial.presentation.conversation.main.fragments.conversations;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clpstudio.bsocial.R;
import com.clpstudio.bsocial.bussiness.service.DatabaseService;
import com.clpstudio.bsocial.data.models.conversations.ConversationNameModel;
import com.clpstudio.bsocial.presentation.BSocialApplication;
import com.clpstudio.bsocial.presentation.conversation.details.ConversationDetailActivity;
import com.clpstudio.bsocial.presentation.conversation.main.GoToPageListener;
import com.clpstudio.bsocial.presentation.conversation.main.MainPagerAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by clapalucian on 5/8/17.
 */

public class ConversationsListFragment extends Fragment implements ConversationsListPresenter.View {

    private static final int GRID_NR_OF_ITEMS = 2;

    @Inject
    DatabaseService databaseService;
    @Inject
    ConversationsListPresenter presenter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    View progressBar;


    private GridLayoutManager gridLayoutManager;
    private ConversationsListAdapter adapter;
    private GoToPageListener goToPageListener;

    public static ConversationsListFragment get() {
        return new ConversationsListFragment();
    }

    @OnClick(R.id.write_button)
    public void onWriteButtonClick() {
        if (goToPageListener != null) {
            goToPageListener.gotoPage(MainPagerAdapter.FRIENDS_POSITION);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GoToPageListener) {
            goToPageListener = (GoToPageListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment_conversation_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        gridLayoutManager = new GridLayoutManager(getActivity(), GRID_NR_OF_ITEMS);
        adapter = new ConversationsListAdapter();
        adapter.setClickListener(element -> ConversationDetailActivity.startActivity(getActivity(), element.getName()));
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        presenter.bindView(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BSocialApplication) getActivity().getApplicationContext()).getDiComponent().inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unbindView();
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
    public void showData(List<ConversationNameModel> data) {
        adapter.addAll(data);
    }
}
