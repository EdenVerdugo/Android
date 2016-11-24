package android.eden.app.udemilab01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ThridActivity extends AppCompatActivity {
    int edad = 0;
    String usuario = "";
    String tipoSaludo = "";
    String mensaje = "";

    Button btnMostrarMensaje = null;
    Button btnShare = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thrid);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_myicon);

        Bundle b = getIntent().getExtras();

        if(b != null){
            edad = b.getInt("Edad");
            usuario = b.getString("Usuario");
            tipoSaludo = b.getString("TipoSaludo");

            if(tipoSaludo.equals("S")) {
                mensaje =  "Hola " + usuario + " ¿Cómo llevas esos " + edad + " años?#MyForm";
            }
            else{
                mensaje = "Espero verte pronto " + usuario + ", antes de que cumplas " + (edad + 1) + "..#MyForm";
            }
        }
        else{
            Toast.makeText(this, "No se encontraron los datos de edad, usuario y tipo saludo", Toast.LENGTH_SHORT).show();
        }

        btnMostrarMensaje = (Button)findViewById(R.id.btnmostrarmensaje);
        btnMostrarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tipoSaludo.equals("S")) {
                    Toast.makeText(ThridActivity.this, mensaje, Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(ThridActivity.this, mensaje, Toast.LENGTH_LONG).show();
                }

                btnMostrarMensaje.setVisibility(View.INVISIBLE);
                btnShare.setVisibility(View.VISIBLE);
            }
        });


        btnShare = (Button)findViewById(R.id.btncompartir);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, mensaje);

                startActivity(Intent.createChooser(i, "Selecciona una aplicacion para compartir el mensaje:"));
            }
        });
    }
}
