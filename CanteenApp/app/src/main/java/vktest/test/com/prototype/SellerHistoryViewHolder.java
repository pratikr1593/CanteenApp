package vktest.test.com.prototype;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Бейбут on 18.04.2016.
 */
public class SellerHistoryViewHolder extends RecyclerView.ViewHolder {
    TextView orderItem1;
    TextView orderItem2;
    TextView orderItem3;
    TextView idTextView;
    TextView dateTextView;

    public SellerHistoryViewHolder(View view) {
        super(view);
        this.orderItem1 = (TextView) view.findViewById(R.id.seller_history_item_1);
        this.orderItem2 = (TextView) view.findViewById(R.id.seller_history_item_2);
        this.orderItem3= (TextView) view.findViewById(R.id.seller_history_item_3);
        this.idTextView = (TextView) view.findViewById(R.id.seller_history_item_id);
        this.dateTextView = (TextView) view.findViewById(R.id.seller_history_item_date);

    }
}
