package com.pineapps.choreit.repository;

import android.content.ContentValues;
import android.database.Cursor;
import com.pineapps.choreit.domain.User;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.valueOf;

public class UserRepository extends ChoreItRepository {
    private static final String USER_TABLE_NAME = "user";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL_ID = "email_id";
    private static final String COLUMN_IS_SYNCED = "is_synced";

    public static final String[] USER_COLUMNS = new String[]{COLUMN_EMAIL_ID, COLUMN_NAME, COLUMN_IS_SYNCED};

    public static final String USER_SQL = "CREATE TABLE " + USER_TABLE_NAME + "(" + COLUMN_EMAIL_ID + " VARCHAR, " +
            COLUMN_NAME + " VARCHAR, " + COLUMN_IS_SYNCED + " VARCHAR)";

    @Override
    protected void onCreate(SQLiteDatabase database) {
        database.execSQL(USER_SQL);
    }

    public void insert(User user) {
        ContentValues values = createContentValuesFor(user);

        SQLiteDatabase db = masterRepository.getWritableDatabase();
        db.insert(USER_TABLE_NAME, null, values);
    }

    public List<User> getAll() {
        SQLiteDatabase db = masterRepository.getReadableDatabase();
        Cursor cursor = db.query(USER_TABLE_NAME, USER_COLUMNS, null, null, null, null, null);
        return readAllUsers(cursor);
    }

    public void deleteAllUsers() {
        SQLiteDatabase db = masterRepository.getWritableDatabase();
        db.delete(USER_TABLE_NAME, null, null);
    }

    public User findByEmailId(String emailId) {
        SQLiteDatabase db = masterRepository.getReadableDatabase();
        Cursor cursor = db.query(USER_TABLE_NAME, USER_COLUMNS, COLUMN_EMAIL_ID + " = ?", new String[]{emailId}, null,
                null, null);
        return readAllUsers(cursor).get(0);
    }

    public List<User> getPendingUsers() {
        SQLiteDatabase db = masterRepository.getReadableDatabase();
        Cursor cursor = db.query(USER_TABLE_NAME, USER_COLUMNS, COLUMN_IS_SYNCED + " = ?", new String[]{"false"},
                null, null, null);
        return readAllUsers(cursor);
    }

    public void markUsersAsSynced(List<User> users) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        for (User user : users) {
            user.markAsSynced();
            database.update(USER_TABLE_NAME, createContentValuesFor(user), COLUMN_EMAIL_ID + " = ?",
                    new String[]{user.emailId()});
        }
    }

    private ContentValues createContentValuesFor(User user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, user.name());
        values.put(COLUMN_EMAIL_ID, user.emailId());
        values.put(COLUMN_IS_SYNCED, Boolean.toString(user.isSynced()));
        return values;
    }

    private List<User> readAllUsers(Cursor cursor) {
        List<User> users = new ArrayList<User>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            users.add(new User(
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)), cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL_ID)),
                    valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_IS_SYNCED)))
            ));
            cursor.moveToNext();
        }
        cursor.close();
        return users;
    }
}
