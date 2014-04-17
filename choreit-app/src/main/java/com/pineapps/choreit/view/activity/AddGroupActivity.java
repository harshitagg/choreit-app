package com.pineapps.choreit.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.pineapps.choreit.ChoreItContext;
import com.pineapps.choreit.R;
import com.pineapps.choreit.domain.Group;
import com.pineapps.choreit.domain.User;
import com.pineapps.choreit.service.GroupService;
import com.pineapps.choreit.view.adapter.UserListAdapter;

import java.util.ArrayList;
import java.util.List;

public class AddGroupActivity extends Activity {
    private static final int REQUEST_CODE = 1;
    private GroupService groupService;
    private List<User> groupUserList;
    private UserListAdapter groupUserAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_group_activity);

        ChoreItContext choreItContext = ChoreItContext.getInstance();
        groupService = choreItContext.groupService();

        initGroupListView();
        initSubmitButton();
    }

    private void initSubmitButton() {
        final Activity activity = this;
        Button addGroupButton = (Button) findViewById(R.id.new_group_create);
        final EditText newGroupName = (EditText) findViewById(R.id.new_group_name);
        addGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newGroupName.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Group Name Cannot Be Blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Group> existingGroups = groupService.getAllGroups();
                List<String> existingGroupNames = new ArrayList<String>();
                for (Group existingGroup : existingGroups) {
                    existingGroupNames.add(existingGroup.name());
                }
                if (existingGroupNames.contains(newGroupName.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Group Already Exists", Toast.LENGTH_SHORT).show();
                    newGroupName.setText("");
                    return;
                }
                groupService.addGroup(newGroupName.getText().toString(), groupUserList);
                Toast.makeText(getApplicationContext(), "New Group Added", Toast.LENGTH_SHORT).show();
                newGroupName.setEnabled(false);
                activity.finish();
            }
        });
    }

    private void initGroupListView() {
        groupUserList = new ArrayList<User>();
        groupUserAdapter = new UserListAdapter(this, groupUserList);
        ListView groupUserListView = (ListView) this.findViewById(R.id.new_group_user_list);
        groupUserListView.setAdapter(groupUserAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_person:
                Intent userChoreIntent = new Intent(this, AddUserActivity.class);
                startActivityForResult(userChoreIntent, REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_group_menu, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (REQUEST_CODE) : {
                if (resultCode == Activity.RESULT_OK) {
                    String newUserName = data.getStringExtra("NAME");
                    String newUserEmail = data.getStringExtra("EMAIL");
                    boolean isExistingUser = data.getBooleanExtra("EXISTING", false);
                    User newUser = new User(newUserName, newUserEmail);
                    if (isExistingUser) {
                        if(groupUserList.contains(newUser)) {
                            Toast.makeText(getApplicationContext(), "User Already in Group", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "User Added to Group", Toast.LENGTH_SHORT).show();
                        }
                    }
                    groupUserList.add(newUser);
                    groupUserAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
