package com.example.saikrishna.gaadimela.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saikrishna.gaadimela.Interface.ItemClickListener;
import com.example.saikrishna.gaadimela.R;

public class CarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView car_name;
    public ImageView car_image;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CarViewHolder(View itemView) {
        super(itemView);

        car_name = (TextView)itemView.findViewById(R.id.car_name);
        car_image = (ImageView)itemView.findViewById(R.id.car_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
