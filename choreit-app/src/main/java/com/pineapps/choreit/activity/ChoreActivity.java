package com.pineapps.choreit.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.*;
import com.pineapps.choreit.ChoreItContext;
import com.pineapps.choreit.R;
import com.pineapps.choreit.domain.Chore;
import com.pineapps.choreit.domain.PredefinedChore;
import com.pineapps.choreit.service.ChoreService;
import com.pineapps.choreit.service.PredefinedChoreService;

import java.util.ArrayList;
import java.util.List;

public class ChoreActivity extends Activity {
    private Button addChoreButton;
    private Spinner choresSpinner;
    private View choreDescription;
    private ChoreService choreService;
    private PredefinedChoreService predefinedChoreService;
    private ArrayAdapter stringArrayAdapter;
    private List<PredefinedChore> allPredefinedChores;
    private List<String> predefinedChoreList;
    private EditText description;
    private Chore c1;
    private Chore c2;
    private Chore c3;
    private List<Chore> chores;
    private View addButton;
    private ChoreItContext choreInstance;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chore_activity);
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(R.string.action_add_chore);

        choreInstance = ChoreItContext.getInstance();
        choreService = choreInstance.choreService();
        predefinedChoreService = choreInstance.predefinedChoreService();
        choresSpinner = (Spinner) findViewById(R.id.chore_name);
        addChoreButton = (Button) findViewById(R.id.add_chore);
        description = (EditText) findViewById(R.id.chore_description);
        allPredefinedChores = predefinedChoreService.getAllPredifinedChores();
        predefinedChoreList = new ArrayList<String>();
        predefinedChoreList.add("Select Chore");
        for (PredefinedChore predefinedChore : allPredefinedChores) {
            predefinedChoreList.add(predefinedChore.title());
        }
        stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, predefinedChoreList);
        choresSpinner.setAdapter(stringArrayAdapter);
        choresSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                    return;
                }
                description.setText(allPredefinedChores.get(position - 1).description());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        addChoreButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(choresSpinner.getSelectedItem().toString().equals("Select Chore")) {
                    Toast.makeText(getApplicationContext(),"Please Enter the Chore Details",Toast.LENGTH_SHORT).show();
                    return;
                }
                choreService.addChore(String.valueOf(choresSpinner.getSelectedItem()), String.valueOf(description.getText()));
                Toast.makeText(getApplicationContext(),"Chore Created",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}