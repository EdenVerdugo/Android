package android.eden.app.udemylab02.adapters;

import android.content.Context;
import android.eden.app.udemylab02.R;
import android.eden.app.udemylab02.models.Fruit;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by DragonShin on 20/11/2016.
 */

public class FruitAdapter extends BaseAdapter {

    Context context = null;
    int layout = 0;
    List<Fruit> lst = null;

    public FruitAdapter(Context context, int layout, List<Fruit> lst){
        this.layout = layout;
        this.lst = lst;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lst.size();
    }

    @Override
    public Object getItem(int i) {
        return lst.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ViewHolder holder = new ViewHolder();

        if(v == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            v = inflater.inflate(layout, null);

            holder.origen = (TextView)v.findViewById(R.id.tvFruitOrigin);
            holder.name = (TextView)v.findViewById(R.id.tvFruitName);
            holder.image = (ImageView)v.findViewById(R.id.imageFruit);

            view.setTag(holder);
        }
        else{
            holder = (ViewHolder) v.getTag();
        }

        holder.origen.setText(lst.get(i).getOrigen());
        holder.name.setText(lst.get(i).getName());
        holder.image.setImageResource(lst.get(i).getIcon());

        return v;
    }

    public class ViewHolder {
        private TextView origen = null;
        private TextView name = null;
        private ImageView image = null;
    }
}
