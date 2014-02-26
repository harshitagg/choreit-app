package com.pineapps.choreit.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.pineapps.choreit.domain.Chore;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.valueOf;

public class ChoreRepository extends ChoreItRepository {
    public static final String CHORE_TABLE_NAME = "chore";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DUE_DATE = "due_date";
    public static final String COLUMN_IS_DONE = "is_done";

    public static final String[] CHORE_COLUMNS = new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_DESCRIPTION,
            COLUMN_DUE_DATE, COLUMN_IS_DONE};

    public static final String CHORE_SQL = "CREATE TABLE " + CHORE_TABLE_NAME + "(" + COLUMN_ID + " VARCHAR, " +
            COLUMN_TITLE + " VARCHAR, " + COLUMN_DESCRIPTION + " VARCHAR, " + COLUMN_DUE_DATE + " VARCHAR, " +
            COLUMN_IS_DONE + " VARCHAR)";

    @Override
    protected void onCreate(SQLiteDatabase database) {
        database.execSQL(CHORE_SQL);
    }

    public void insert(Chore chore) {
        ContentValues values = createContentValuesFor(chore);

        SQLiteDatabase db = masterRepository.getWritableDatabase();
        db.insert(CHORE_TABLE_NAME, null, values);
    }

    public List<Chore> getAll() {
        SQLiteDatabase db = masterRepository.getReadableDatabase();
        Cursor cursor = db.query(CHORE_TABLE_NAME, CHORE_COLUMNS, null, null, null, null, null);
        return readAllChores(cursor);
    }

    public void deleteAllChores() {
        SQLiteDatabase db = masterRepository.getWritableDatabase();
        db.delete(CHORE_TABLE_NAME, null, null);
    }

    public Chore findByChoreId(String choreId) {
        SQLiteDatabase db = masterRepository.getReadableDatabase();
        Cursor cursor = db.query(CHORE_TABLE_NAME, CHORE_COLUMNS, COLUMN_ID + " = ?", new String[]{choreId}, null,
                null, null);
        return readAllChores(cursor).get(0);
    }

    public void update(Chore chore) {
        ContentValues values = createContentValuesFor(chore);

        SQLiteDatabase db = masterRepository.getWritableDatabase();
        db.update(CHORE_TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{chore.id()});
    }

    public List<Chore> getAllUndoneChores() {
        SQLiteDatabase db = masterRepository.getReadableDatabase();
        Cursor cursor = db.query(CHORE_TABLE_NAME, CHORE_COLUMNS, COLUMN_IS_DONE + " = ?", new String[]{"false"},
                null, null, null);
        return readAllChores(cursor);
    }

    private ContentValues createContentValuesFor(Chore chore) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, chore.id());
        values.put(COLUMN_TITLE, chore.title());
        values.put(COLUMN_DESCRIPTION, chore.description());
        values.put(COLUMN_DUE_DATE, chore.dueDate());
        values.put(COLUMN_IS_DONE, Boolean.toString(chore.isDone()));
        return values;
    }

    private List<Chore> readAllChores(Cursor cursor) {
        List<Chore> chores = new ArrayList<Chore>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            chores.add(new Chore(
                    cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_DUE_DATE)),
                    valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_IS_DONE)))
            ));
            cursor.moveToNext();
        }
        cursor.close();
        return chores;
    }
}
