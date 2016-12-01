package vktest.test.com.prototype;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RowViewSellerHolder extends RecyclerView.ViewHolder{


    ImageView imageView;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView priceView;
    Button orderButton;
    public RowViewSellerHolder(View view) {
        super(view);
        this.textView1 = (TextView) view.findViewById(R.id.tv1Seller);
        this.textView2 = (TextView) view.findViewById(R.id.tv2Seller);
        this.textView3 = (TextView) view.findViewById(R.id.tv3Seller);
        this.imageView = (ImageView) view.findViewById(R.id.imageSeller);
        this.priceView = (TextView) view.findViewById(R.id.textviewSeller);
        this.orderButton = (Button) view.findViewById(R.id.deleteButtonSeller);
    }




}