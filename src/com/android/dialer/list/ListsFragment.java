package com.android.dialer.list;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.contacts.common.list.OnPhoneNumberPickerActionListener;
import com.android.dialer.R;
import com.android.dialer.calllog.CallLogFragment;
import com.android.dialer.calllog.CallLogQueryHandler;

/**
 * Fragment that is used as the main screen of the Dialer.
 *
 * Contains a ViewPager that contains various contact lists like the Speed Dial list and the
 * All Contacts list. This will also eventually contain the logic that allows sliding the
 * ViewPager containing the lists up above the shortcut cards and pin it against the top of the
 * screen.
 */
public class ListsFragment extends Fragment {

    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private PhoneFavoriteFragment mSpeedDialFragment;
    private CallLogFragment mRecentsFragment;
    private AllContactsFragment mAllContactsFragment;

    private static final int TAB_INDEX_SPEED_DIAL = 0;
    private static final int TAB_INDEX_RECENTS = 1;
    private static final int TAB_INDEX_ALL_CONTACTS = 2;

    private String[] mTabTitles;

    private static final int TAB_INDEX_COUNT = 3;

    // TODO: Replace with a date limit (e.g. 2 weeks)
    private static final int MAX_ENTRIES = 20;

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case TAB_INDEX_SPEED_DIAL:
                    mSpeedDialFragment = new PhoneFavoriteFragment();
                    return mSpeedDialFragment;
                case TAB_INDEX_RECENTS:
                    mRecentsFragment = new CallLogFragment(CallLogQueryHandler.CALL_TYPE_ALL,
                            MAX_ENTRIES);
                    return mRecentsFragment;
                case TAB_INDEX_ALL_CONTACTS:
                    mAllContactsFragment = new AllContactsFragment();
                    return mAllContactsFragment;
            }
            throw new IllegalStateException("No fragment at position " + position);
        }

        @Override
        public int getCount() {
            return TAB_INDEX_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles[position];
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View parentView = inflater.inflate(R.layout.lists_fragment, container, false);
        mViewPager = (ViewPager) parentView.findViewById(R.id.lists_pager);
        mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        mTabTitles = new String[TAB_INDEX_COUNT];
        mTabTitles[TAB_INDEX_SPEED_DIAL] = getResources().getString(R.string.tab_speed_dial);
        mTabTitles[TAB_INDEX_RECENTS] = getResources().getString(R.string.tab_recents);
        mTabTitles[TAB_INDEX_ALL_CONTACTS] = getResources().getString(R.string.tab_all_contacts);

        ViewPagerTabs tabs = (ViewPagerTabs) parentView.findViewById(R.id.lists_pager_header);
        tabs.setViewPager(mViewPager);
        return parentView;
    }
}