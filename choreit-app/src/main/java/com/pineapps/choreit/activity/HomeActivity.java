package com.pineapps.choreit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.pineapps.choreit.ChoreItContext;
import com.pineapps.choreit.R;
import com.pineapps.choreit.domain.Chore;
import com.pineapps.choreit.view.ListAdapter;

import java.util.List;

public class HomeActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        ChoreItContext context = ChoreItContext.getInstance();
        List<Chore> choreList = context.choreService().getAllChores();

        ListView viewChores = (ListView) findViewById(R.id.listview);
        viewChores.setAdapter(new ListAdapter(this, choreList));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_chore:
                Intent choreIntent = new Intent(this, AddChoreActivity.class);
                startActivity(choreIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}