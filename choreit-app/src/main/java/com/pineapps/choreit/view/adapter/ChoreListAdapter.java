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
import org.joda.time.LocalDate;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.List;

import static org.joda.time.LocalDate.parse;

public class ChoreListAdapter extends BaseAdapter {
    private ChoreIconMap choreIconMap;
    private List<Chore> choreList;
    private LayoutInflater layoutInflater;
    private PrettyTime prettyTime;

    public ChoreListAdapter(Context context, List<Chore> choreList) {
        layoutInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.choreList = choreList;
        this.choreIconMap = ChoreItContext.getInstance().choreIconMap();
        prettyTime = new PrettyTime();
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
        TextView choreDueDate = (TextView) view.findViewById(R.id.chore_due_date_list_item);
        TextView choreDescription = (TextView) view.findViewById(R.id.chore_description_list_item);
        ImageView choreIcon = (ImageView) view.findViewById(R.id.chore_icon_list_item);

        Chore chore = choreList.get(position);
        choreName.setText(chore.title());
        choreDueDate.setText(prettyTime.format(parse(chore.dueDate()).plusDays(1).toDate()));
        choreDescription.setText(chore.description());
        choreIcon.setImageResource(choreIconMap.get(chore.title()));
        return view;
    }

    public void setChoreList(List<Chore> choreList) {
        this.choreList = choreList;
    }
}
