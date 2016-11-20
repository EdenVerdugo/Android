package android.eden.app.udemiejemplo2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends BaseAdapter {

    private Context context = null;
    private int layout = 0;
    private List<String> names = null;


    public MyAdapter(Context context, int layout, List<String> names) {
        this.context = context;
        this.layout = layout;
        this.names = names;
    }

    @Override
    public int getCount() {
        return this.names.size();
    }

    @Override
    public Object getItem(int i) {

        return this.names.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        View v = view;

        if(view == null){
            LayoutInflater inflater = LayoutInflater.from(this.context);
            v = inflater.inflate(this.layout, null);

            holder = new ViewHolder();
            holder.nameTextView = (TextView)v.findViewById(R.id.textView1);

            v.setTag(holder);
        }
        else {
            holder = (ViewHolder)v.getTag();
        }

        String name = names.get(i);
        holder.nameTextView.setText(name);

        return v;
    }

    static class ViewHolder{
        private TextView nameTextView;
    }
}
