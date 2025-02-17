package com.example.findjobsrdv0.Clases_EmpleoCompleto;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findjobsrdv0.Adaptadores_Empleador.Empleadores;
import com.example.findjobsrdv0.Pantallas_CurriculosCompleto.cPantallaRegistrarCurriculo;
import com.example.findjobsrdv0.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class PantallaPerfilEmpleador extends AppCompatActivity {

    private EditText editNombrePEmpleador, editRncPEmpleador, editPaginaWebPEmpleador, editTelefonoPEmplador,
            editDireccionPEmpleador, editCorreoPEmpleador, editProvinciaPEmpleador, editDescripcionPEmpleador;

    private String sNombrePEmpleador, sRncPEmpleador, sPaginaWebPEmpleador, sTelefonoPEmpleador,
            sDireccionPEmpleador, sCorreoPEmpleador, sProvinciaPEmpleador, sDescripcionPEmpleador, sImagenPEmpleador;

    private String sIdPEmpleador = "";

    private Boolean sVerificacionPEmpleador;

    private Button btnVerificacionPEmpleador;

    private ImageView ImagePerfilPEmpleador;

    private int IMAGE_REQUEST_CODE = 5;

    private FirebaseDatabase database;
    private DatabaseReference DBperfilEmpleadores;

    private DatabaseReference DBImagen;


    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private String mStoragePath = "ImagenesPerfilesEmpleadores/";
    private String mDatabasePath = "Empleadores";

    private Uri mFilePathUri;

    StorageReference mStorageReference;
    ProgressDialog mProgressDialog;

    private String TelefonoEmpleador, EmailEmpleador, NombreEmpleador, FotoPerfilCorreo;

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_perfil_empleador_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.setTitle("Perfil Empleador");

        Window w = getWindow();
        w.setTitle("My title");

        mStorageReference = getInstance().getReference();
        mProgressDialog = new ProgressDialog(PantallaPerfilEmpleador.this);

        database = FirebaseDatabase.getInstance();
        DBperfilEmpleadores = database.getReference(getResources().getString(R.string.Ref_Empleadores));

        DBImagen = FirebaseDatabase.getInstance().getReference( "Empleadores" );


        user = firebaseAuth.getInstance().getCurrentUser();

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        editNombrePEmpleador = (EditText) findViewById(R.id.xmleditNombreEmpleador);
        editRncPEmpleador = (EditText) findViewById(R.id.xmleditRNCEmpleador);
        editPaginaWebPEmpleador = (EditText) findViewById(R.id.xmleditPagWebEmpleador);
        editTelefonoPEmplador = (EditText) findViewById(R.id.xmleditTelefonoEmpleador);
        editDireccionPEmpleador = (EditText) findViewById(R.id.xmleditDireccionEmpleador);
        editCorreoPEmpleador = (EditText) findViewById(R.id.xmleditEmailEmpleador);
        editProvinciaPEmpleador = (EditText) findViewById(R.id.xmleditProvEmpleador);
        editDescripcionPEmpleador = (EditText) findViewById(R.id.xmleditDescEmpleador);

        ImagePerfilPEmpleador = (ImageView) findViewById(R.id.xmlImagePerfilEmpleador);
        ImagePerfilPEmpleador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "Seleccionar Imagen"), IMAGE_REQUEST_CODE);

            }
        });

        btnVerificacionPEmpleador = (Button) findViewById(R.id.xmlBtnVerificacionPerfEmp);
        btnVerificacionPEmpleador.setVisibility(View.INVISIBLE);

        editNombrePEmpleador.setEnabled(false);
        editRncPEmpleador.setEnabled(true);
        editPaginaWebPEmpleador.setEnabled(false);
        editTelefonoPEmplador.setEnabled(false);
        editDireccionPEmpleador.setEnabled(false);
        editCorreoPEmpleador.setEnabled(false);
        editProvinciaPEmpleador.setEnabled(false);
        editDescripcionPEmpleador.setEnabled(false);

        if (getIntent() != null) {
            sIdPEmpleador = getIntent().getStringExtra("EmpleadorConectado");
            if (!sIdPEmpleador.isEmpty()) {
                LoadDatosEmpleadores(sIdPEmpleador);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            mFilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mFilePathUri);
                ImagePerfilPEmpleador.setImageBitmap(bitmap);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean onSupportNavigateUp() {
       // super.onBackPressed();
        //finish();
        llmar();
        return true;
    }

    public void llmar(){
        Intent intent = new Intent(this, PantallaPrincipalEmpleador.class);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menuperfil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_EditarPerfil) {
            //process your onClick here
            ActivarCampor();
            return true;
        }
        if (id == R.id.menu_ActualizarPerfil) {
            //process your onClick here
            EnviarDatos();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void beginUpdate() {

        mProgressDialog.setTitle("Actualizando...");
        mProgressDialog.show();
        Revisando();
    }

    public void Revisando() {

        sNombrePEmpleador = editNombrePEmpleador.getText().toString().trim();
        sRncPEmpleador = editRncPEmpleador.getText().toString().trim();
        sPaginaWebPEmpleador = editPaginaWebPEmpleador.getText().toString().trim();
        sTelefonoPEmpleador = editTelefonoPEmplador.getText().toString().trim();
        sDireccionPEmpleador = editDireccionPEmpleador.getText().toString().trim();
        sCorreoPEmpleador = editCorreoPEmpleador.getText().toString().trim();
        sProvinciaPEmpleador = editProvinciaPEmpleador.getText().toString().trim();
        sDescripcionPEmpleador = editDescripcionPEmpleador.getText().toString().trim();


        if (TextUtils.isEmpty( sNombrePEmpleador )) {
            editNombrePEmpleador.setError( "Campo vacío, por favor escriba la Cedula " );
            Toast.makeText( PantallaPerfilEmpleador.this, "Campo Nombre vacío", Toast.LENGTH_LONG ).show();
            mProgressDialog.dismiss();
            return;
        }

        if (TextUtils.isEmpty( sRncPEmpleador )) {
            editRncPEmpleador.setError( "Campo vacío, por favor escriba el RNC " );
            Toast.makeText( PantallaPerfilEmpleador.this, "Campo RNC vacío", Toast.LENGTH_LONG ).show();
            mProgressDialog.dismiss();
            return;
        }
        if (TextUtils.isEmpty( sPaginaWebPEmpleador )) {
            editPaginaWebPEmpleador.setError( "Campo vacío, por favor escriba la Pagina Web " );
            Toast.makeText( PantallaPerfilEmpleador.this, "Campo Pagina Web vacío", Toast.LENGTH_LONG ).show();
            mProgressDialog.dismiss();
            return;
        }
        if (TextUtils.isEmpty( sTelefonoPEmpleador )) {
            editTelefonoPEmplador.setError( "Campo vacío, por favor escriba el telefono " );
            Toast.makeText( PantallaPerfilEmpleador.this, "Campo Telefono vacío", Toast.LENGTH_LONG ).show();
            mProgressDialog.dismiss();
            return;
        }

        if (TextUtils.isEmpty( sDireccionPEmpleador )) {
            editDireccionPEmpleador.setError( "Campo vacío, por favor escriba la direccion " );
            Toast.makeText( PantallaPerfilEmpleador.this, "Campo direccion vacío", Toast.LENGTH_LONG ).show();
            mProgressDialog.dismiss();
            return;
        }
        if (TextUtils.isEmpty( sCorreoPEmpleador )) {
            editCorreoPEmpleador.setError( "Campo vacío, por favor escriba el correo " );
            Toast.makeText( PantallaPerfilEmpleador.this, "Campo correo vacío", Toast.LENGTH_LONG ).show();
            mProgressDialog.dismiss();
            return;
        }
        if (TextUtils.isEmpty( sProvinciaPEmpleador )) {
            editProvinciaPEmpleador.setError( "Campo vacío, por favor escriba la provincia " );
            Toast.makeText( PantallaPerfilEmpleador.this, "Campo provincia vacío", Toast.LENGTH_LONG ).show();
            mProgressDialog.dismiss();
            return;
        }
        if (TextUtils.isEmpty( sDescripcionPEmpleador )) {
            editDescripcionPEmpleador.setError( "Campo vacío, por favor escriba la descripcion " );
            Toast.makeText( PantallaPerfilEmpleador.this, "Campo descripcion vacío", Toast.LENGTH_LONG ).show();
            mProgressDialog.dismiss();
            return;
        }

        DeleteImagenAnterior();

    }

    public void DeleteImagenAnterior() {
        if (sImagenPEmpleador != null && !sImagenPEmpleador.isEmpty()) {
            final StorageReference mPictureRef = getInstance().getReferenceFromUrl(sImagenPEmpleador);
            mPictureRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(PantallaPerfilEmpleador.this, "Eliminando Imagen...", Toast.LENGTH_LONG).show();
                    Log.d("link foto", String.valueOf(mPictureRef));
                    SubirNuevaImagen();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    SubirNuevaImagen();
//                    Toast.makeText(PantallaPerfilEmpleador.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                    mProgressDialog.dismiss();
                }
            });
        } else {

//            Toast.makeText(PantallaPerfilEmpleador.this, "No hay imagen agregada", Toast.LENGTH_LONG).show();
            SubirNuevaImagen();
        }
    }

    private void SubirNuevaImagen() {

        Query q = DBImagen.orderByChild( "sImagenEmpleador" ).equalTo( sImagenPEmpleador );
        q.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {

                    String imageName = System.currentTimeMillis() + ".png";
                    //String imageName = System.currentTimeMillis() + getFileExtension(mFilePathUri);

                    StorageReference storageReference2do = mStorageReference.child(mStoragePath + imageName);

                    Bitmap bitmap = ((BitmapDrawable) ImagePerfilPEmpleador.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

                    byte[] data = baos.toByteArray();
                    UploadTask uploadTask = storageReference2do.putBytes(data);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(PantallaPerfilEmpleador.this, "Nueva Imagen Subida...", Toast.LENGTH_LONG).show();
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            Uri downloadUri = uriTask.getResult();
                            ActualizarDatosEmpleador(downloadUri.toString());

                        }


                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PantallaPerfilEmpleador.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();
                        }
                    });

                } else {

                    String imageName = System.currentTimeMillis() + ".png";
                    //String imageName = System.currentTimeMillis() + getFileExtension(mFilePathUri);

                    StorageReference storageReference2do = mStorageReference.child(mStoragePath + imageName);

                    Bitmap bitmap = ((BitmapDrawable) ImagePerfilPEmpleador.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

                    byte[] data = baos.toByteArray();
                    UploadTask uploadTask = storageReference2do.putBytes(data);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(PantallaPerfilEmpleador.this, "Nueva Imagen Subida...", Toast.LENGTH_LONG).show();
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            Uri downloadUri = uriTask.getResult();
                            ActualizarDatosEmpleador(downloadUri.toString());

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PantallaPerfilEmpleador.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
    }

    private void ActivarCampor() {

        editNombrePEmpleador.setEnabled(true);
//        editRncPEmpleador.setEnabled(true);
        editPaginaWebPEmpleador.setEnabled(true);
        editTelefonoPEmplador.setEnabled(true);
        editDireccionPEmpleador.setEnabled(true);
        //editCorreoPEmpleador.setEnabled(false);
        editProvinciaPEmpleador.setEnabled(true);
        editDescripcionPEmpleador.setEnabled(true);
    }


    public void LoadDatosEmpleadores(String sIdEmpleador) {
        DBperfilEmpleadores.child(sIdEmpleador).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    editRncPEmpleador.setEnabled( false );

                    Empleadores Datosempleadores = dataSnapshot.getValue(Empleadores.class);
                    sImagenPEmpleador = Datosempleadores.getsImagenEmpleador();
                    if (sImagenPEmpleador != null && sImagenPEmpleador != "") {
                        Picasso.get().load(sImagenPEmpleador).into(ImagePerfilPEmpleador);
                    } else {
                        //FotoPerfilCorreo = user.getPhotoUrl();
                        if (user.getPhotoUrl() != null) {
                            Glide.with(PantallaPerfilEmpleador.this).load(user.getPhotoUrl()).into(ImagePerfilPEmpleador);
                        }
                    }

                    sNombrePEmpleador = Datosempleadores.getsNombreEmpleador();
                    if (sNombrePEmpleador != null && sNombrePEmpleador != "") {
                        editNombrePEmpleador.setText(sNombrePEmpleador);
                        //this.setTitle(sNombrePEmpleador);
                        //this.setTitle("klk");
                    } else {
                        NombreEmpleador = user.getDisplayName();
                        if (NombreEmpleador != null && NombreEmpleador != "") {
                            editNombrePEmpleador.setText(NombreEmpleador);
                        }
                    }

                    sTelefonoPEmpleador = Datosempleadores.getsTelefonoEmpleador();
                    if (sTelefonoPEmpleador != null && sTelefonoPEmpleador != "") {
                        editTelefonoPEmplador.setText(sTelefonoPEmpleador);
                    } else {
                        TelefonoEmpleador = user.getPhoneNumber();
                        if (TelefonoEmpleador != null && TelefonoEmpleador != "") {
                            editTelefonoPEmplador.setText(TelefonoEmpleador);
                        }
                    }

                    sCorreoPEmpleador = Datosempleadores.getsCorreoEmpleador();
                    if (sCorreoPEmpleador != null && sCorreoPEmpleador != "") {
                        editCorreoPEmpleador.setText(sCorreoPEmpleador);
                    } else {
                        EmailEmpleador = user.getEmail();
                        if (EmailEmpleador != null && EmailEmpleador != "") {
                            editCorreoPEmpleador.setText(EmailEmpleador);
                        }
                    }

                    sRncPEmpleador = Datosempleadores.getsRncEmpleador();
                    if (sRncPEmpleador != null && sRncPEmpleador != "") {
                        editRncPEmpleador.setText(sRncPEmpleador);
                    }

                    sPaginaWebPEmpleador = Datosempleadores.getsPaginaWebEmpleador();
                    if (sPaginaWebPEmpleador != null && sPaginaWebPEmpleador != "") {
                        editPaginaWebPEmpleador.setText(sPaginaWebPEmpleador);
                    }

                    sDireccionPEmpleador = Datosempleadores.getsDireccionEmpleador();
                    if (sDireccionPEmpleador != null && sDireccionPEmpleador != "") {
                        editDireccionPEmpleador.setText(sDireccionPEmpleador);
                    }

                    sProvinciaPEmpleador = Datosempleadores.getsProvinciaEmpleador();
                    if (sProvinciaPEmpleador != null && sProvinciaPEmpleador != "") {
                        editProvinciaPEmpleador.setText(sProvinciaPEmpleador);
                    }

                    sDescripcionPEmpleador = Datosempleadores.getsDescripcionEmpleador();
                    if (sDescripcionPEmpleador != null && sDescripcionPEmpleador != "") {
                        editDescripcionPEmpleador.setText(sDescripcionPEmpleador);
                    }

                    sVerificacionPEmpleador = Datosempleadores.getsVerificacionEmpleador();
                    //Log.d("verificacion", String.valueOf(sVerificacionPEmpleador));
                    if (sVerificacionPEmpleador != null) {

                        if (sVerificacionPEmpleador == true) {
                            btnVerificacionPEmpleador.setVisibility(View.VISIBLE);
                        }
                        if (sVerificacionPEmpleador == false) {
                            btnVerificacionPEmpleador.setVisibility(View.INVISIBLE);
                        }
                    }
                } else {

//                    editRncPEmpleador.setEnabled( true );

                    if (user.getPhotoUrl() != null) {
                        Glide.with(PantallaPerfilEmpleador.this).load(user.getPhotoUrl()).into(ImagePerfilPEmpleador);
                    }
                    NombreEmpleador = user.getDisplayName();
                    if (NombreEmpleador != null && NombreEmpleador != "") {
                        editNombrePEmpleador.setText(NombreEmpleador);
                    }
                    EmailEmpleador = user.getEmail();
                    if (EmailEmpleador != null && EmailEmpleador != "") {
                        editCorreoPEmpleador.setText(EmailEmpleador);
                    }
                    TelefonoEmpleador = user.getPhoneNumber();
                    if (TelefonoEmpleador != null && TelefonoEmpleador != "") {
                        editTelefonoPEmplador.setText(TelefonoEmpleador);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PantallaPerfilEmpleador.this, "Hubo un problema con traer los datos", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void EnviarDatos(){
        Query q = DBperfilEmpleadores.orderByChild( "sIdEmpleador" ).equalTo(sIdPEmpleador);
        q.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    RegistrarDatosEmpleador();
                }else {
                    beginUpdate();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
    }

    private void RegistrarDatosEmpleador(){

        mProgressDialog.setTitle("Actualizando...");
        mProgressDialog.show();

        if (mFilePathUri == null) {
            Toast.makeText( PantallaPerfilEmpleador.this, "Favor Seleccionar imagen", Toast.LENGTH_LONG ).show();
            mProgressDialog.dismiss();
            return;
        }

        final StorageReference StorageReference2nd = mStorageReference.child( mStoragePath + System.currentTimeMillis() + "." + getFileExtension( mFilePathUri ) );

        UploadTask uploadTask = StorageReference2nd.putFile( mFilePathUri );
        Task<Uri> urlTask = uploadTask.continueWithTask( new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return StorageReference2nd.getDownloadUrl();
            }
        } ).addOnCompleteListener( new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Uri downloadUri = task.getResult();
                    String downloadURL = downloadUri.toString();
                    Log.d( "url", downloadURL );
                    sNombrePEmpleador = editNombrePEmpleador.getText().toString().trim();
                    sRncPEmpleador = editRncPEmpleador.getText().toString().trim();
                    sPaginaWebPEmpleador = editPaginaWebPEmpleador.getText().toString().trim();
                    sTelefonoPEmpleador = editTelefonoPEmplador.getText().toString().trim();
                    sDireccionPEmpleador = editDireccionPEmpleador.getText().toString().trim();
                    sCorreoPEmpleador = editCorreoPEmpleador.getText().toString().trim();
                    sProvinciaPEmpleador = editProvinciaPEmpleador.getText().toString().trim();
                    sDescripcionPEmpleador = editDescripcionPEmpleador.getText().toString().trim();

                    if (TextUtils.isEmpty( sNombrePEmpleador )) {
                        editNombrePEmpleador.setError( "Campo vacío, por favor escriba la Cedula " );
                        Toast.makeText( PantallaPerfilEmpleador.this, "Campo Nombre vacío", Toast.LENGTH_LONG ).show();
                        mProgressDialog.dismiss();
                        return;
                    }

                    if (TextUtils.isEmpty( sRncPEmpleador )) {
                        editRncPEmpleador.setError( "Campo vacío, por favor escriba el RNC " );
                        Toast.makeText( PantallaPerfilEmpleador.this, "Campo RNC vacío", Toast.LENGTH_LONG ).show();
                        mProgressDialog.dismiss();
                        return;
                    }
                    if (TextUtils.isEmpty( sPaginaWebPEmpleador )) {
                        editPaginaWebPEmpleador.setError( "Campo vacío, por favor escriba la Pagina Web " );
                        Toast.makeText( PantallaPerfilEmpleador.this, "Campo Pagina Web vacío", Toast.LENGTH_LONG ).show();
                        mProgressDialog.dismiss();
                        return;
                    }
                    if (TextUtils.isEmpty( sTelefonoPEmpleador )) {
                        editTelefonoPEmplador.setError( "Campo vacío, por favor escriba el telefono " );
                        Toast.makeText( PantallaPerfilEmpleador.this, "Campo Telefono vacío", Toast.LENGTH_LONG ).show();
                        mProgressDialog.dismiss();
                        return;
                    }

                    if (TextUtils.isEmpty( sDireccionPEmpleador )) {
                        editDireccionPEmpleador.setError( "Campo vacío, por favor escriba la direccion " );
                        Toast.makeText( PantallaPerfilEmpleador.this, "Campo direccion vacío", Toast.LENGTH_LONG ).show();
                        mProgressDialog.dismiss();
                        return;
                    }
                    if (TextUtils.isEmpty( sCorreoPEmpleador )) {
                        editCorreoPEmpleador.setError( "Campo vacío, por favor escriba el correo " );
                        Toast.makeText( PantallaPerfilEmpleador.this, "Campo correo vacío", Toast.LENGTH_LONG ).show();
                        mProgressDialog.dismiss();
                        return;
                    }
                    if (TextUtils.isEmpty( sProvinciaPEmpleador )) {
                        editProvinciaPEmpleador.setError( "Campo vacío, por favor escriba la provincia " );
                        Toast.makeText( PantallaPerfilEmpleador.this, "Campo provincia vacío", Toast.LENGTH_LONG ).show();
                        mProgressDialog.dismiss();
                        return;
                    }
                    if (TextUtils.isEmpty( sDescripcionPEmpleador )) {
                        editDescripcionPEmpleador.setError( "Campo vacío, por favor escriba la descripcion " );
                        Toast.makeText( PantallaPerfilEmpleador.this, "Campo descripcion vacío", Toast.LENGTH_LONG ).show();
                        mProgressDialog.dismiss();
                        return;
                    }




                    Empleadores empleadores = new Empleadores( sNombrePEmpleador, sRncPEmpleador,
                            sPaginaWebPEmpleador, sTelefonoPEmpleador, sDireccionPEmpleador,
                            sCorreoPEmpleador, downloadURL, false, sIdPEmpleador,
                            sDescripcionPEmpleador, sProvinciaPEmpleador );
                    DBperfilEmpleadores.child( sIdPEmpleador ).setValue( empleadores );
                    mProgressDialog.dismiss();
                    Toast.makeText( PantallaPerfilEmpleador.this, "Sus Datos han Sido Actualizado", Toast.LENGTH_LONG ).show();
//                    Intent intent = new Intent( PantallaPerfilEmpleador.this, PantallaPrincipalEmpleador.class );
//                    startActivity( intent );

                    mProgressDialog.dismiss();

                }else {

                }
            }
        }).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                    mProgressDialog.dismiss();
                Toast.makeText( PantallaPerfilEmpleador.this, e.getMessage(), Toast.LENGTH_LONG ).show();
            }
        } );

    }
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType( contentResolver.getType( uri ) );
    }

    private void ActualizarDatosEmpleador(String foto) {

        sNombrePEmpleador = editNombrePEmpleador.getText().toString().trim();
        sRncPEmpleador = editRncPEmpleador.getText().toString().trim();
        sPaginaWebPEmpleador = editPaginaWebPEmpleador.getText().toString().trim();
        sTelefonoPEmpleador = editTelefonoPEmplador.getText().toString().trim();
        sDireccionPEmpleador = editDireccionPEmpleador.getText().toString().trim();
        sCorreoPEmpleador = editCorreoPEmpleador.getText().toString().trim();
        sProvinciaPEmpleador = editProvinciaPEmpleador.getText().toString().trim();
        sDescripcionPEmpleador = editDescripcionPEmpleador.getText().toString().trim();

        Empleadores empleadores = new Empleadores( sNombrePEmpleador, sRncPEmpleador,
                sPaginaWebPEmpleador, sTelefonoPEmpleador, sDireccionPEmpleador,
                sCorreoPEmpleador, foto, sVerificacionPEmpleador, sIdPEmpleador,
                sDescripcionPEmpleador, sProvinciaPEmpleador );
        DBperfilEmpleadores.child( sIdPEmpleador ).setValue( empleadores );
        mProgressDialog.dismiss();
        Toast.makeText(PantallaPerfilEmpleador.this, "Sus Datos han Sido Actualizado", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(PantallaPerfilEmpleador.this, PantallaPrincipalEmpleador.class);
        startActivity(intent);

    }
}
