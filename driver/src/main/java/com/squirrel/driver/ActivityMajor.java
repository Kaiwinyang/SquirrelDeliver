package com.squirrel.driver;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;

public class ActivityMajor extends AppCompatActivity {

    FragmentOrdersComplete fragmentOrdersComplete = FragmentOrdersComplete.newInstance("", "");
    FragmentOrdersInSuspense fragmentOrdersInSuspense = FragmentOrdersInSuspense.newInstance("");
    FragmentOrdersProcessing fragmentOrdersProcessing = FragmentOrdersProcessing.newInstance("");
    FragmentSettings fragmentSettings = FragmentSettings.newInstance("");


    BottomNavigationView bottomNavigation;
    ViewPagerAdapter adapter;

    ViewPager viewPager;
    //MenuItem menuItem;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_orders_in_suspense:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_my_orders:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_orders_complete:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_sittings:
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };

    private ViewPager.OnPageChangeListener mOnPageChangeListener
            = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public void onPageSelected(int position) {
            bottomNavigation.getMenu().getItem(position).setChecked(true);
        }


    };


    private void setupViewPager() {
        viewPager = findViewById(R.id.majorViewPager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(fragmentOrdersInSuspense)
                .addFragment(fragmentOrdersProcessing)
                .addFragment(fragmentOrdersComplete)
                .addFragment(fragmentSettings);

        viewPager.setOffscreenPageLimit(adapter.getCount());
        viewPager.addOnPageChangeListener(mOnPageChangeListener);
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major);

        bottomNavigation = findViewById(R.id.navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setupViewPager();
    }

}

class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    public ViewPagerAdapter addFragment(Fragment fragment) {
        fragments.add(fragment);
        return this;
    }
}
