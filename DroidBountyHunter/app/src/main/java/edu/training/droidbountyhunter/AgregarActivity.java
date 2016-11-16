package edu.training.droidbountyhunter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Eden on 16/08/2016.
 */
public class AgregarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        this.setTitle("Nuevo Fugitivo");
        setContentView(R.layout.activity_agregar);

        Toolbar tb = (Toolbar)findViewById(R.id.toolbarAgregar);
        setSupportActionBar(tb);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    /*public  void onSaveClick(View view){
        TextView oTxtN = (TextView)this.findViewById(R.id.txtNew);
        if(!oTxtN.getText().toString().isEmpty()){
            Home.oDB.InsertFugitivo(oTxtN.getText().toString());
            setResult(0);
            finish();
        }
        else{
            new AlertDialog.Builder(this)
                    .setTitle("Alerta")
                    .setMessage("Favor de capturar el nombre del fugitivo")
                    .show();
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_agregar, menu);

        MenuItem mn = menu.getItem(0);
        mn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
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
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

