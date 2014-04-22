package com.pineapps.choreit.repository;

import android.content.Context;
import com.pineapps.choreit.util.Session;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.io.File;

import static com.pineapps.choreit.repository.PredefinedChoreRepository.PREDEFINED_CHORE_TABLE_NAME;
import static com.pineapps.choreit.repository.GroupRepository.GROUP_TABLE_NAME;

public class Repository extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    private File databaseFile;
    private Context context;
    private Session session;
    private ChoreItRepository[] choreItRepositories;

    public Repository(Context context, Session session, ChoreItRepository... choreItRepositories) {
        super(context, session.repositoryName(), null, DATABASE_VERSION);
        this.context = context;
        this.session = session;
        this.databaseFile = context.getDatabasePath(session.repositoryName());
        this.choreItRepositories = choreItRepositories;

        initSQLCipher();
        for (ChoreItRepository choreItRepository : choreItRepositories) {
            choreItRepository.updateMasterRepository(this);
        }
    }

    private void initSQLCipher() {
        SQLiteDatabase.loadLibs(context);
        if (!databaseFile.exists()) {
            databaseFile.getParentFile().mkdirs();
        }
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databaseFile, session.password(), null);
        database.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        for (ChoreItRepository choreItRepository : choreItRepositories) {
            choreItRepository.onCreate(sqLiteDatabase);
            if (choreItRepository.getClass() == PredefinedChoreRepository.class) {
                createPredefinedChores(sqLiteDatabase);
            }
            if (choreItRepository.getClass() == GroupRepository.class) {
                createPersonalGroup(sqLiteDatabase);
            }
        }
    }

    private void createPersonalGroup(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + GROUP_TABLE_NAME + " values ('0','Private','null','false')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }

    private void createPredefinedChores(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + PREDEFINED_CHORE_TABLE_NAME + " values ('1','Cleaning','Clean the house')");
        db.execSQL("INSERT INTO " + PREDEFINED_CHORE_TABLE_NAME + " values ('2','Dish washing','Wash all utensils')");
        db.execSQL("INSERT INTO " + PREDEFINED_CHORE_TABLE_NAME + " values ('3','Laundry','Put clothes for washing')");
        db.execSQL("INSERT INTO " + PREDEFINED_CHORE_TABLE_NAME + " values ('4','Dusting','Dust the furniture')");
    }

    public SQLiteDatabase getReadableDatabase() {
        if (session.password() == null) {
            throw new RuntimeException("Password has not been set!");
        }
        return super.getReadableDatabase(session.password());
    }

    public SQLiteDatabase getWritableDatabase() {
        if (session.password() == null) {
            throw new RuntimeException("Password has not been set!");
        }
        return super.getWritableDatabase(session.password());
    }
}
