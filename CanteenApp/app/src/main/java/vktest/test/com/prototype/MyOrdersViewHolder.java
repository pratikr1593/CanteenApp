package vktest.test.com.prototype;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Бейбут on 10.04.2016.
 */
public class MyOrdersViewHolder extends RecyclerView.ViewHolder
{

    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textViewId;
    TextView priceView;
    Button cancelButton;
    public MyOrdersViewHolder(View view) {
        super(view);
        this.textViewId = (TextView) view.findViewById(R.id.my_order_id);
        this.textView1 = (TextView) view.findViewById(R.id.my_order_item1);
        this.textView2 = (TextView) view.findViewById(R.id.my_order_item2);
        this.textView3 = (TextView) view.findViewById(R.id.my_order_item3);

        this.priceView = (TextView) view.findViewById(R.id.my_order_price);
        this.cancelButton = (Button) view.findViewById(R.id.my_order_cancelOrder);
    }
}
