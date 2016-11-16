package edu.training.droidbountyhunter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by Eden on 16/08/2016.
 */
public class DetalleActivity extends AppCompatActivity {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final String APP_PATH = "droidBH";
    private LocationManager locationManager;
    double dLat;
    double dLon;
    String sNombre;
    int modo = 0;

    private Uri fileUri;
    String sID;

    private void newLocation(Location pLoc) {
        dLat = pLoc.getLatitude();
        dLon = pLoc.getLongitude();

        Toast.makeText(DetalleActivity.this, "Detectado (" + String.valueOf(dLat) + ", " + String.valueOf(dLon) + " )", Toast.LENGTH_SHORT).show();
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            newLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private void ActivarGPS(){
        try {
            locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Toast.makeText(DetalleActivity.this, "Activando GPS...", Toast.LENGTH_SHORT).show();

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);

            String provider = locationManager.getBestProvider(criteria, true);

            Location location = locationManager.getLastKnownLocation(provider);

            if(location != null){
                newLocation(location);
            }
        }
        catch (SecurityException e){
            Toast.makeText(DetalleActivity.this, "Error de seguridad " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void ApagarGPS(){
        if(locationManager != null){
            try{
                locationManager.removeUpdates(locationListener);
                Toast.makeText(DetalleActivity.this, "Desactivando GPS...", Toast.LENGTH_SHORT).show();
            }
            catch (SecurityException e){
                Toast.makeText(DetalleActivity.this, "Error de seguridad " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_detalle, menu);

        if(modo == 1){//atrapados
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
        }
        else{//fugitivo
            menu.getItem(3).setVisible(false);
        }

        /*mn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                TextView oTxtN = (TextView)findViewById(R.id.txtNew);
                if(!oTxtN.getText().toString().isEmpty()){
                    Home.oDB.InsertFugitivo(oTxtN.getText().toString());
                    setResult(0);
                    finish();
                }
                else{
                    new AlertDialog.Builder(getApplicationContext())
                            .setTitle("Alerta")
                            .setMessage("Favor de capturar el nombre del fugitivo")
                            .show();
                }

                return false;
            }
        });*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /*if(item.getItemId() == android.R.id.home){
            finish();
        }*/
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_atrapar:
                onCaptureClick(null);
                break;
            case R.id.menu_eliminar:
                onDeleteClick(null);
                break;
            case R.id.menu_foto:
                onFotoClick(null);
                break;
            case R.id.menu_mapa:
                onMapClick(null);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Bundle oExt = this.getIntent().getExtras();

        //this.setTitle(oExt.getString("Detalle"));
        this.setTitle(oExt.getString("title" + " - [" + oExt.getString("id") + "]"));
        this.sID = oExt.getString("id");
        this.sNombre = oExt.getString("nom");
        modo = oExt.getInt("mode");

        setContentView(R.layout.activity_detalle);


        Toolbar tb = (Toolbar)findViewById(R.id.toolbarDetalle);
        setSupportActionBar(tb);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        TextView oMsg = (TextView)this.findViewById(R.id.lblMsg);


        if( modo == 0){

            //Button oBtn3 = (Button)this.findViewById(R.id.btnMap);
            //oBtn3.setVisibility(View.GONE);

            oMsg.setText("El fugitivo sigue suelto...");

            ActivarGPS();
        }
        else{
            //Button oBtn1 = (Button)this.findViewById(R.id.btnCap);
            //oBtn1.setVisibility(View.GONE);

            //Button oBtn2 = (Button)this.findViewById(R.id.btnFoto);
            //oBtn2.setVisibility(View.GONE);

            dLat = Double.valueOf(oExt.getString("lat"));
            dLon = Double.valueOf(oExt.getString("lon"));

            oMsg.setText("Atrapado!!!");

            ImageView oImg = (ImageView)this.findViewById(R.id.imgFoto);
            if(oExt.getString("foto") != null && oExt.getString("foto") != ""){
                Bitmap bm = PictureTools.decodeSampledBitmapFromUri(oExt.getString("foto"), 200, 200);
                oImg.setImageBitmap(bm);
            }
        }

        if(savedInstanceState != null){
            fileUri = savedInstanceState.getParcelable("uri");
            if(fileUri != null){
                ImageView oImg = (ImageView)this.findViewById(R.id.imgFoto);
                Bitmap bm = PictureTools.decodeSampledBitmapFromUri(savedInstanceState.getString("foto"), 200, 200);


                oImg.setImageBitmap(bm);
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(fileUri != null){
            outState.putParcelable("uri", fileUri);
            outState.putString("foto", fileUri.getPath());
        }

        super.onSaveInstanceState(outState);
    }

    public void onCaptureClick(View view){
        if(fileUri == null){
            Toast.makeText(DetalleActivity.this, "Es necesario tomar la foto del fugitivo..", Toast.LENGTH_SHORT).show();

            return;
        }

        Bitmap bm = PictureTools.decodeSampledBitmapFromUri(fileUri.getPath(), 75, 75);
        String imgBase64 = "";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        imgBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();

        String formatDate = df.format(c.getTime());

        Home.oDB.UpdateFugitivo("1", sID, fileUri.getPath(), dLat, dLon, formatDate);

        final NetServices oNS = new NetServices(new IOnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object feed) {
                MessageClose(feed.toString());
            }

            @Override
            public void onTaskError(Object feed) {
                Toast.makeText(getApplicationContext(), "Ocurrio un error en el WebService!!", Toast.LENGTH_SHORT).show();
            }
        });

        final NetServices oNS1 = new NetServices(new IOnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object feed) {
                MessageClose(feed.toString());
            }

            @Override
            public void onTaskError(Object feed) {
                Toast.makeText(getApplicationContext(), "Ocurrio un error en el WebService!!", Toast.LENGTH_SHORT).show();
            }
        });



        String sDevice = Home.UDID;

        oNS.execute("Atrapar", Home.UDID);
        oNS1.execute("InsertarUbicacion", Home.UDID, imgBase64, String.valueOf(dLat), String.valueOf(dLon), sNombre);


        //Button oBtn1 = (Button)this.findViewById(R.id.btnCap);
        //Button oBtn2 = (Button)this.findViewById(R.id.btnDel);
        //Button oBtn3 = (Button)this.findViewById(R.id.btnFoto);

        //oBtn1.setVisibility(View.GONE);
        //oBtn2.setVisibility(View.GONE);
        //oBtn3.setVisibility(View.GONE);

        setResult(0);
        //finish();
    }

    public void onDeleteClick(View view){
        Home.oDB.DeleteFugitivo(sID);
        setResult(0);

        finish();
    }

    @Override
    protected void onDestroy() {
        ApagarGPS();
        ImageView oImg = (ImageView)this.findViewById(R.id.imgFoto);
        oImg.setImageBitmap(null);
        System.gc();

        super.onDestroy();
    }

    public void MessageClose(String sMsg){
        AlertDialog.Builder oADb = new AlertDialog.Builder(this);
        AlertDialog oAD = oADb.create();
        oAD.setTitle("Alerta");
        oAD.setMessage(sMsg);
        oAD.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                finish();
            }
        });
        oAD.show();
    }

    public void onFotoClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, this.sID);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    public static Uri getOutputMediaFileUri(int type, String pID){
        return Uri.fromFile(getOutputMediaFile(type, pID));
    }

    public static File getOutputMediaFile(int type, String pID){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), APP_PATH);
        if(!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        File mediaFile;
        if(type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + pID + ".jpg");
        }
        else{
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                ImageView oImg = (ImageView)this.findViewById(R.id.imgFoto);

                Bitmap bm = PictureTools.decodeSampledBitmapFromUri(fileUri.getPath(), 200, 200);
                oImg.setImageBitmap(bm);

            }
            else if(resultCode == RESULT_CANCELED){
                Toast.makeText(DetalleActivity.this, "Se ha cancelado la captura de imagen", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(DetalleActivity.this, "Ocurrio un error al capturar la imagen.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onMapClick(View view) {
        Intent oMap = new Intent(this, MapActivity.class);
        oMap.putExtra("lat", dLat);
        oMap.putExtra("lon", dLon);
        oMap.putExtra("nom", sNombre);

        startActivity(oMap);
    }
}
