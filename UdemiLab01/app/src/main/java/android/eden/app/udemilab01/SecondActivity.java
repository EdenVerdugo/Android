package android.eden.app.udemilab01;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;



public class SecondActivity extends AppCompatActivity {

    String usuario = "";
    RadioButton rbSaludo = null;
    RadioButton rbDespedida = null;
    SeekBar skEdad = null;
    TextView tvEdad = null;
    Button btnSiguiente = null;

    private final int MIN_SEEK = 16;
    private final int MAX_SEEK = 60;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_myicon);

        Bundle b = getIntent().getExtras();
        if(b != null){
            usuario = b.getString("Usuario");
        }
        else{
            Toast.makeText(this, "No se encontro el dato de usuario", Toast.LENGTH_SHORT).show();
        }

        tvEdad = (TextView)findViewById(R.id.tvedad);
        skEdad = (SeekBar)findViewById(R.id.seekedad);
        rbSaludo = (RadioButton)findViewById(R.id.rdsaludo);
        rbDespedida = (RadioButton)findViewById(R.id.rddespedida);

        btnSiguiente = (Button)findViewById(R.id.btnsiguiente);

        tvEdad.setText(MIN_SEEK + "");

        skEdad.setMax(MAX_SEEK - MIN_SEEK);
        skEdad.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvEdad.setText(i + MIN_SEEK + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SecondActivity.this, ThridActivity.class);

                String tipoSaludo = rbSaludo.isChecked() ? "S" : "D";
                int edad = skEdad.getProgress() + MIN_SEEK;

                i.putExtra("Usuario", usuario);
                i.putExtra("TipoSaludo", tipoSaludo);
                i.putExtra("Edad", edad);

                startActivity(i);
            }
        });

    }
}
