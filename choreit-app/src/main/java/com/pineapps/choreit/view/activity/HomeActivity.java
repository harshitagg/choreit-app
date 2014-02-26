package com.pineapps.choreit.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.pineapps.choreit.ChoreItContext;
import com.pineapps.choreit.R;
import com.pineapps.choreit.domain.Chore;
import com.pineapps.choreit.service.ChoreService;
import com.pineapps.choreit.view.adapter.ChoreListAdapter;

import java.util.List;

public class HomeActivity extends Activity {
    public static final int UPDATE_LIST = 1;
    public static final String CHORE_ID = "chore_id";

    private ChoreListAdapter choreListAdapter;
    private ChoreService choreService;
    private List<Chore> choreList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        ChoreItContext context = ChoreItContext.getInstance();
        choreService = context.choreService();

        initListView();
    }

    private void initListView() {
        ListView choreListView = (ListView) findViewById(R.id.listview);
        choreList = choreService.getAllUndoneChoresSortedByDueDate();
        choreListAdapter = new ChoreListAdapter(this, choreList);
        choreListView.setAdapter(choreListAdapter);

        choreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                startChoreDetailActivity(choreList.get(position).id());
            }
        });
    }

    private void startChoreDetailActivity(String choreId) {
        Intent intent = new Intent(this, ChoreDetailActivity.class);
        intent.putExtra(CHORE_ID, choreId);
        startActivityForResult(intent, UPDATE_LIST);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_chore:
                Intent choreIntent = new Intent(this, AddChoreActivity.class);
                startActivityForResult(choreIntent, UPDATE_LIST);
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

        if (requestCode == UPDATE_LIST) {
            updateChoreList();
        }
    }

    private void updateChoreList() {
        choreList = choreService.getAllUndoneChoresSortedByDueDate();
        choreListAdapter.setChoreList(choreList);
        choreListAdapter.notifyDataSetChanged();
    }
}
