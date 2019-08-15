package com.boredream.bga.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boredream.bga.R;
import com.boredream.bga.entity.Book;
import com.boredream.bga.entity.Course;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainFragment extends BaseFragment {

    View view;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.rv)
    RecyclerView rv;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_main, null);
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
        rv.setLayoutManager(new GridLayoutManager(activity, 2));
//        adapter = new TvAdapter(activity, tvList);
//        rv.setAdapter(adapter);

        refresh.setEnableRefresh(true);
        refresh.setEnableLoadmore(false);
        refresh.setOnRefreshListener(refresh -> {
            loadData();
        });

        setupRefreshLayout(refresh);
    }

    private void loadData() {
    }

    private void onResponse() {
    }

    @OnClick({R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.btn_5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                Book book = new Book();
                book.setName("ROOT");
                book.setScore(7.9);
                // book.setType(Book.DE);

                FirebaseFirestore.getInstance()
                        .collection("book")
                        .add(book)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

                break;
            case R.id.btn_2:
                FirebaseFirestore.getInstance()
                        .collection("book")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<Book> books = queryDocumentSnapshots.toObjects(Book.class);
                                Log.i(TAG, "onSuccess: " + books);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error getting documents.", e);
                            }
                        })
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                Log.i(TAG, "onComplete: ");
                            }
                        });

                break;
            case R.id.btn_3:
                break;
            case R.id.btn_4:
                break;
            case R.id.btn_5:
                break;
        }
    }
}