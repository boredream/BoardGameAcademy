package com.boredream.bga.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boredream.bga.R;
import com.boredream.bga.constants.UserInfoKeeper;
import com.boredream.bga.entity.Course;
import com.boredream.bga.net.HttpRequest;
import com.boredream.bga.net.RxComposer;
import com.boredream.bga.net.SimpleDisObserver;
import com.boredream.bga.utils.MockUtils;
import com.boredream.bga.view.TitleBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ActFragment extends BaseFragment {

    View view;
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.rv)
    RecyclerView rv;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_act, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        loadData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initView() {
        titleBar.setRightIv(R.drawable.ic_close, v -> addCourse()); // FIXME: 2019-08-26

        rv.setLayoutManager(new GridLayoutManager(activity, 2));
        adapter = new TvAdapter(activity, tvList);
        rv.setAdapter(adapter);

        refresh.setEnableRefresh(true);
        refresh.setEnableLoadmore(false);
        refresh.setOnRefreshListener(refresh -> {
            loadData();
        });

        setupRefreshLayout(refresh);
    }

    private void addCourse() {
        HttpRequest.getInstance()
                .createCourse(System.currentTimeMillis(),
                        MockUtils.getClassroom(),
                        UserInfoKeeper.getInstance().getCurrentUserProfile())
                .compose(RxComposer.commonProgress(this))
                .subscribe(new SimpleDisObserver<Course>() {
                    @Override
                    public void onNext(Course course) {
                        showTip("add success");
                    }
                });
    }

    private void loadData() {
        HttpRequest.getInstance()
                .getCourseList()
                .compose(RxComposer.commonRefresh(this, false))
                .subscribe(new SimpleDisObserver<List<Course>>() {
                    @Override
                    public void onNext(List<Course> courses) {

                    }
                });
    }

    private void onResponse() {

    }
}