package com.boredream.bga.activity;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.boredream.bga.R;
import com.boredream.bga.fragment.BaseFragment;
import com.boredream.bga.fragment.FragmentController;
import com.boredream.bga.fragment.MainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.container)
    LinearLayout container;

    private FragmentController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        ButterKnife.bind(this);

        ArrayList<BaseFragment> fragments = new ArrayList<>();
        fragments.add(new MainFragment());
        fragments.add(new MainFragment());
        fragments.add(new MainFragment());

        controller = new FragmentController(this, navigation, R.id.fl_content, fragments);
        controller.showFragment(0);
    }

}
