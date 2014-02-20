package com.pineapps.choreit;

import android.provider.BaseColumns;

public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public FeedReaderContract() {
    }

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "chore";
        public static final String COLUMN_NAME_ENTRY_ID = "chore_id";
        public static final String COLUMN_NAME_TITLE = "chore_title";
        public static final String COLUMN_NAME_DESCRIPTION = "chore_desc";
    }
}