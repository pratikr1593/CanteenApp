package vktest.test.com.prototype;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class TextViewAdapter extends RecyclerView.Adapter<OrderViewHolder> {
    public static OrderViewHolder mCurrentViewHolder = null;
    Context context;
    ArrayList<Order> orders;
    public static IntentResult mIntentResult;

    public TextViewAdapter(Context context, ArrayList<Order> itemsList) {
        this.context = context;
        this.orders = itemsList;
    }

    @Override
    public int getItemCount() {
        if (orders == null) {
            return 0;
        } else {
            return orders.size();
        }
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.list_item_order, viewGroup, false);
        OrderViewHolder viewHolder = new OrderViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final OrderViewHolder rowViewHolder, final int position) {
        final Order items = orders.get(position);
        rowViewHolder.orderItem1.setText(String.valueOf(items.getProduct1()));
        rowViewHolder.orderItem2.setText(String.valueOf(items.getProduct2()));
        rowViewHolder.orderItem3.setText(String.valueOf(items.getProduct3()));
        rowViewHolder.idTextView.setText(String.valueOf(items.getId()));
        rowViewHolder.priceTextView.setText(String.valueOf(items.getPrice()));

        rowViewHolder.doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCurrentViewHolder = rowViewHolder;
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                builder.setTitle("Order status");
                String message = "Are you sure?";
                builder.setMessage(message);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final Firebase myFirebaseRef = new Firebase("https://torrid-heat-2650.firebaseio.com/orders/");
                        myFirebaseRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                for (DataSnapshot root : snapshot.getChildren())
                                    for (DataSnapshot child : root.getChildren()) {
                                        if((child.child("id").getValue().toString().equals(items.getId().toString()))){

                                            final Firebase myFirebaseRef = new Firebase("https://torrid-heat-2650.firebaseio.com/orders/" + root.getKey() + "/"
                                            + items.getId());
                                            myFirebaseRef.removeValue();

                                        }

                                    }

                                orders.remove(items);
                                notifyDataSetChanged();

                                Firebase myFirebaseRef = new Firebase("https://torrid-heat-2650.firebaseio.com/orders_history/" + items.getId() );
                                myFirebaseRef.child("price").setValue(items.getPrice());
                                myFirebaseRef.child("id").setValue(items.getId());

                                myFirebaseRef.child("product1").setValue(items.getProduct1());
                                myFirebaseRef.child("product2").setValue(items.getProduct2());
                                myFirebaseRef.child("product3").setValue(items.getProduct3());
                                myFirebaseRef.child("date").setValue(System.currentTimeMillis()/1000L);

                                SellerHistoryItem order = new SellerHistoryItem();
                                order.setItem1(items.getProduct1());
                                order.setItem2(items.getProduct2());
                                order.setItem3(items.getProduct3());
                                order.setId(items.getId());
                                order.setDate(System.currentTimeMillis()/1000L + "");

                                SellerHistoryFragment.updateAdapter(order);


                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                System.out.println("The read failed: " + firebaseError.getMessage());
                            }
                        });

                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        });
        rowViewHolder.scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanNow(v);
            }
        });
    }


    /**
     * event handler for scan button
     * @param view view of the activity
     */
    public void scanNow(View view){
        IntentIntegrator integrator = new IntentIntegrator((Activity)context);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setResultDisplayDuration(0);
        integrator.setWide();  // Wide scanning rectangle, may work better for 1D barcodes
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();
    }



    public static void saveSingleResult(IntentResult scanningResult) {

        String scanContent = scanningResult.getContents();
        Firebase myFirebaseRef = new Firebase("https://torrid-heat-2650.firebaseio.com/orders_history/" + mCurrentViewHolder.idTextView.getText().toString() );
        myFirebaseRef.child(String.valueOf(System.currentTimeMillis() / 1000L)).setValue(scanContent);

    }
}