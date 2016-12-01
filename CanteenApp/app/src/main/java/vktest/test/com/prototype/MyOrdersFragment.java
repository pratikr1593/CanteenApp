package vktest.test.com.prototype;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class MyOrdersFragment extends Fragment {
    static ArrayList<Order> itemsList;
    private static Context context;
    public MyOrdersFragment() {

    }

    static RecyclerView recyclerView;
    static MyOrdersAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyOrdersFragment.context = getContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.my_orders, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.my_orders_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyOrdersAdapter(getContext(), getData());
        recyclerView.setAdapter(adapter);


        return v;
    }

    public ArrayList<Order> getData()
    {

        itemsList = new ArrayList<>();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String customerID = pref.getString("customerID", "0");
        if(customerID.equals("0")) {
            return null;
        }
        final Firebase myFirebaseRef = new Firebase("https://torrid-heat-2650.firebaseio.com/orders/" + customerID);
        myFirebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    Order order = new Order();
                    Log.d("product1", child.child("product1").getValue().toString());


                    order.setProduct1(child.child("product1").getValue().toString());
                    order.setProduct2(child.child("product2").getValue().toString());
                    order.setProduct3(child.child("product3").getValue().toString());
                    order.setId(child.child("id").getValue().toString());
                    order.setPrice(child.child("price").getValue().toString());

                    itemsList.add(order);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });

        return itemsList;

    }

    public static void updateAdapter(Order orderToAdd)
    {

        itemsList.add(orderToAdd);
        recyclerView.setAdapter(new MyOrdersAdapter(context, itemsList));

    }

}
