package com.clpstudio.bsocial.presentation.conversation.main.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.clpstudio.bsocial.presentation.conversation.main.fragments.conversations.ConversationsListFragment;
import com.clpstudio.bsocial.presentation.conversation.main.fragments.friends.FriendsListFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by clapalucian on 25/05/2017.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {
    public static final int CONVERSATIONS_POSITION = 0;
    public static final int FRIENDS_POSITION = 1;
    private static final int NR_PAGES = 2;

    private static final String[] titles = {"Conversations", "Friends"};

    private Map<Integer, String> mFragmentTags;
    private FragmentManager mFragmentManager;
    private Context context;

    public MainPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mFragmentManager = fm;
        mFragmentTags = new HashMap<>();
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return Fragment.instantiate(context, ConversationsListFragment.class.getName(), null);
        } else {
            return Fragment.instantiate(context, FriendsListFragment.class.getName(), null);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        if (obj instanceof Fragment) {
            // record the fragment tag here.
            Fragment f = (Fragment) obj;
            String tag = f.getTag();
            mFragmentTags.put(position, tag);
        }
        return obj;
    }

    public Fragment getFragment(int position) {
        String tag = mFragmentTags.get(position);
        if (tag == null)
            return null;
        return mFragmentManager.findFragmentByTag(tag);
    }

    @Override
    public int getCount() {
        return NR_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
