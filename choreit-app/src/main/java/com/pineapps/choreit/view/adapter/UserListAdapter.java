package com.pineapps.choreit.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.pineapps.choreit.R;
import com.pineapps.choreit.domain.User;

import java.util.ArrayList;
import java.util.List;


public class UserListAdapter extends SimpleCursorAdapter implements Filterable {
    private List<User> userList;
    private List<User> filteredList;
    private LayoutInflater layoutInflater;
    private ItemFilter itemFilter = new ItemFilter();

    public UserListAdapter(Context context, List<User> userList) {
        super(context, 0, null, null, null, 0);
        layoutInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.userList = userList;
        this.filteredList = userList;
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.existing_contact_list_item, null);

        TextView userName = (TextView) view.findViewById(R.id.existing_user_entry);
        TextView userEmail = (TextView) view.findViewById(R.id.existing_user_email);

        User user = filteredList.get(position);
        userName.setText(user.name());
        userEmail.setText(user.emailId());
        return view;
    }

    public Filter getFilter() {
        return itemFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            List<User> list = userList;
            List<User> newList = new ArrayList<User>();
            User tmpUser;

            String filterableName;
            String filterableEmail;

            for (User aList : list) {
                tmpUser = aList;
                filterableName = tmpUser.name();
                filterableEmail = tmpUser.emailId();
                if (filterableName.toLowerCase().contains(filterString) ||
                        filterableEmail.toLowerCase().contains(filterString)) {
                    newList.add(tmpUser);
                }
            }

            results.values = newList;
            results.count = newList.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (List<User>) results.values;
            notifyDataSetChanged();
        }

    }
}
