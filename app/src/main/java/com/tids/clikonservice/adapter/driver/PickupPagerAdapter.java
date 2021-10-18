package com.tids.clikonservice.adapter.driver;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.tids.clikonservice.Fragment.MerchantPickupFragment;
import com.tids.clikonservice.Fragment.TechnicianPickupFragment;

public class PickupPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    public PickupPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MerchantPickupFragment tab1 = new MerchantPickupFragment();
                return tab1;
            case 1:
                TechnicianPickupFragment tab2 = new TechnicianPickupFragment();
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