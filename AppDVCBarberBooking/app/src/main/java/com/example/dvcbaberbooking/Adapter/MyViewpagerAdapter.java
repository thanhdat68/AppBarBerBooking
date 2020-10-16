package com.example.dvcbaberbooking.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.dvcbaberbooking.Fragments.BookingStep1Fragment;
import com.example.dvcbaberbooking.Fragments.BookingStep2Fragment;
import com.example.dvcbaberbooking.Fragments.BookingStep3Fragment;
import com.example.dvcbaberbooking.Fragments.BookingStep4Fragment;

public class MyViewpagerAdapter extends FragmentPagerAdapter {

    public MyViewpagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return BookingStep1Fragment.getInstance();
            case 1:
                return BookingStep2Fragment.getInstance();
            case 2:
                return BookingStep3Fragment.getInstance();
            case 3:
                return BookingStep4Fragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
