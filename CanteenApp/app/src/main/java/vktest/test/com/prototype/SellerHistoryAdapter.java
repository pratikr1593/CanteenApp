package vktest.test.com.prototype;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Бейбут on 18.04.2016.
 */
public class SellerHistoryAdapter extends RecyclerView.Adapter<SellerHistoryViewHolder>{
    Context context;
    ArrayList<SellerHistoryItem> itemsList;


    public SellerHistoryAdapter(Context context, ArrayList<SellerHistoryItem> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @Override
    public SellerHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_seller_history, parent, false);
        SellerHistoryViewHolder viewHolder = new SellerHistoryViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SellerHistoryViewHolder rowViewHolder, int position) {

        SellerHistoryItem items = itemsList.get(position);
        rowViewHolder.orderItem1.setText(String.valueOf(items.getItem1()));
        rowViewHolder.orderItem2.setText(String.valueOf(items.getItem2()));
        rowViewHolder.orderItem3.setText(String.valueOf(items.getItem3()));
        rowViewHolder.idTextView.setText(items.getId());
        rowViewHolder.dateTextView.setText(String.valueOf(new Date(Long.parseLong(items.getDate()))));

    }

    @Override
    public int getItemCount() {

        if (itemsList == null) {
            return 0;
        } else {
            return itemsList.size();
        }
    }
}
