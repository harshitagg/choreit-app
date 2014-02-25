package com.pineapps.choreit.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.pineapps.choreit.ChoreItContext;
import com.pineapps.choreit.R;
import com.pineapps.choreit.domain.Chore;
import com.pineapps.choreit.service.ChoreService;
import com.pineapps.choreit.view.adapter.ChoreListAdapter;

import java.util.List;

public class HomeActivity extends Activity {
    public static final int ADD_CHORE = 1;

    private ChoreListAdapter choreListAdapter;
    private ChoreService choreService;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        ChoreItContext context = ChoreItContext.getInstance();
        choreService = context.choreService();

        ListView viewChores = (ListView) findViewById(R.id.listview);
        List<Chore> choreList = choreService.getAllChores();
        choreListAdapter = new ChoreListAdapter(this, choreList);
        viewChores.setAdapter(choreListAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_chore:
                Intent choreIntent = new Intent(this, AddChoreActivity.class);
                startActivityForResult(choreIntent, ADD_CHORE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_CHORE) {
            updateChoreList();
        }
    }

    private void updateChoreList() {
        List<Chore> choreList = choreService.getAllChores();
        choreListAdapter.setChoreList(choreList);
        choreListAdapter.notifyDataSetChanged();
    }
}
