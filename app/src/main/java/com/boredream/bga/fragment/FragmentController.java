package com.boredream.bga.fragment;


import com.boredream.bga.R;
import com.boredream.bga.activity.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * fragment切换控制器, 初始化时直接add全部fragment, 然后利用show和hide进行切换控制
 */
public class FragmentController {

    private BottomNavigationView nav;
    private int containerId;
    private FragmentManager fm;
    private ArrayList<BaseFragment> fragments;

    public FragmentController(MainActivity activity, BottomNavigationView nav, int containerId, ArrayList<BaseFragment> fragments) {
        this.nav = nav;
        this.containerId = containerId;
        this.fragments = fragments;
        this.fm = activity.getSupportFragmentManager();
        initFragment();
    }

    public void initFragment() {
        FragmentTransaction ft = fm.beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            ft.add(containerId, fragments.get(i), String.valueOf(i));
        }
        ft.commitAllowingStateLoss();

        nav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showFragment(0);
                    return true;
                case R.id.navigation_dashboard:
                    showFragment(1);
                    return true;
                case R.id.navigation_notifications:
                    showFragment(2);
                    return true;
            }
            return false;
        });
    }

    public void showFragment(int position) {
        hideFragments();
        BaseFragment fragment = fragments.get(position);
        FragmentTransaction ft = fm.beginTransaction();
        ft.show(fragment);
        ft.commitAllowingStateLoss();
    }

    public void hideFragments() {
        FragmentTransaction ft = fm.beginTransaction();
        for (BaseFragment fragment : fragments) {
            if (fragment != null) {
                ft.hide(fragment);
            }
        }
        ft.commit();
    }

    public BaseFragment getFragment(int position) {
        return fragments.get(position);
    }

}