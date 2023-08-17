package com.itoasis.callingapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itoasis.callingapp.R;
import com.itoasis.callingapp.adapter.PaymentAdapter;
import com.itoasis.callingapp.modal.PaymentModal;

import java.util.ArrayList;


public class Payment extends Fragment {

    private RecyclerView courseRV;

    // variable for our adapter
    // class and array list
    private PaymentAdapter adapter;
    private ArrayList<PaymentModal> list;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_payment, container, false);
        courseRV = v.findViewById(R.id.recyclerPayment);
        buildRecyclerView();
        return v;

    }





    private void buildRecyclerView() {

        // below line we are creating a new array list
        list = new ArrayList<PaymentModal>();

        // below line is to add data to our array list.
        list.add(new PaymentModal("https://media.istockphoto.com/id/1464547965/photo/worker-thinking-or-typing-on-laptop-in-cafe-coffee-shop-or-restaurant-on-startup-ideas-vision.jpg?s=2048x2048&w=is&k=20&c=GPnra0Yal9RbRzRv76auIFBG20ErmkyPFosa3jzEthY=","Lorem Ipsum","4:37 PM","Rs. 250","Repeat"));
        list.add(new PaymentModal("https://media.istockphoto.com/id/1464547965/photo/worker-thinking-or-typing-on-laptop-in-cafe-coffee-shop-or-restaurant-on-startup-ideas-vision.jpg?s=2048x2048&w=is&k=20&c=GPnra0Yal9RbRzRv76auIFBG20ErmkyPFosa3jzEthY=","Lorem Ipsum","4:37 PM","Rs. 250","Repeat"));
        list.add(new PaymentModal("https://media.istockphoto.com/id/1464547965/photo/worker-thinking-or-typing-on-laptop-in-cafe-coffee-shop-or-restaurant-on-startup-ideas-vision.jpg?s=2048x2048&w=is&k=20&c=GPnra0Yal9RbRzRv76auIFBG20ErmkyPFosa3jzEthY=","Lorem Ipsum","4:37 PM","Rs. 250","Repeat"));
        list.add(new PaymentModal("https://media.istockphoto.com/id/1464547965/photo/worker-thinking-or-typing-on-laptop-in-cafe-coffee-shop-or-restaurant-on-startup-ideas-vision.jpg?s=2048x2048&w=is&k=20&c=GPnra0Yal9RbRzRv76auIFBG20ErmkyPFosa3jzEthY=","Lorem Ipsum","4:37 PM","Rs. 250","Repeat"));

        // initializing our adapter class.
        adapter = new PaymentAdapter(list,getContext());

        // adding layout manager to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        courseRV.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        courseRV.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        courseRV.setAdapter(adapter);
    }
}