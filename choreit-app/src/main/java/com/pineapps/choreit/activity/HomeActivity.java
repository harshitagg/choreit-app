package com.pineapps.choreit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.pineapps.choreit.R;
import com.pineapps.choreit.domain.Chore;
import com.pineapps.choreit.view.ListAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends Activity {

    private ListView viewChores;
    private List<Chore> tempChoreList;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_add_chore:
                Intent choreIntent = new Intent(this, AddChoreActivity.class);
                startActivity(choreIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        Chore c1 = new Chore("Laundry1","Do Laundry1");
        Chore c2 = new Chore("Laundry2","Do Laundry2");
        Chore c3 = new Chore("Laundry3","Do Laundry3");
        tempChoreList = new ArrayList<Chore>();
        tempChoreList.add(c1);
        tempChoreList.add(c2);
        tempChoreList.add(c3);
        viewChores = (ListView) findViewById(R.id.listview);
        viewChores.setAdapter(new ListAdapter(this,tempChoreList));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}