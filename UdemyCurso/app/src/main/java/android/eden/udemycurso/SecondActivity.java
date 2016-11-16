package android.eden.udemycurso;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    TextView lblMensaje = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        lblMensaje = (TextView)findViewById(R.id.lblMensaje);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String greeter = bundle.getString("greeter");

            lblMensaje.setText(greeter);
        }
        else{
            Toast.makeText(this, "greeter esta vacio!!", Toast.LENGTH_SHORT).show();
        }
    }
}
