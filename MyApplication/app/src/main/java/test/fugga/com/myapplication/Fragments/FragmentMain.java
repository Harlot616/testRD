package test.fugga.com.myapplication.Fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.fugga.com.myapplication.Adapters.AdapterMain;
import test.fugga.com.myapplication.Classes.DeleteElements;
import test.fugga.com.myapplication.Classes.JsonData;
import test.fugga.com.myapplication.Classes.MyData;
import test.fugga.com.myapplication.Classes.SortUtil;
import test.fugga.com.myapplication.CustomItemDecoration;
import test.fugga.com.myapplication.Classes.PersistenData;
import test.fugga.com.myapplication.Delegates.Methods;
import test.fugga.com.myapplication.Delegates.ServiceDelegate;
import test.fugga.com.myapplication.MainActivity;
import test.fugga.com.myapplication.R;

public class FragmentMain extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private AdapterMain adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Drawable divider;
    private RecyclerView.ItemDecoration dividerDecoration;
    private ArrayList<MyData> myListData;
    private ArrayList<MyData> baseListData;
    private Realm realm;
    private SortUtil sort;


    public FragmentMain() {
        sort = new SortUtil();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myListData = new ArrayList<>();
        baseListData = new ArrayList<>();
        realm = Realm.getDefaultInstance();
        final RealmResults<PersistenData> data = realm.where(PersistenData.class).findAll();

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        refreshLayout.setOnRefreshListener(this);

        if (isNetworkAvailable()) {
            getData();
        } else if (!data.isEmpty()) {
            for (PersistenData pData:data) {
                MyData d = new MyData();
                d.objectID = pData.objectID;
                d.story_title = pData.story_title;
                d.title = pData.title;
                d.author = pData.author;
                d.created_at = pData.created_at;
                d.story_url = pData.story_url;
                myListData.add(d);
            }

            loadData();
        }
    }

    @Override
    public void onRefresh() {
        if (isNetworkAvailable()) {
            getData();
        } else {
            refreshLayout.setRefreshing(false);
        }
    }

    private void getData() {
        Methods methods = ServiceDelegate.getClient().create(Methods.class);
        Call<JsonData> call = methods.getHits();
        call.enqueue(new Callback<JsonData>() {
            @Override
            public void onResponse(Call<JsonData> call, Response<JsonData> response) {
                myListData = sort.QuickSort(response.body().hits, 0, response.body().hits.size() - 1, false);
                final RealmResults<DeleteElements> deleted = realm.where(DeleteElements.class).findAll();

                for (MyData md : myListData) {
                    baseListData.add(md);
                }
                for (DeleteElements dElem : deleted) {
                    for (MyData bData : baseListData) {
                        if (bData.objectID == dElem.objectID) {
                            myListData.remove(bData);
                        }
                    }
                }

                final RealmResults<PersistenData> results = realm.where(PersistenData.class).findAll();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        results.deleteAllFromRealm();
                    }
                });
                for (MyData md : myListData) {
                    realm.beginTransaction();

                    PersistenData pData = realm.createObject(PersistenData.class);
                    pData.objectID = md.objectID;
                    pData.story_title = md.story_title;
                    pData.title = md.title;
                    pData.author = md.author;
                    pData.created_at = md.created_at;
                    pData.story_url = md.story_url;

                    realm.commitTransaction();
                }

                loadData();
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<JsonData> call, Throwable t) {
                Log.e("Restaurant", t.toString());
            }
        });
    }

    private void loadData() {
        adapter = new AdapterMain(myListData, (MainActivity) getActivity());
        divider = ContextCompat.getDrawable(getContext(), R.drawable.divider);
        dividerDecoration = new CustomItemDecoration(divider);


        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(dividerDecoration);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}