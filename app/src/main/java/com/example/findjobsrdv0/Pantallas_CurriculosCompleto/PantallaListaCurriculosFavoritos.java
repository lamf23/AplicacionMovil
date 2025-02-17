package com.example.findjobsrdv0.Pantallas_CurriculosCompleto;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.findjobsrdv0.GeneralesApp.ItemClickListener;
import com.example.findjobsrdv0.Adaptadores_Curriculo_Buscador.AdapterCurriculo;
import com.example.findjobsrdv0.Adaptadores_Curriculo_Buscador.Curriculos;
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

public class PantallaListaCurriculosFavoritos extends AppCompatActivity {

    private FirebaseDatabase databaseCurriculoFav;
    private DatabaseReference vistaCurriculoFavoritos, DBCurriculoFavoritos;

    private String sEmpresaIdFav = "";

    private AdapterCurriculo adapterCurriculoFavorito;
    private ArrayList<Curriculos> mDatasetCurriculo = new ArrayList<Curriculos>();

    private RecyclerView recyclerViewCurriculosFav;
    private RecyclerView.LayoutManager layoutManager;

    private String IdCurriculoFavorito, IdBuscadorCurriculoFav, ImagenCurriculoFav,
            NombreCurriculoFav, ApellidoCurriculoFav, CedulaCurriculoFav,
            EmailCurriculoFav, TelefonoCurriculoFav, CelularCurriculoFav,
            ProvinciaCurriculoFav, EstadoCivilCurriculoFav, DireccionCurriculoFav,
            OcupacionCurriculoFav, IdiomaCurriculoFav, GradoMayorCurriculoFav,
            EstadoActualCurriculoFav, SexoCurriculoFav, HabilidadesCurriculoFav, FechaCurriculoFav, NivelPrimarioFormAcad, NivelSecundarioFormAcad, CarreraFormAcad , UniversidadesFormAcad ;

    private FirebaseAuth mAuthEmpresaFav;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_lista_curriculos_favoritos);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        databaseCurriculoFav = FirebaseDatabase.getInstance();
        vistaCurriculoFavoritos = databaseCurriculoFav.getReference();
        DBCurriculoFavoritos = databaseCurriculoFav.getReference();

        recyclerViewCurriculosFav = (RecyclerView) findViewById(R.id.ListaCurriculosFavoritosR);

        recyclerViewCurriculosFav.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewCurriculosFav.setLayoutManager(layoutManager);

        adapterCurriculoFavorito = new AdapterCurriculo(PantallaListaCurriculosFavoritos.this, mDatasetCurriculo);
        recyclerViewCurriculosFav.setAdapter(adapterCurriculoFavorito);

        mAuthEmpresaFav = FirebaseAuth.getInstance();
        user = mAuthEmpresaFav.getCurrentUser();
        sEmpresaIdFav = user.getUid();

        if (sEmpresaIdFav != null) {
            if (!sEmpresaIdFav.isEmpty()) {
                Log.d("dataAplicacionesempresa", sEmpresaIdFav);
                TraerCurriculosFav(sEmpresaIdFav);
            }
        }
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void TraerCurriculosFav(String sEmpresaId) {
        Query q = DBCurriculoFavoritos.child(getResources().getString(R.string.Ref_EmpleadoresConFavoritos))
                .child(sEmpresaId)
                .child(getResources().getString(R.string.Favoritos_LIKES));
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("dataAplicacionesempresa", String.valueOf(dataSnapshot));
                for (DataSnapshot CurriculosSnapshot : dataSnapshot.getChildren()) {
                    String IdCurriculoAplico = CurriculosSnapshot.child(getResources().getString(R.string.Favoritos_IdCurriculoLike)).getValue(String.class);
                    loadCurriculoFavorito(IdCurriculoAplico);
                    Log.d("dataidcurriculo", IdCurriculoAplico);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadCurriculoFavorito(final String sIdFavCurriculo) {
        vistaCurriculoFavoritos.child(getResources().getString(R.string.Ref_Curriculos)).orderByChild(getResources().getString(R.string.Campo_sIdCurriculo)).equalTo(sIdFavCurriculo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {

                    Curriculos DatosCurriculoFav = datasnapshot.getValue(Curriculos.class);

                    ApellidoCurriculoFav = DatosCurriculoFav.getsApellidoC();
                    NombreCurriculoFav = DatosCurriculoFav.getsNombreC();
                    CedulaCurriculoFav = DatosCurriculoFav.getsCedulaC();
                    EmailCurriculoFav = DatosCurriculoFav.getsEmailC();
                    TelefonoCurriculoFav = DatosCurriculoFav.getsTelefonoC();
                    ProvinciaCurriculoFav = DatosCurriculoFav.getsProvinciaC();
                    EstadoCivilCurriculoFav = DatosCurriculoFav.getsEstadoCivilC();
                    DireccionCurriculoFav = DatosCurriculoFav.getsDireccionC();
                    OcupacionCurriculoFav = DatosCurriculoFav.getsOcupacionC();
                    IdiomaCurriculoFav = DatosCurriculoFav.getsIdiomaC();
                    GradoMayorCurriculoFav = DatosCurriculoFav.getsGradoMayorC();
                    CelularCurriculoFav = DatosCurriculoFav.getsCelularC();
                    EstadoActualCurriculoFav = DatosCurriculoFav.getsEstadoActualC();
                    SexoCurriculoFav = DatosCurriculoFav.getsSexoC();
                    HabilidadesCurriculoFav = DatosCurriculoFav.getsHabilidadesC();
                    FechaCurriculoFav = DatosCurriculoFav.getsFechaC();
                    IdCurriculoFavorito = DatosCurriculoFav.getsIdCurriculo();
                    ImagenCurriculoFav = DatosCurriculoFav.getsImagenC();
                    NivelPrimarioFormAcad = DatosCurriculoFav.getsNivelPrimarioFormAcad();
                    NivelSecundarioFormAcad = DatosCurriculoFav.getsNivelSecundarioFormAcad();
                    CarreraFormAcad = DatosCurriculoFav.getsCarreraFormAcad();
                    UniversidadesFormAcad = DatosCurriculoFav.getsUniversidadFormAcad();

                    final Curriculos cvFav = new Curriculos(IdCurriculoFavorito, ImagenCurriculoFav, NombreCurriculoFav, ApellidoCurriculoFav, CedulaCurriculoFav,
                            EmailCurriculoFav, TelefonoCurriculoFav, CelularCurriculoFav,
                            ProvinciaCurriculoFav, EstadoCivilCurriculoFav, DireccionCurriculoFav,
                            OcupacionCurriculoFav, IdiomaCurriculoFav, GradoMayorCurriculoFav,
                            EstadoActualCurriculoFav, SexoCurriculoFav, HabilidadesCurriculoFav,
                            FechaCurriculoFav, "se utilizara para el estado del curriculo, administrador, etc", NivelPrimarioFormAcad, NivelSecundarioFormAcad, CarreraFormAcad, UniversidadesFormAcad );

                    PantallaListaCurriculosFavoritos.this.mDatasetCurriculo.add(cvFav);
                    adapterCurriculoFavorito.notifyDataSetChanged();

                    final Curriculos clickItem = cvFav;
                    adapterCurriculoFavorito.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongClick) {
                            Toast.makeText(PantallaListaCurriculosFavoritos.this, "Spinner vacío, por favor seleccione una Provincia", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(PantallaListaCurriculosFavoritos.this, DetalleCurriculo.class);
                            intent.putExtra("detallecurrID", adapterCurriculoFavorito.mDatasetCurriculo.get(position).getsIdCurriculo());
                            startActivity(intent);
                        }
                    });
                }
                Log.d("CVPERSONASFAV::::", String.valueOf(dataSnapshot));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
