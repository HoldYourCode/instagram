package com.holdyourcolour.instahelper.controller.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.holdyourcolour.instahelper.view.fragments.UsersListFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a UsersListFragment
        Log.d("TAG","get item " + position);
        return UsersListFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        // Show 4 total pages.
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Followers";
            case 1:
                return "Following";
            case 2:
                return "unFollowers";
            case 3:
                return "unAccepted";
        }
        return null;
    }
}
