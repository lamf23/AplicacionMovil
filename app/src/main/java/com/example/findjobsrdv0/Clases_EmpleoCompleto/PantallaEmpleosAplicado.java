package com.example.findjobsrdv0.Clases_EmpleoCompleto;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.findjobsrdv0.Adaptadores_Empleador.AdapterEmpleo;
import com.example.findjobsrdv0.Adaptadores_Empleador.Empleos;
import com.example.findjobsrdv0.GeneralesApp.ItemClickListener;
import com.example.findjobsrdv0.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PantallaEmpleosAplicado extends AppCompatActivity {

    private FirebaseDatabase databaseEmpleosAplico;
    private DatabaseReference DBEmpleos, EmpleosAplicadoDB;

    private AdapterEmpleo adapterEmpleo;
    private ArrayList<Empleos> mDatasetEmpleos = new ArrayList<Empleos>();

    private String sIdPersonaAplico = "";
    private RecyclerView recyclerViewEmpleos;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseAuth mAuthPersonaAplico;
    private FirebaseUser user;

    private String sIDEmpleoPEA, sNombreEmpleoPEA, sNombreEmpresaPEA, sProvinciaPEA,
            sDireccionPEA, sTelefonoPEA, sPaginaWebPEA, sEmailPEA, sSalarioPEA, sOtrosDatosPEA,
            sHorarioPEA, sFechaPublicacionPEA, sMostrarIdiomaPEA, sAreaPEA,
            sFormacionAcademicaPEA, sAnosExperienciaPEA, sSexoRequeridoPEA, sRangoPEA,
            sJornadaPEA, sCantidadVacantesPEA, sTipoContratoPEA, sEstadoEmpleoPEA,
            sPersonasAplicaronPEA, sImagenEmpleoPEA, sIdEmpleadorPEA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_empleos_aplicado);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        databaseEmpleosAplico = FirebaseDatabase.getInstance();
        DBEmpleos = databaseEmpleosAplico.getReference();
        EmpleosAplicadoDB = databaseEmpleosAplico.getReference(getResources().getString(R.string.Ref_EmpleosConCandidatos));

        recyclerViewEmpleos = (RecyclerView) findViewById(R.id.ListaEmpleosAplicadoR);

        recyclerViewEmpleos.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewEmpleos.setLayoutManager(layoutManager);

        //adapterEmpleo.mDatasetEmpleo.clear();
        //adapterEmpleo.notifyDataSetChanged();
//        mDatasetEmpleos.clear();
//        recyclerViewEmpleos.setAdapter(null);

        adapterEmpleo = new AdapterEmpleo(PantallaEmpleosAplicado.this, mDatasetEmpleos);
        recyclerViewEmpleos.setAdapter(adapterEmpleo);


        mAuthPersonaAplico = FirebaseAuth.getInstance();
        user = mAuthPersonaAplico.getCurrentUser();
        sIdPersonaAplico = user.getUid();

        if (sIdPersonaAplico != null) {
            if (!sIdPersonaAplico.isEmpty()) {
                //recyclerViewEmpleos.setAdapter(null);
                adapterEmpleo.mDatasetEmpleo.clear();
                TraerAplicacionesEmpleo(sIdPersonaAplico);
                //recyclerViewEmpleos.setAdapter(null);
                adapterEmpleo.mDatasetEmpleo.clear();
            }
        }
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void TraerAplicacionesEmpleo(String sPersonaIdE) {

        Query q = EmpleosAplicadoDB.orderByChild(getResources().getString(R.string.Aplicacion_sIdPersonaAplico)).equalTo(sPersonaIdE);
        //adapterEmpleo.mDatasetEmpleo.clear();

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("dataAplicacionesEmpleo", String.valueOf(dataSnapshot));


                Log.d("juan", String.valueOf(mDatasetEmpleos));
                Log.d("juanklk", String.valueOf(adapterEmpleo));
                for (DataSnapshot CurriculosSnapshot : dataSnapshot.getChildren()) {
                    String IdEmpleoAplico = CurriculosSnapshot.child(getResources().getString(R.string.Aplicacion_sIdEmpleoAplico)).getValue(String.class);
                    loadEmpleo(IdEmpleoAplico);
                    //recyclerViewEmpleos.setAdapter(null);
                    Log.d("dataidEmpleos", IdEmpleoAplico);
                }
                //adapterEmpleo.mDatasetEmpleo.clear();
                //recyclerViewEmpleos.setAdapter(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        adapterEmpleo.mDatasetEmpleo.clear();

    }

    private void loadEmpleo(final String sIDEmpleo) {
        DBEmpleos.child(getResources().getString(R.string.Ref_Empleos)).orderByChild(getResources().getString(R.string.Campo_sIDEmpleo)).equalTo(sIDEmpleo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                    Empleos DatosEmpleos = datasnapshot.getValue(Empleos.class);

                    sIDEmpleoPEA = DatosEmpleos.getsIDEmpleo();
                    sNombreEmpleoPEA = DatosEmpleos.getsNombreEmpleoE();
                    sNombreEmpresaPEA = DatosEmpleos.getsNombreEmpresaE();
                    sProvinciaPEA = DatosEmpleos.getsProvinciaE();
                    sDireccionPEA = DatosEmpleos.getsDireccionE();
                    sTelefonoPEA = DatosEmpleos.getsTelefonoE();
                    sPaginaWebPEA = DatosEmpleos.getsPaginaWebE();
                    sEmailPEA = DatosEmpleos.getsEmailE();
                    sSalarioPEA = DatosEmpleos.getsSalarioE();
                    sOtrosDatosPEA = DatosEmpleos.getsOtrosDatosE();
                    sHorarioPEA = DatosEmpleos.getsHorarioE();
                    sFechaPublicacionPEA = DatosEmpleos.getsFechaPublicacionE();
                    sMostrarIdiomaPEA = DatosEmpleos.getsMostrarIdiomaE();
                    sAreaPEA = DatosEmpleos.getsAreaE();
                    sFormacionAcademicaPEA = DatosEmpleos.getsFormacionAcademicaE();
                    sAnosExperienciaPEA = DatosEmpleos.getsAnosExperienciaE();
                    sSexoRequeridoPEA = DatosEmpleos.getsSexoRequeridoE();
                    sRangoPEA = DatosEmpleos.getsRangoE();
                    sJornadaPEA = DatosEmpleos.getsJornadaE();
                    sCantidadVacantesPEA = DatosEmpleos.getsCantidadVacantesE();
                    sEstadoEmpleoPEA = DatosEmpleos.getsEstadoEmpleoE();
                    sPersonasAplicaronPEA = DatosEmpleos.getsEstadoAdminE();
                    sIdEmpleadorPEA = DatosEmpleos.getsIdEmpleadorE();
                    sImagenEmpleoPEA = DatosEmpleos.getsImagenEmpleoE();
                    sTipoContratoPEA = DatosEmpleos.getsTipoContratoE();

                    Log.d("DATOS::::", datasnapshot.child(getResources().getString(R.string.Campo_sNombreEmpleoE)).getValue(String.class));
                    Log.d("DATOS::::", sNombreEmpleoPEA);


                    final Empleos empleos = new Empleos(sIDEmpleoPEA, sNombreEmpleoPEA, sNombreEmpresaPEA, sProvinciaPEA,
                            sDireccionPEA, sTelefonoPEA, sPaginaWebPEA, sEmailPEA, sSalarioPEA, sOtrosDatosPEA,
                            sHorarioPEA, sFechaPublicacionPEA, sMostrarIdiomaPEA, sAreaPEA,
                            sFormacionAcademicaPEA, sAnosExperienciaPEA, sSexoRequeridoPEA, sRangoPEA,
                            sJornadaPEA, sCantidadVacantesPEA, sTipoContratoPEA, sEstadoEmpleoPEA,
                            sPersonasAplicaronPEA, sImagenEmpleoPEA, sIdEmpleadorPEA);

                    //recyclerViewEmpleos.setAdapter(null);

                    PantallaEmpleosAplicado.this.mDatasetEmpleos.add(empleos);
                    adapterEmpleo.notifyDataSetChanged();
                    final Empleos clickItem = empleos;
                    adapterEmpleo.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongClick) {
                            Intent intent = new Intent(PantallaEmpleosAplicado.this, PantallaDetallesEmpleo.class);
                            intent.putExtra("sEmpleoIdBuscado", adapterEmpleo.mDatasetEmpleo.get(position).getsIDEmpleo());
                            startActivity(intent);
                        }
                    });
                }
                Log.d("CVEMPLEO::::", String.valueOf(dataSnapshot));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
