package com.pineapps.choreit.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import com.pineapps.choreit.ChoreItContext;
import com.pineapps.choreit.R;
import com.pineapps.choreit.domain.Chore;
import com.pineapps.choreit.domain.FetchStatus;
import com.pineapps.choreit.domain.Group;
import com.pineapps.choreit.service.ChoreService;
import com.pineapps.choreit.service.GroupService;
import com.pineapps.choreit.sync.AfterFetchListener;
import com.pineapps.choreit.sync.UpdateTask;
import com.pineapps.choreit.view.adapter.ChoreListAdapter;
import com.pineapps.choreit.view.adapter.GroupListAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends Activity {
    public static final int UPDATE_CHORE_LIST = 1;
    public static final int UPDATE_GROUP_LIST = 2;
    public static final String CURRENT_GROUP = "current_group";
    public static final String CURRENT_GROUP_OFFSET = "current_group_offset";
    public static final String CHORE_ID = "chore_id";

    private String currentGroup = "0";
    private int getCurrentGroupOffsetInList = 0;
    private ChoreListAdapter choreListAdapter;
    private ChoreService choreService;
    private GroupService groupService;
    private List<Chore> choreList;
    private ChoreItContext context;
    private List<Group> groupList;
    private GroupListAdapter groupListAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        context = ChoreItContext.getInstance();
        choreService = context.choreService();
        groupService = context.groupService();

        initGroupSpinner();
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
        startActivityForResult(intent, UPDATE_CHORE_LIST);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_chore:
                Intent choreIntent = new Intent(this, AddChoreActivity.class);
                choreIntent.putExtra(CURRENT_GROUP, currentGroup);
                choreIntent.putExtra(CURRENT_GROUP_OFFSET, getCurrentGroupOffsetInList);
                startActivityForResult(choreIntent, UPDATE_CHORE_LIST);
                return true;
            case R.id.action_add_group:
                Intent groupIntent = new Intent(this, AddGroupActivity.class);
                startActivityForResult(groupIntent, UPDATE_GROUP_LIST);
                return true;
            case R.id.action_refresh:
                updateFromServer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initGroupSpinner() {
        Spinner groupSpinner = (Spinner) findViewById(R.id.group_spinner);
        groupListAdapter = new GroupListAdapter(this, groupList);
        updateGroupList();
        groupSpinner.setAdapter(groupListAdapter);
        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                currentGroup = groupList.get(position).id();
                getCurrentGroupOffsetInList = position;
                updateChoreList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        groupSpinner.setSelection(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_CHORE_LIST) {
            updateChoreList();
        }
        if (requestCode == UPDATE_GROUP_LIST) {
            updateGroupList();
        }
    }

    private void updateGroupList() {
        groupList = groupService.getAllGroups();
        groupListAdapter.setGroupList(groupList);
        groupListAdapter.notifyDataSetChanged();
    }

    private void updateChoreList() {
        List<Chore> tmpList = new ArrayList<Chore>();
        choreList = choreService.getAllUndoneChoresSortedByDueDate();
        for (Chore chore : choreList) {
            if (chore.groupId().equals(currentGroup)) {
                tmpList.add(chore);
            }
        }
        choreList = tmpList;
        choreListAdapter.setChoreList(choreList);
        choreListAdapter.notifyDataSetChanged();
    }

    private void updateFromServer() {
        UpdateTask updateTask = new UpdateTask(getApplicationContext(), context.choreSyncService());

        updateTask.updateFromServer(new AfterFetchListener() {
            public void afterFetch(FetchStatus status) {
            }
        });
    }
}
