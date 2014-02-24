package com.pineapps.choreit.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.pineapps.choreit.R;
import com.pineapps.choreit.domain.Chore;

import java.util.List;

public class ListAdapter extends BaseAdapter {
    private List<Chore> choreList;
    private LayoutInflater layoutInflater;

    public ListAdapter(Context context, List<Chore> choreList) {
        layoutInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.choreList = choreList;
    }

    @Override
    public int getCount() {
        return choreList.size();
    }

    @Override
    public Object getItem(int i) {
        return choreList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.list_layout, null);

        TextView choreName = (TextView) view.findViewById(R.id.chore_name_list_item);
        TextView choreDescription = (TextView) view.findViewById(R.id.chore_description_list_item);
        choreName.setText(choreList.get(position).title());
        choreDescription.setText(choreList.get(position).description());
        return view;
    }
}
