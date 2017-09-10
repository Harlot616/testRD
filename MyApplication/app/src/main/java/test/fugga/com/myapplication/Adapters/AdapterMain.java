package test.fugga.com.myapplication.Adapters;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import test.fugga.com.myapplication.Classes.DeleteElements;
import test.fugga.com.myapplication.Classes.MyData;
import test.fugga.com.myapplication.Classes.PersistenData;
import test.fugga.com.myapplication.Classes.SortUtil;
import test.fugga.com.myapplication.Fragments.FragmentWebview;
import test.fugga.com.myapplication.MainActivity;
import test.fugga.com.myapplication.R;
import test.fugga.com.myapplication.ViewHolders.ViewHolderMain;

public class AdapterMain extends RecyclerView.Adapter<ViewHolderMain> {
    private ArrayList<MyData> myDataList;
    private MainActivity ma;
    private Realm realm;
    private SortUtil util;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public AdapterMain () { }

    public AdapterMain (ArrayList<MyData> myDataList, MainActivity ma) {
        this.myDataList = myDataList;
        this.ma = ma;
        this.util = new SortUtil();
    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }

    @Override
    public ViewHolderMain onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list, null);
        final ViewHolderMain vh = new ViewHolderMain(view);

        vh.rowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentWebview fwv = new FragmentWebview();
                Bundle b = new Bundle();
                b.putString("url", myDataList.get(vh.getAdapterPosition()).story_url);
                fwv.setArguments(b);
                ma.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ma.getSupportActionBar().setDisplayShowHomeEnabled(true);
                ma.changeFragment(fwv);
            }
        });

        vh.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount(vh.getAdapterPosition());
                vh.swipeLayout.close(true);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolderMain holder, int position) {
        MyData dataObject = myDataList.get(position);

        if (myDataList.get(position).title == null) {
            holder.title.setText(myDataList.get(position).story_title);
        } else {
            holder.title.setText(myDataList.get(position).title);
        }
        holder.authorCreate.setText(myDataList.get(position).author + " - " +
                util.timePassed(myDataList.get(position).created_at));

        viewBinderHelper.bind(holder.swipeLayout, dataObject.objectID);
    }

    private void deleteAccount(final int index) {
        realm = Realm.getDefaultInstance();
        final RealmResults<PersistenData> results = realm.where(PersistenData.class).findAll();

        //Almacenamiento elemento eliminado.
        realm.beginTransaction();
        DeleteElements dElements = realm.createObject(DeleteElements.class);
        dElements.objectID = myDataList.get(index).objectID;
        realm.commitTransaction();

        //Se elimina de la lista
        myDataList.remove(index);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();
            }
        });

        for (MyData md : myDataList) {
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

        notifyItemRemoved(index);
        notifyItemRangeChanged(0, myDataList.size());
    }
}
