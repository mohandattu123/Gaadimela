package com.example.saikrishna.gaadimela;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.saikrishna.gaadimela.Common.Common;
import com.example.saikrishna.gaadimela.Interface.ItemClickListener;
import com.example.saikrishna.gaadimela.Model.Car;
import com.example.saikrishna.gaadimela.Model.Category;
import com.example.saikrishna.gaadimela.Model.User;
import com.example.saikrishna.gaadimela.ViewHolder.CarViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.rengwuxian.materialedittext.MaterialMultiAutoCompleteTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CarList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager  layoutManager;

    FirebaseDatabase database;
    DatabaseReference carList;

    String categoryId="";

    FirebaseRecyclerAdapter<Car,CarViewHolder> adapter;

    //search functionality
    FirebaseRecyclerAdapter<Car,CarViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);

        database = FirebaseDatabase.getInstance();
        carList = database.getReference("Cars");

        recyclerView = (RecyclerView)findViewById(R.id.recycler_car);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //get intent here
        if (getIntent()!=null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if(!categoryId.isEmpty() && categoryId!=null)
        {
            if(Common.isConnectToInternet(getBaseContext()))
            loadListCar(categoryId);
            else {
                Toast.makeText(CarList.this, "Please check your connection !!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        materialSearchBar = (MaterialSearchBar)findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter the car name");
        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String>suggest = new ArrayList<String>();
                for(String search:suggestList){
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled){
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<Car, CarViewHolder>(Car.class,R.layout.car_item,CarViewHolder.class,carList.orderByChild("Name").equalTo(text.toString())) {
            @Override
            protected void populateViewHolder(CarViewHolder viewHolder, Car model, int position) {
                viewHolder.car_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.car_image);

                final Car local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent carDetail = new Intent(CarList.this,CarDetail.class);
                        carDetail.putExtra("CarId",searchAdapter.getRef(position).getKey());
                        startActivity(carDetail);

                    }
                });
            }
        };
        recyclerView.setAdapter(searchAdapter);
    }

    private void loadSuggest() {
        carList.orderByChild("menuId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    Car item = postSnapshot.getValue(Car.class);
                    suggestList.add(item.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadListCar(String categoryId) {

        adapter = new FirebaseRecyclerAdapter<Car, CarViewHolder>(Car.class, R.layout.car_item, CarViewHolder.class,
                        carList.orderByChild("menuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(CarViewHolder viewHolder, Car model, int position) {
                viewHolder.car_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.car_image);

                final Car local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent carDetail = new Intent(CarList.this,CarDetail.class);
                        carDetail.putExtra("CarId",adapter.getRef(position).getKey());
                        startActivity(carDetail);
                        //Toast.makeText(CarList.this, ""+position, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        //set adapter
        recyclerView.setAdapter(adapter);
    }
}
