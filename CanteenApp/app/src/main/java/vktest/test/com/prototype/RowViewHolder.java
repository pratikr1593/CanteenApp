package vktest.test.com.prototype;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RowViewHolder extends RecyclerView.ViewHolder{


    ImageView imageView;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView priceView;
    Button orderButton;
    public RowViewHolder(View view) {
        super(view);
        this.textView1 = (TextView) view.findViewById(R.id.tv1);
        this.textView2 = (TextView) view.findViewById(R.id.tv2);
        this.textView3 = (TextView) view.findViewById(R.id.tv3);
        this.imageView = (ImageView) view.findViewById(R.id.image);
        this.priceView = (TextView) view.findViewById(R.id.textview);
        this.orderButton = (Button) view.findViewById(R.id.orderButton);
    }




}