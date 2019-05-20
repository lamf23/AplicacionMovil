package com.example.findjobsrdv0.VistaCurriculo_RecyclerView.Vista_Curriculo_Principal.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findjobsrdv0.Modelo.ItemClickListener;
import com.example.findjobsrdv0.R;

public class VistaCurriculoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView txtNombre,  txtCedula, txtProvincia, txtDireccion, txtGradoMayor, txtEstadoActual;

   // public Button btninformacionbasicasCurriculo, btnformacionacadCurriculo;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    public VistaCurriculoViewHolder(@NonNull View itemView) {
        super( itemView );

        txtNombre = (TextView)itemView.findViewById( R.id.textViewNombreC );
        txtCedula = (TextView) itemView.findViewById( R.id.textViewCedulaC);
        txtDireccion = (TextView) itemView.findViewById( R.id.textViewDireccionC);
        txtEstadoActual = (TextView) itemView.findViewById( R.id.textViewEstadoActualCurr);
        txtGradoMayor = (TextView) itemView.findViewById( R.id.textViewMaestriaC);
        txtProvincia = (TextView) itemView.findViewById( R.id.textViewProvinciaC);
/*
        btninformacionbasicasCurriculo = (Button) itemView.findViewById( R.id.xmlBtnirAldetalleinformacionbasicascurriculo );
        btnformacionacadCurriculo = (Button) itemView.findViewById( R.id.xmlBtnirAldetalleFormacionacdcurriculo );
*/
        itemView.setOnClickListener( this );
    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick( view,getAdapterPosition(),false );
    }


}
