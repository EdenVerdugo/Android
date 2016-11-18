package android.eden.udemycurso;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ThirdActivity extends AppCompatActivity {

    private EditText txtTelefono = null;
    private EditText txtWeb = null;

    private ImageButton imgbtnTelefono = null;
    private ImageButton imgbtnWeb = null;
    private ImageButton imgbtnCamara = null;

    private final int PHONE_CALL_CODE = 100;
    private final int PICTURE_FROM_CAMERA = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        txtTelefono = (EditText) findViewById(R.id.txtTelefono);
        txtWeb = (EditText) findViewById(R.id.txtWeb);

        imgbtnTelefono = (ImageButton) findViewById(R.id.imgbtnTelefono);
        imgbtnWeb = (ImageButton) findViewById(R.id.imgbtnWeb);

        imgbtnCamara = (ImageButton) findViewById(R.id.imgbtnCamara);

        imgbtnTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numTelefono = txtTelefono.getText().toString();

                if (numTelefono != "") {
                    //comprobar version actual
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        //comprobar si ha aceptado o no ha aceptado o nunca se le ha preguntado
                        if(CheckPermission(Manifest.permission.CALL_PHONE)){
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + numTelefono));
                            if (ActivityCompat.checkSelfPermission(ThirdActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            startActivity(intent);
                        }
                        else{
                            if(!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)){
                                //no se ha preguntado aun
                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_CODE);
                            }
                            else{
                                //denegado
                                Toast.makeText(ThirdActivity.this, "Por favor activa este permiso", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                i.addCategory(Intent.CATEGORY_DEFAULT);
                                i.setData(Uri.parse("package:" + getPackageName()));
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

                                startActivity(i);
                            }
                        }

                        //NewerVersions();
                    } else {
                        OlderVersions(numTelefono);
                    }
                }
            }

            private void OlderVersions(String numTelefono) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + numTelefono));

                if (CheckPermission(Manifest.permission.CALL_PHONE)) {
                    startActivity(intent);
                } else {
                    Toast.makeText(ThirdActivity.this, "No tienes permisos para realizar esta acción", Toast.LENGTH_SHORT).show();
                }
            }

        });

        imgbtnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = txtWeb.getText().toString();
                if(url != null && !url.isEmpty()){
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + url));
                    //i.setAction(Intent.ACTION_VIEW);
                    //i.setData(Uri.parse("http://" + url));

                    //i = new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people"));

                    //i = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:eden.verdugo@gmail.com"));

                    i = new Intent(Intent.ACTION_SEND, Uri.parse("eden.verdugo@gmail.com"));
                    i.setType("plain/text");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Titulo correo");
                    i.putExtra(Intent.EXTRA_TEXT, "Este es el cuerpo del correo yeeeehaaa!!");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{"algo@mail.com", "otro@mail.com"});

                    startActivity(Intent.createChooser(i, "Selecciona el cliente de correo"));


                    //i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:6672323326"));




                    //startActivity(i);
                }
            }
        });

        imgbtnCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent("android.media.action.IMAGE_CAPTURE");

                startActivityForResult(i, PICTURE_FROM_CAMERA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case PICTURE_FROM_CAMERA:
                if(resultCode == Activity.RESULT_OK){
                    String resultado = data.toUri(0);
                    Toast.makeText(this, resultado, Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(this, "Hubo un error con la fotografia, intente de nuevo", Toast.LENGTH_LONG).show();
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PHONE_CALL_CODE:
                String permission = permissions[0];
                int result = grantResults[0];

                if (permission.equals(Manifest.permission.CALL_PHONE)) {
                    //comprobar si ha sido aceptado o denegado
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        // Concedio permiso
                        String numTelefono = txtTelefono.getText().toString();
                        if(!numTelefono.isEmpty()) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + numTelefono));
                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(this, "No se ha especificado un numero valido", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        // No concedio permiso
                        Toast.makeText(ThirdActivity.this, "No tienes permisos para realizar esta acción", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private boolean CheckPermission(String permission){
        int result = this.checkCallingOrSelfPermission(permission);

        return  result == PackageManager.PERMISSION_GRANTED;
    }
}
