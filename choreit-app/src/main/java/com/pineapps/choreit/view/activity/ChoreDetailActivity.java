package com.pineapps.choreit.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.pineapps.choreit.ChoreItContext;
import com.pineapps.choreit.R;
import com.pineapps.choreit.domain.Chore;
import com.pineapps.choreit.service.ChoreService;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import static com.pineapps.choreit.view.activity.AddChoreActivity.DATE_FORMAT_PATTERN;
import static com.pineapps.choreit.view.activity.HomeActivity.CHORE_ID;
import static com.pineapps.choreit.view.activity.HomeActivity.UPDATE_CHORE_LIST;

public class ChoreDetailActivity extends Activity {
    private ChoreService choreService;
    private Chore chore;
    private ChoreItContext context;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chore_detail_activity);

        Intent intent = getIntent();
        String choreId = intent.getStringExtra(CHORE_ID);

        context = ChoreItContext.getInstance();
        choreService = context.choreService();
        chore = choreService.getChoreById(choreId);

        initView();
    }

    private void initView() {
        TextView choreTitleTextField = (TextView) findViewById(R.id.chore_name_detail);
        choreTitleTextField.setText(chore.title());

        ImageView choreIconImageView = (ImageView) findViewById(R.id.chore_icon_detail);
        choreIconImageView.setImageResource(context.choreIconMap().get(chore.title()));

        TextView choreDueDateTextField = (TextView) findViewById(R.id.chore_due_date_detail);
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_FORMAT_PATTERN);
        choreDueDateTextField.setText("Due by " + dateTimeFormatter.print(LocalDate.parse(chore.dueDate())));

        TextView choreDescriptionTextField = (TextView) findViewById(R.id.chore_description_detail);
        choreDescriptionTextField.setText(chore.description());
    }

    public void markAsDone(View view) {
        choreService.markChoreAsDone(chore);
        Toast.makeText(getApplicationContext(), "Chore completed", Toast.LENGTH_SHORT).show();
        setResult(UPDATE_CHORE_LIST);
        finish();
    }
}
