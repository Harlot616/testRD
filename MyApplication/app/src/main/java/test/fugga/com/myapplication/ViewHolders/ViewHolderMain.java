package test.fugga.com.myapplication.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import test.fugga.com.myapplication.R;


public class ViewHolderMain extends RecyclerView.ViewHolder {
    public SwipeRevealLayout swipeLayout;
    public LinearLayout delete, rowList;
    public TextView title;
    public TextView authorCreate;

    public ViewHolderMain(View itemView) {
        super(itemView);

        swipeLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe);
        delete = (LinearLayout) itemView.findViewById(R.id.bttnDelete);
        rowList = (LinearLayout) itemView.findViewById(R.id.rowList);
        title = (TextView)itemView.findViewById(R.id.title);
        authorCreate = (TextView)itemView.findViewById(R.id.author_create);
    }
}
