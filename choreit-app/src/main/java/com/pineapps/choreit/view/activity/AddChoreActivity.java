package com.pineapps.choreit.view.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.pineapps.choreit.ChoreItContext;
import com.pineapps.choreit.R;
import com.pineapps.choreit.domain.PredefinedChore;
import com.pineapps.choreit.service.ChoreService;
import com.pineapps.choreit.service.PredefinedChoreService;
import com.pineapps.choreit.view.ChoreIconMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pineapps.choreit.view.activity.HomeActivity.ADD_CHORE;
import static java.lang.String.valueOf;

public class AddChoreActivity extends Activity {
    private Spinner choresSpinner;
    private ChoreService choreService;
    private List<PredefinedChore> predefinedChoreList;
    private List<String> predefinedChoreTitleList;
    private EditText descriptionEditText;
    private EditText newChoreTitle;
    private PredefinedChoreService predefinedChoreService;
    private ImageView choreIcon;
    private ChoreIconMap choreIconMap;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_chore_activity);

        ActionBar actionBar = getActionBar();
        actionBar.setTitle(R.string.action_add_chore);

        ChoreItContext choreItContext = ChoreItContext.getInstance();

        descriptionEditText = (EditText) findViewById(R.id.chore_description);
        newChoreTitle = (EditText) findViewById(R.id.chore_name_new);
        choreIcon = (ImageView) findViewById(R.id.chore_icon);
        choreService = choreItContext.choreService();
        choreIconMap = choreItContext.choreIconMap();

        initPredefinedChores(choreItContext);
        initChoreSpinner();
        initAddChoreButton();
    }

    private void initPredefinedChores(ChoreItContext choreItContext) {
        predefinedChoreService = choreItContext.predefinedChoreService();
        predefinedChoreList = predefinedChoreService.getAllPredefinedChores();
        predefinedChoreTitleList = new ArrayList<String>();
        predefinedChoreTitleList.add("Select Chore");
        for (PredefinedChore predefinedChore : predefinedChoreList) {
            predefinedChoreTitleList.add(predefinedChore.title());
        }
        predefinedChoreTitleList.add("Add New Preset");
    }

    private void initAddChoreButton() {
        Button addChoreButton = (Button) findViewById(R.id.add_chore);
        final Activity activity = this;
        addChoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (choresSpinner.getVisibility() == View.GONE) {
                    if (newChoreTitle.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Please Enter the Chore Title", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (descriptionEditText.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Please Enter the Chore Description", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        choreService.addChore(valueOf(newChoreTitle.getText()), valueOf(descriptionEditText.getText()));
                        predefinedChoreService.addPredefinedChore(valueOf(newChoreTitle.getText()),
                                valueOf(descriptionEditText.getText()));
                    }
                } else if (choresSpinner.getSelectedItem().toString().equals("Select Chore")) {
                    Toast.makeText(getApplicationContext(), "Please Select the Chore Title", Toast.LENGTH_SHORT).show();
                    return;
                } else if (descriptionEditText.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter the Chore Description", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    choreService.addChore(valueOf(choresSpinner.getSelectedItem()),
                            valueOf(descriptionEditText.getText()));
                }
                Toast.makeText(getApplicationContext(), "Chore Created", Toast.LENGTH_SHORT).show();
                activity.setResult(ADD_CHORE);
                activity.finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reset_spinner:
                newChoreTitle.setVisibility(View.GONE);
                choresSpinner.setVisibility(View.VISIBLE);
                choresSpinner.setSelection(0);
                descriptionEditText.setText("");
            default:
                return super.onOptionsItemSelected(item);
        }
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
                    descriptionEditText.setText("");
                    choreIcon.setImageResource(R.drawable.general);
                    return;
                } else if (position == (predefinedChoreTitleList.size() - 1)) {
                    choresSpinner.setVisibility(View.GONE);
                    newChoreTitle.setVisibility(View.VISIBLE);
                    newChoreTitle.requestFocus();
                    descriptionEditText.setText("");
                    choreIcon.setImageResource(R.drawable.general);
                    return;
                }

                PredefinedChore predefinedChore = predefinedChoreList.get(position - 1);
                descriptionEditText.setText(predefinedChore.description());
                choreIcon.setImageResource(choreIconMap.get(predefinedChore.title()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.spinner_menu, menu);
        return true;
    }
}
