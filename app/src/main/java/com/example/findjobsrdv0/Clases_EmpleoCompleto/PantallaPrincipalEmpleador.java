package com.example.findjobsrdv0.Clases_EmpleoCompleto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.findjobsrdv0.GeneralesApp.PantallaConfiguracion;
import com.example.findjobsrdv0.GeneralesApp.PantallaModoUsuario;
import com.example.findjobsrdv0.GeneralesApp.PantallaNavegador;
import com.example.findjobsrdv0.Pantallas_CurriculosCompleto.PantallaCompararCurriculo;
import com.example.findjobsrdv0.Pantallas_CurriculosCompleto.PantallaCurriculosAplicados;
import com.example.findjobsrdv0.Pantallas_CurriculosCompleto.PantallaListaBuscarCurriculos;
import com.example.findjobsrdv0.Pantallas_CurriculosCompleto.PantallaListaCurriculosFavoritos;
import com.example.findjobsrdv0.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PantallaPrincipalEmpleador extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private ImageView photoImageViewEmpleador;
    private TextView nameTextViewEmpleador;
    private TextView emailTextViewEmpleador;
    private TextView tituloelegiropcionBuscador;
    private FirebaseUser user;

    private GoogleApiClient googleApiClientEmpleador;

    private FirebaseAuth firebaseAuthEmpleador;
    private FirebaseAuth.AuthStateListener firebaseAuthListenerEmpleador;

    private String EmpleadorConectado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal_empleador);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tituloelegiropcionBuscador = (TextView) findViewById(R.id.tvelegiropcionEmpleador);
        Typeface face = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fonts_robotos));
        tituloelegiropcionBuscador.setTypeface(face);

        EmpleadorConectado = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        photoImageViewEmpleador = (ImageView) header.findViewById(R.id.fotoperfilEmpleador);
        nameTextViewEmpleador = (TextView) header.findViewById(R.id.nombreTextEmpleador);
        emailTextViewEmpleador = (TextView) header.findViewById(R.id.CorreoTextEmpleador);

        TextView TextViewEmpleador = (TextView) header.findViewById(R.id.nombreTextEmpleador);

        SharedPreferences preferences = this.getSharedPreferences("UserPrefEmpleador", Context.MODE_PRIVATE);

        photoImageViewEmpleador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IrPerfilEmpleador(EmpleadorConectado);

            }
        });
        nameTextViewEmpleador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IrPerfilEmpleador(EmpleadorConectado);
            }
        });
        emailTextViewEmpleador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IrPerfilEmpleador(EmpleadorConectado);

            }
        });

        LinearLayout IrAnadirEmpleo = (LinearLayout) findViewById(R.id.lyAnadirEmpleoEmpleador);
        IrAnadirEmpleo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PantallaRegistrarEmpleos.class);
                startActivityForResult(intent, 0);
            }
        });

        LinearLayout IrEmpleosAnadidos = (LinearLayout) findViewById(R.id.lyEmpresasAñadidos);
        IrEmpleosAnadidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PantallaListaEmpleosAnadidos.class);
                startActivityForResult(intent, 0);
            }
        });

        LinearLayout IrBuscarEmpleosBE = (LinearLayout) findViewById(R.id.lyBuscarCurriculosEmpleador);
        IrBuscarEmpleosBE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PantallaListaBuscarCurriculos.class);
                startActivityForResult(intent, 0);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClientEmpleador = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        firebaseAuthEmpleador = FirebaseAuth.getInstance();
        firebaseAuthListenerEmpleador = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    setUserData(user);
                } else {
                    goRegInScreenEmpleador();
                }
            }
        };
    }

    private void setUserData(FirebaseUser user) {

        SharedPreferences preferences = this.getSharedPreferences("UserPrefEmpleador", Context.MODE_PRIVATE);
        String Nombre = preferences.getString("Nombre", "Nombre");
        String foto = preferences.getString("ImagenEmpresa", "ImagenEmpresa");

        nameTextViewEmpleador.setText(Nombre);
        if(!foto.equals(null) && !foto.isEmpty()){
            Glide.with(this).load(foto).into(photoImageViewEmpleador);
        }

        nameTextViewEmpleador.setText(user.getDisplayName());
        emailTextViewEmpleador.setText(user.getEmail());
        Glide.with(this).load(user.getPhotoUrl()).into(photoImageViewEmpleador);
    }

    public void IrPerfilEmpleador(String EmpleadorConectado) {
        SharedPreferences preferences = this.getSharedPreferences("UserPrefEmpleador", Context.MODE_PRIVATE);
        String Nombre = preferences.getString("Nombre", "Nombre");

        Intent intent = new Intent(this, PantallaPerfilEmpleador.class);
        intent.putExtra("EmpleadorConectado", EmpleadorConectado);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuthEmpleador.addAuthStateListener(firebaseAuthListenerEmpleador);
    }

    private void goRegInScreenEmpleador() {
        Intent intent = new Intent(this, PantallaModoUsuario.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

//elimina el usuario de la base de datos
    public void revoke(View view) {
        firebaseAuthEmpleador.signOut();

        Auth.GoogleSignInApi.revokeAccess(googleApiClientEmpleador).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    goRegInScreenEmpleador();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.not_revoke, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuthListenerEmpleador != null) {
            firebaseAuthEmpleador.removeAuthStateListener(firebaseAuthListenerEmpleador);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_aplicar_verificacion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_verificacion) {
            Intent intent = new Intent(this, PantallaAplicarVerificacionEmpleador.class);
            startActivityForResult(intent, 0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.curriculosfavoritos) {
            Intent intent = new Intent(this, PantallaListaCurriculosFavoritos.class);
            startActivity(intent);

        } else if (id == R.id.curriculosMarcadoInteres) {
            Intent intent = new Intent(this, PantallaCurriculosAplicados.class);
            startActivity(intent);

        } else if (id == R.id.navegadorEmpleador) {
            Intent intent = new Intent(this, PantallaNavegador.class);
            startActivity(intent);

        } else if (id == R.id.compararEmpleador) {
            Intent intent = new Intent(this, PantallaCompararCurriculo.class);
            startActivity(intent);

        } else if (id == R.id.ConfiguracionEmpleador) {
            Intent intent = new Intent(this, PantallaConfiguracion.class);
            startActivityForResult(intent, 0);

        } else if (id == R.id.compartirEmpleador) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Si dispone de una y/o quieres publicar alguna oferta de empleo, Descarga ---> https://play.google.com/store/apps/details?id=com.FindJobsRD");
            startActivity(Intent.createChooser(intent, "Share with"));

        } else if (id == R.id.cerrarsesionEmpleador) {
            firebaseAuthEmpleador.signOut();

            Auth.GoogleSignInApi.signOut(googleApiClientEmpleador).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if (status.isSuccess()) {
                        Toast.makeText(getApplicationContext(), "La sesión se cerro con exito", Toast.LENGTH_SHORT).show();
                        goRegInScreenEmpleador();

                    } else {
                        Toast.makeText(getApplicationContext(), R.string.not_close_session, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
