package vktest.test.com.prototype;

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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.UUID;

public class RecyclerViewSellerAdapter extends RecyclerView.Adapter<RowViewHolder> {
    Context context;
    ArrayList<MenuSingleItems> itemsList;
    String customerID;

    public RecyclerViewSellerAdapter(Context context, ArrayList<MenuSingleItems> itemsList) {
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
        View view = inflater.inflate(R.layout.single_row_seller, null);
        RowViewHolder viewHolder = new RowViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RowViewHolder rowViewHolder, int position) {
        final MenuSingleItems items = itemsList.get(position);
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
                builder.setTitle("Deleting product");
                String message = "Are you sure?";
                String generatedString = UUID.randomUUID().toString();

                builder.setMessage(message);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final Firebase myFirebaseRef = new Firebase("https://torrid-heat-2650.firebaseio.com/products/" + items.getId());
                        myFirebaseRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                for (DataSnapshot root : snapshot.getChildren())
                                    for (DataSnapshot child : root.getChildren()) {
                                        if((child.child("id").getValue().toString().equals(items.getId()))){

                                            final Firebase myFirebaseRef = new Firebase("https://torrid-heat-2650.firebaseio.com/orders/" + root.getKey() + "/"
                                                    + items.getId());
                                            myFirebaseRef.removeValue();

                                        }

                                    }

                                itemsList.remove(items);
                                notifyDataSetChanged();



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


    }


}