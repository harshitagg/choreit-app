package com.pineapps.choreit.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.pineapps.choreit.R;
import com.pineapps.choreit.domain.Group;

import java.util.List;

public class GroupListAdapter extends BaseAdapter {
    private List<Group> groupList;
    private LayoutInflater layoutInflater;

    public GroupListAdapter(Context context, List<Group> groupList) {
        layoutInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupList = groupList;
    }

    @Override
    public int getCount() {
        return groupList.size();
    }

    @Override
    public Object getItem(int i) {
        return groupList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.group_spinner_item_layout, null);

        TextView groupName = (TextView) view.findViewById(R.id.group_name_spinner_item);
        Group group = groupList.get(position);
        groupName.setText(group.name());
        return view;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }
}
