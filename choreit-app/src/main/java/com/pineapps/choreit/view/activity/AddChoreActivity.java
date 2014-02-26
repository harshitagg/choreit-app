package com.pineapps.choreit.view.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.OnClickListener;
import static com.pineapps.choreit.view.activity.HomeActivity.UPDATE_LIST;
import static java.lang.String.valueOf;

public class AddChoreActivity extends Activity {
    public static final String DATE_FORMAT_PATTERN = "MMM d, y";
    private Spinner choresSpinner;
    private ChoreService choreService;
    private List<PredefinedChore> predefinedChoreList;
    private List<String> predefinedChoreTitleList;
    private EditText descriptionEditText;
    private EditText newChoreTitle;
    private PredefinedChoreService predefinedChoreService;
    private ImageView choreIcon;
    private ChoreIconMap choreIconMap;
    private TextView choreDueDateTextView;
    private LocalDate nextDay;
    private DateTimeFormatter dateTimeFormatter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_chore_activity);

        ActionBar actionBar = getActionBar();
        actionBar.setTitle(R.string.action_add_chore);

        ChoreItContext choreItContext = ChoreItContext.getInstance();

        descriptionEditText = (EditText) findViewById(R.id.chore_description);
        newChoreTitle = (EditText) findViewById(R.id.chore_name_new);
        choreIcon = (ImageView) findViewById(R.id.chore_icon);
        choreDueDateTextView = (TextView) findViewById(R.id.chore_due_date);

        choreService = choreItContext.choreService();
        choreIconMap = choreItContext.choreIconMap();

        initPredefinedChores(choreItContext);
        initChoreSpinner();
        initAddChoreButton();
        initChoreDueDateTextView();
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
        addChoreButton.setOnClickListener(new OnClickListener() {
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
                        choreService.addChore(valueOf(newChoreTitle.getText()), valueOf(descriptionEditText.getText()),
                                nextDay.toString());
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
                            valueOf(descriptionEditText.getText()), nextDay.toString());
                }
                Toast.makeText(getApplicationContext(), "Chore Created", Toast.LENGTH_SHORT).show();
                activity.setResult(UPDATE_LIST);
                activity.finish();
            }
        });
    }

    private void initChoreDueDateTextView() {
        nextDay = LocalDate.now().plusDays(1);
        dateTimeFormatter = DateTimeFormat.forPattern(DATE_FORMAT_PATTERN);
        choreDueDateTextView.setText("Due on: " + dateTimeFormatter.print(nextDay));

        choreDueDateTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
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

    public void showDatePickerDialog(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                nextDay = new LocalDate(year, month + 1, day);
                choreDueDateTextView.setText("Due on: " + dateTimeFormatter.print(nextDay));
            }
        }, nextDay.getYear(), nextDay.getMonthOfYear() - 1, nextDay.getDayOfMonth());

        datePickerDialog.show();
    }
}
