package vktest.test.com.prototype;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class SellerOrdersFragment extends Fragment {

    RecyclerView recyclerView;

    TextViewAdapter adapter;
    private Toolbar toolbar;
    public SellerOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Firebase.setAndroidContext(this);
        View v =  inflater.inflate(R.layout.fragment_menu_seller_orders, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewNew);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TextViewAdapter(getContext(), getOrders());
        System.out.println(adapter.getItemCount());
        recyclerView.setAdapter(adapter);
        return v;

    }


    public ArrayList<Order> getOrders() {

        final ArrayList<Order> itemsList = new ArrayList<>();
        final Firebase myFirebaseRef = new Firebase("https://torrid-heat-2650.firebaseio.com/orders");
        myFirebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Log.d("CHECK1", snapshot.getChildrenCount() + "");
                for (DataSnapshot root : snapshot.getChildren())
                    for (DataSnapshot child : root.getChildren()) {

                        Order order = new Order();
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


}
