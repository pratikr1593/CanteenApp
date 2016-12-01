package vktest.test.com.prototype;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.UUID;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RowViewHolder> {
    Context context;
    ArrayList<MenuSingleItems> itemsList;
    String customerID;

    public RecyclerViewAdapter(Context context, ArrayList<MenuSingleItems> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @Override
    public int getItemCount() {
        if (itemsList == null) {
            return 0;
        } else {
            return itemsList.size();
        }
    }

    @Override
    public RowViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.single_row, null);
        RowViewHolder viewHolder = new RowViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RowViewHolder rowViewHolder, int position) {
        MenuSingleItems items = itemsList.get(position);
        rowViewHolder.textView1.setText(String.valueOf(items.getItem1()));
        rowViewHolder.textView2.setText(String.valueOf(items.getItem2()));
        rowViewHolder.priceView.setText(String.valueOf(items.getPrice()));
        rowViewHolder.textView3.setText(String.valueOf(items.getItem3()));

        //rowViewHolder.imageView.setBackgroundResource(items.getImgPath());
        Picasso.with(context).load(items.getImage()).into(rowViewHolder.imageView);
                rowViewHolder.orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                builder.setTitle("Order Info");
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
                customerID = pref.getString("customerID", "0");
                if(customerID.equals("0")) {
                    String generatedString = UUID.randomUUID().toString();
                    customerID = generatedString.substring(0, Math.min(generatedString.length(), 7));
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("customerID", customerID);
                    editor.commit();
                }
                Log.d("customerID", customerID);
                String generatedString = UUID.randomUUID().toString();
                final String orderID = generatedString.substring(0, Math.min(generatedString.length(), 5));
                String message = "Total price is: " + rowViewHolder.priceView.getText() + "\nOrder ID is: " + orderID;
                builder.setMessage(message);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Firebase myFirebaseRef = new Firebase("https://torrid-heat-2650.firebaseio.com/orders/" + customerID + "/" + orderID);
                        myFirebaseRef.child("id").setValue(orderID);
                        myFirebaseRef.child("product1").setValue(rowViewHolder.textView1.getText());
                        myFirebaseRef.child("product2").setValue(rowViewHolder.textView2.getText());
                        myFirebaseRef.child("product3").setValue(rowViewHolder.textView3.getText());
                        myFirebaseRef.child("price").setValue(rowViewHolder.priceView.getText());
                        myFirebaseRef.child("status").setValue("not ready");

                        Toast.makeText(context, "Order Accepted", Toast.LENGTH_SHORT).show();
                        Order order = new Order();
                        order.setProduct1(rowViewHolder.textView1.getText().toString());
                        order.setProduct2(rowViewHolder.textView2.getText().toString());
                        order.setProduct3(rowViewHolder.textView3.getText().toString());
                        order.setId(orderID);
                        order.setPrice(rowViewHolder.priceView.getText().toString());
                        MyOrdersFragment.updateAdapter(order); /////////////////////

                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        });
    }


}