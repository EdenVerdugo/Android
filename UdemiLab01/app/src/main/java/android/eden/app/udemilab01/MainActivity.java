package android.eden.app.udemilab01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText txtUsuario = null;
    Button btnSiguiente = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_myicon);

        txtUsuario = (EditText)findViewById(R.id.txtusuario);
        btnSiguiente = (Button)findViewById(R.id.btnsiguiente);

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Usuario = txtUsuario.getText().toString();

                if(Usuario != null && !Usuario.isEmpty()) {

                    Intent i = new Intent(MainActivity.this, SecondActivity.class);
                    i.putExtra("Usuario", Usuario );

                    startActivity(i);

                }
                else{
                    Toast.makeText(MainActivity.this, "El nombre de usuario no puede quedar vacio", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
