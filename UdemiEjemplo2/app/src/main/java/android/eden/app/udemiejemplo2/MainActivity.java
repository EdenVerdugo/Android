package android.eden.app.udemiejemplo2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView1 = (ListView)findViewById(R.id.listView1);

        List<String> names = new ArrayList<String>();
        names.add("Eden");
        names.add("Rodrigo");
        names.add("Verdugo");
        names.add("Garcia");
    }
}
