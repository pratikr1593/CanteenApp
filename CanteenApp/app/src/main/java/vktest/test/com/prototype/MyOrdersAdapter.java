package vktest.test.com.prototype;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.Firebase;

import java.util.ArrayList;
public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersViewHolder>{
    Context context;
    ArrayList<Order> itemsList;


    public MyOrdersAdapter(Context context, ArrayList<Order> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }


    @Override
    public MyOrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_order, parent, false);
        MyOrdersViewHolder viewHolder = new MyOrdersViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyOrdersViewHolder holder, int position) {
        final Order items = itemsList.get(position);
        holder.textView1.setText(String.valueOf(items.getProduct1()));
        holder.textView2.setText(String.valueOf(items.getProduct2()));
        holder.textView3.setText(String.valueOf(items.getProduct3()));
        holder.textViewId.setText("Your order number: " + String.valueOf(items.getId()));
        holder.priceView.setText(String.valueOf(items.getPrice()));

        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                builder.setTitle("Order status");
                String message = "Are you sure?";
                builder.setMessage(message);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
                        String customerID = pref.getString("customerID", "0");
                        final Firebase myFirebaseRef = new Firebase("https://torrid-heat-2650.firebaseio.com/orders/" +  customerID + "/"
                                + items.getId());





                        myFirebaseRef.removeValue();
                        itemsList.remove(items);
                        notifyDataSetChanged();

                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();




            }
        });

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
