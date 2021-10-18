package com.tids.clikonservice.adapter.driver;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.tids.clikonservice.Fragment.MerchantDeliveryFragment;
import com.tids.clikonservice.Fragment.TechnicianDeliveryFragment;

public class DeliveryPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    public DeliveryPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MerchantDeliveryFragment tab1 = new MerchantDeliveryFragment();
                return tab1;
            case 1:
                TechnicianDeliveryFragment tab2 = new TechnicianDeliveryFragment();
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