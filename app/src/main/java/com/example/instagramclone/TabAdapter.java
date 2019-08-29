package com.example.instagramclone;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int tabPosition) {
        switch (tabPosition){
            case 0:
                SharePostTab sharePostTab = new SharePostTab();
                return sharePostTab;

            case 1:
                UsersTab usersTab = new UsersTab();
                return usersTab;

            case 2:
                ProfileTab profileTab = new ProfileTab();
                return profileTab;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Post";
            case 1:
                return "Users";
            case 2:
                return "Profile";

            default:
                return null;
        }
    }
}
