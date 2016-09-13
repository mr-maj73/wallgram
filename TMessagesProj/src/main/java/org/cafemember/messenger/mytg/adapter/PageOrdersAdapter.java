package org.cafemember.messenger.mytg.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.cafemember.messenger.mytg.fragments.OrdersAdvertising;
import org.cafemember.messenger.mytg.fragments.OrdersMembership;

/**
 * Created by mohammad on 11/09/2016.
 */
public class PageOrdersAdapter extends FragmentStatePagerAdapter {

private int mNumOfTabs;
    public PageOrdersAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                OrdersAdvertising tab1 = new OrdersAdvertising();
                return tab1;

            case 1:
                OrdersMembership tab2 = new OrdersMembership();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
