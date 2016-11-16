package edu.training.droidbountyhunter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by DragonShin on 21/08/2016.
 */
public class FugitivosAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<String[]> items;
    private LayoutInflater inflater;

    public FugitivosAdapter(Context context, ArrayList<String[]> items) {
        this.context = context;
        this.items = items;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return items.indexOf(items.get(i));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater lInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = lInflater.inflate(R.layout.row_list, null);
        }


        String[] item =  items.get(position);

        ImageView img = (ImageView) convertView.findViewById(R.id.imgFugitivo);
        Bitmap bm = PictureTools.decodeSampledBitmapFromUri(item[3], 200, 200);

        if(bm != null)
        {
            img.setImageBitmap(bm);
        }
        else
        {
            img.setImageResource(R.drawable.common_ic_googleplayservices);
        }

        TextView txtNombre = (TextView)convertView.findViewById(R.id.rowText);
        txtNombre.setText(item[1]);

        TextView txtFecha = (TextView)convertView.findViewById(R.id.txtFecha);
        txtFecha.setText(item[6]);

        return convertView;
    }
}