package com.example.findjobsrdv0.Modelo;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AreasViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

    public TextView NombreArea;
    public TextView DescripcionArea;
    public TextView SubAreasA;
    public ImageView imagenArea;

    private ItemClickListener itemClickListener;


    public AreasViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void onClick(View v) {

    }
}
