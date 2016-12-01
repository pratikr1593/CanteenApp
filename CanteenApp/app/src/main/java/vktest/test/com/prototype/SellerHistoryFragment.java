package vktest.test.com.prototype;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class SellerHistoryFragment extends Fragment {
    static ArrayList<SellerHistoryItem> itemsList;
    private static Context context;
    public SellerHistoryFragment() {

    }

    static RecyclerView recyclerView;
    static SellerHistoryAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SellerHistoryFragment.context = getContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.seller_history, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.seller_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SellerHistoryAdapter(getContext(), getData());
        recyclerView.setAdapter(adapter);


        return v;
    }

    public ArrayList<SellerHistoryItem> getData()
    {

        itemsList = new ArrayList<>();

        final Firebase myFirebaseRef = new Firebase("https://torrid-heat-2650.firebaseio.com/orders_history");
        myFirebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    SellerHistoryItem order = new SellerHistoryItem();
                    order.setItem1(child.child("product1").getValue().toString());
                    order.setItem2(child.child("product2").getValue().toString());
                    order.setItem3(child.child("product3").getValue().toString());
                    order.setId(child.child("id").getValue().toString());
                    order.setDate(child.child("date").getValue().toString());

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

    public static void updateAdapter(SellerHistoryItem orderToAdd){

        itemsList.add(orderToAdd);
        recyclerView.setAdapter(new SellerHistoryAdapter(context, itemsList));

    }

}
