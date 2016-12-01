package vktest.test.com.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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


public class SellerProductsFragment extends Fragment {
    RecyclerView recyclerView;

    RecyclerViewAdapter adapter;
    public SellerProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_productlist, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new RecyclerViewAdapter(getContext(), getData());
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddProductActivity.class);
                startActivity(i);
            }
        });



        return v;
    }

    public ArrayList<MenuSingleItems> getData()
    {
        final ArrayList<MenuSingleItems> it = new ArrayList<MenuSingleItems>();
        final Firebase myFirebaseRef = new Firebase("https://torrid-heat-2650.firebaseio.com/products");
        myFirebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    MenuSingleItems order = new MenuSingleItems();
                    order.setItem1(child.child("product1").getValue().toString());
                    order.setItem2(child.child("product2").getValue().toString());
                    order.setItem3(child.child("product3").getValue().toString());
                    order.setPrice(child.child("price").getValue().toString());
                    order.setImage(child.child("image_id").getValue().toString());
                    it.add(order);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });


        return it;
    }
}
