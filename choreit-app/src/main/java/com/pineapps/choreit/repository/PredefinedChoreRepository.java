package com.pineapps.choreit.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.pineapps.choreit.domain.Chore;
import com.pineapps.choreit.domain.PredefinedChore;

import java.util.ArrayList;
import java.util.List;

public class PredefinedChoreRepository extends ChoreItRepository {
    public static final String PREDEFINED_CHORE_TABLE_NAME = "predefined_chore";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";

    public static final String[] PREDEFINED_CHORE_COLUMNS = new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_DESCRIPTION};

    public static final String PREDEFINED_CHORE_SQL = "CREATE TABLE " + PREDEFINED_CHORE_TABLE_NAME + "(" + COLUMN_ID
            + " VARCHAR, " + COLUMN_TITLE + " VARCHAR, " + COLUMN_DESCRIPTION + " VARCHAR)";

    @Override
    protected void onCreate(SQLiteDatabase database) {
        database.execSQL(PREDEFINED_CHORE_SQL);
    }

    public void insert(PredefinedChore chore) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, chore.id());
        values.put(COLUMN_TITLE, chore.title());
        values.put(COLUMN_DESCRIPTION, chore.description());

        SQLiteDatabase db = masterRepository.getWritableDatabase();
        db.insert(PREDEFINED_CHORE_TABLE_NAME, null, values);
    }

    public List<PredefinedChore> getAll() {
        SQLiteDatabase db = masterRepository.getReadableDatabase();
        Cursor cursor = db.query(PREDEFINED_CHORE_TABLE_NAME, PREDEFINED_CHORE_COLUMNS, null, null, null, null, null);
        return readAllChores(cursor);
    }

    private List<PredefinedChore> readAllChores(Cursor cursor) {
        List<PredefinedChore> chores = new ArrayList<PredefinedChore>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            chores.add(new PredefinedChore(
                    cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))
            ));
            cursor.moveToNext();
        }
        cursor.close();
        return chores;
    }

    public void deleteAllChores() {
        SQLiteDatabase db = masterRepository.getWritableDatabase();
        db.delete(PREDEFINED_CHORE_TABLE_NAME, null, null);
    }
}
