package com.pineapps.choreit.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.*;
import com.pineapps.choreit.ChoreItContext;
import com.pineapps.choreit.R;
import com.pineapps.choreit.domain.PredefinedChore;
import com.pineapps.choreit.service.ChoreService;
import com.pineapps.choreit.service.PredefinedChoreService;

import java.util.ArrayList;
import java.util.List;

public class AddChoreActivity extends Activity {
    private Spinner choresSpinner;
    private ChoreService choreService;
    private List<PredefinedChore> predefinedChoreList;
    private List<String> predefinedChoreTitleList;
    private EditText descriptionEditText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_chore_activity);

        ActionBar actionBar = getActionBar();
        actionBar.setTitle(R.string.action_add_chore);

        ChoreItContext choreItContext = ChoreItContext.getInstance();

        descriptionEditText = (EditText) findViewById(R.id.chore_description);

        choreService = choreItContext.choreService();

        initPredefinedChores(choreItContext);
        initChoreSpinner();
        initAddChoreButton();
    }

    private void initPredefinedChores(ChoreItContext choreItContext) {
        PredefinedChoreService predefinedChoreService = choreItContext.predefinedChoreService();
        predefinedChoreList = predefinedChoreService.getAllPredifinedChores();
        predefinedChoreTitleList = new ArrayList<String>();
        predefinedChoreTitleList.add("Select Chore");
        for (PredefinedChore predefinedChore : predefinedChoreList) {
            predefinedChoreTitleList.add(predefinedChore.title());
        }
    }

    private void initAddChoreButton() {
        Button addChoreButton = (Button) findViewById(R.id.add_chore);
        addChoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (choresSpinner.getSelectedItem().toString().equals("Select Chore")) {
                    Toast.makeText(getApplicationContext(), "Please Enter the Chore Details", Toast.LENGTH_SHORT).show();
                    return;
                }
                choreService.addChore(String.valueOf(choresSpinner.getSelectedItem()),
                        String.valueOf(descriptionEditText.getText()));
                Toast.makeText(getApplicationContext(), "Chore Created", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initChoreSpinner() {
        choresSpinner = (Spinner) findViewById(R.id.chore_name);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, predefinedChoreTitleList);
        choresSpinner.setAdapter(adapter);
        choresSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                    return;
                }
                descriptionEditText.setText(predefinedChoreList.get(position - 1).description());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
