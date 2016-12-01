package vktest.test.com.prototype;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OrderViewHolder extends RecyclerView.ViewHolder{


    TextView orderItem1;
    TextView orderItem2;
    TextView orderItem3;

    TextView idTextView;
    TextView priceTextView;
    Button doneButton;
    Button scanButton;



    public OrderViewHolder(View view) {
        super(view);
        this.orderItem1 = (TextView) view.findViewById(R.id.tv1);
        this.orderItem2 = (TextView) view.findViewById(R.id.tv2);
        this.orderItem3= (TextView) view.findViewById(R.id.tv3);
        this.idTextView = (TextView) view.findViewById(R.id.tvId);
        this.priceTextView = (TextView) view.findViewById(R.id.price);
        this.doneButton = (Button) view.findViewById(R.id.submitButton);
        this.scanButton = (Button) view.findViewById(R.id.scanButton);
    }




}