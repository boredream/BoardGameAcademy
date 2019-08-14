package com.boredream.bga.fragment;


import android.widget.RadioGroup;

import com.boredream.bga.activity.BaseActivity;

import java.util.ArrayList;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * fragment切换控制器, 初始化时直接add全部fragment, 然后利用show和hide进行切换控制
 */
public class FragmentController {

    private RadioGroup rg;
    private int containerId;
    private FragmentManager fm;
    private ArrayList<BaseFragment> fragments;

    public FragmentController(BaseActivity activity, RadioGroup rg, int containerId, ArrayList<BaseFragment> fragments) {
        this.rg = rg;
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
        ft.commit();

        rg.setOnCheckedChangeListener((group, checkedId) ->
                showFragment(rg.indexOfChild(rg.findViewById(checkedId))));
    }

    public void showFragment(int position) {
        hideFragments();
        BaseFragment fragment = fragments.get(position);
        FragmentTransaction ft = fm.beginTransaction();
        ft.show(fragment);
        ft.commit();
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