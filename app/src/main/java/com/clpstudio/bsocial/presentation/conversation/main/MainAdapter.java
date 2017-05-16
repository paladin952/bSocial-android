package com.clpstudio.bsocial.presentation.conversation.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.clpstudio.bsocial.presentation.conversation.main.fragments.conversations.ConversationsListFragment;
import com.clpstudio.bsocial.presentation.conversation.main.fragments.friends.FriendsListFragment;

/**
 * Created by clapalucian on 16/05/2017.
 */

public class MainAdapter extends FragmentStatePagerAdapter {

    private static final int NR_PAGES = 2;

    public MainAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return ConversationsListFragment.get();
        } else {
            return FriendsListFragment.get();
        }
    }

    @Override
    public int getCount() {
        return NR_PAGES;
    }
}
