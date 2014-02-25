package com.pineapps.choreit.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.pineapps.choreit.ChoreItContext;
import com.pineapps.choreit.R;
import com.pineapps.choreit.domain.Chore;
import com.pineapps.choreit.view.ChoreIconMap;

import java.util.List;

public class ChoreListAdapter extends BaseAdapter {
    private ChoreIconMap choreIconMap;
    private List<Chore> choreList;
    private LayoutInflater layoutInflater;

    public ChoreListAdapter(Context context, List<Chore> choreList) {
        layoutInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.choreList = choreList;
        this.choreIconMap = ChoreItContext.getInstance().choreIconMap();
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
        View view = layoutInflater.inflate(R.layout.chore_list_item_layout, null);

        TextView choreName = (TextView) view.findViewById(R.id.chore_name_list_item);
        TextView choreDescription = (TextView) view.findViewById(R.id.chore_description_list_item);
        ImageView choreIcon = (ImageView) view.findViewById(R.id.chore_icon_list_item);

        String title = choreList.get(position).title();
        choreName.setText(title);
        choreDescription.setText(choreList.get(position).description());
        choreIcon.setImageResource(choreIconMap.get(title));
        return view;
    }

    public void setChoreList(List<Chore> choreList) {
        this.choreList = choreList;
    }
}
