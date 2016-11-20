package android.eden.app.udemiejemplo2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    ListView listView1 = null;
    List<String> names = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView1 = (ListView)findViewById(R.id.listView1);

        names = new ArrayList<String>();
        names.add("Eden");
        names.add("Rodrigo");
        names.add("Verdugo");
        names.add("Garcia");
        names.add("Eden");
        names.add("Rodrigo");
        names.add("Verdugo");
        names.add("Garcia");
        names.add("Eden");
        names.add("Rodrigo");
        names.add("Verdugo");
        names.add("Garcia");
        names.add("Eden");
        names.add("Rodrigo");
        names.add("Verdugo");
        names.add("Garcia");
        names.add("Eden");
        names.add("Rodrigo");
        names.add("Verdugo");
        names.add("Garcia");
        names.add("Eden");
        names.add("Rodrigo");
        names.add("Verdugo");
        names.add("Garcia");
        names.add("Eden");
        names.add("Rodrigo");
        names.add("Verdugo");
        names.add("Garcia");
        names.add("Eden");
        names.add("Rodrigo");
        names.add("Verdugo");
        names.add("Garcia");

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        MyAdapter adapter = new MyAdapter(this, R.layout.list_item, names);

        listView1.setAdapter(adapter);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(ListActivity.this, "position: " + position + ", id: " + id + " item: " + names.get(position) , Toast.LENGTH_SHORT).show();
            }
        });



        /*
        List<String> names2 = new ArrayList<String>(){{
            add("Jessie");
            add("James");
            add("Mememes");
        }};
        */
    }

}

