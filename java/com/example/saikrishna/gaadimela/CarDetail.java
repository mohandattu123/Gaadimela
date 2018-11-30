package com.example.saikrishna.gaadimela;

import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.saikrishna.gaadimela.Common.Common;
import com.example.saikrishna.gaadimela.Database.Database;
import com.example.saikrishna.gaadimela.Model.Car;
import com.example.saikrishna.gaadimela.Model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CarDetail extends AppCompatActivity {

    TextView car_name,car_price,car_description;
    ImageView car_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String carId="";

    FirebaseDatabase database;
    DatabaseReference cars;

    Car currentCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);


        database = FirebaseDatabase.getInstance();
        cars = database.getReference("Cars");


        numberButton = (ElegantNumberButton)findViewById(R.id.number_button);
        btnCart = (FloatingActionButton)findViewById(R.id.btnCart);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToCart(new Order(
                        carId,
                        currentCar.getName(),
                        numberButton.getNumber(),
                        currentCar.getPrice(),
                        currentCar.getDiscount()
                ));

                Toast.makeText(CarDetail.this,"Added to Cart",Toast.LENGTH_SHORT).show();
            }
        });

        car_description = (TextView)findViewById(R.id.car_description);
        car_name = (TextView)findViewById(R.id.car_name);
        car_price = (TextView)findViewById(R.id.car_price);
        car_image = (ImageView)findViewById(R.id.img_car);


        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);


        if(getIntent() !=null)
            carId = getIntent().getStringExtra("CarId");
        if(!carId.isEmpty()){
            if(Common.isConnectToInternet(getBaseContext()))
              getDetailCar(carId);
            else {
                Toast.makeText(CarDetail.this, "Please check your connection !!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void getDetailCar(String carId) {
        cars.child(carId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentCar=dataSnapshot.getValue(Car.class);

                Picasso.with(getBaseContext()).load( currentCar.getImage()).into(car_image);

                collapsingToolbarLayout.setTitle( currentCar.getName());

                car_price.setText( currentCar.getPrice());

                car_name.setText( currentCar.getName());

                car_description.setText( currentCar.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
