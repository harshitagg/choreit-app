package com.pineapps.choreit.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pineapps.choreit.domain.Group;
import com.pineapps.choreit.domain.User;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.valueOf;

public class GroupRepository extends ChoreItRepository {
    private static final String GROUP_TABLE_NAME = "choreit_group";
    private static final String GROUP_COLUMN_ID = "id";
    private static final String GROUP_COLUMN_NAME = "name";
    private static final String GROUP_COLUMN_USERS = "users";
    private static final String GROUP_COLUMN_IS_SYNCED = "is_synced";

    public static final String[] GROUP_COLUMNS = new String[]{GROUP_COLUMN_ID, GROUP_COLUMN_NAME, GROUP_COLUMN_USERS,
            GROUP_COLUMN_IS_SYNCED};

    public static final String GROUP_SQL = "CREATE TABLE " + GROUP_TABLE_NAME + "(" + GROUP_COLUMN_ID + " VARCHAR PRIMARY KEY, " +
            GROUP_COLUMN_NAME + " VARCHAR, " + GROUP_COLUMN_USERS + " VARCHAR, " + GROUP_COLUMN_IS_SYNCED + " VARCHAR)";

    @Override
    protected void onCreate(SQLiteDatabase database) {
        database.execSQL(GROUP_SQL);
    }

    public void insert(Group group) {
        ContentValues values = createContentValuesFor(group);

        SQLiteDatabase db = masterRepository.getWritableDatabase();
        db.insert(GROUP_TABLE_NAME, null, values);
    }

    public List<Group> getAll() {
        SQLiteDatabase db = masterRepository.getReadableDatabase();
        Cursor cursor = db.query(GROUP_TABLE_NAME, GROUP_COLUMNS, null, null, null, null, null);
        return readAllGroups(cursor);
    }

    public void deleteAllGroups() {
        SQLiteDatabase db = masterRepository.getWritableDatabase();
        db.delete(GROUP_TABLE_NAME, null, null);
    }

    public Group findByGroupId(String groupId) {
        SQLiteDatabase db = masterRepository.getReadableDatabase();
        Cursor cursor = db.query(GROUP_TABLE_NAME, GROUP_COLUMNS, GROUP_COLUMN_ID + " = ?", new String[]{groupId}, null,
                null, null);
        return readAllGroups(cursor).get(0);
    }

    public List<Group> getPendingGroups() {
        SQLiteDatabase db = masterRepository.getReadableDatabase();
        Cursor cursor = db.query(GROUP_TABLE_NAME, GROUP_COLUMNS, GROUP_COLUMN_IS_SYNCED + " = ?", new String[]{"false"},
                null, null, null);
        return readAllGroups(cursor);
    }

    public void markGroupsAsSynced(List<Group> groups) {
        SQLiteDatabase database = masterRepository.getWritableDatabase();
        for (Group group : groups) {
            group.markAsSynced();
            database.update(GROUP_TABLE_NAME, createContentValuesFor(group), GROUP_COLUMN_ID + " = ?", new String[]{group.id()});
        }
    }

    private ContentValues createContentValuesFor(Group group) {
        ContentValues values = new ContentValues();
        values.put(GROUP_COLUMN_ID, group.id());
        values.put(GROUP_COLUMN_NAME, group.name());
        values.put(GROUP_COLUMN_USERS, new Gson().toJson(group.users()));
        values.put(GROUP_COLUMN_IS_SYNCED, Boolean.toString(group.isSynced()));
        return values;
    }

    private List<Group> readAllGroups(Cursor cursor) {
        List<Group> groups = new ArrayList<Group>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            List<User> users = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(GROUP_COLUMN_USERS)), new TypeToken<List<User>>() {
            }.getType());
            groups.add(new Group(
                    cursor.getString(cursor.getColumnIndex(GROUP_COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(GROUP_COLUMN_NAME)),
                    users,
                    valueOf(cursor.getString(cursor.getColumnIndex(GROUP_COLUMN_IS_SYNCED)))
            ));
            cursor.moveToNext();
        }
        cursor.close();
        return groups;
    }
}
