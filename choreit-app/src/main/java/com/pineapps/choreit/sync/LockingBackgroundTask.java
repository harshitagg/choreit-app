package com.pineapps.choreit.sync;

import android.os.AsyncTask;

import java.util.concurrent.locks.ReentrantLock;

import static com.pineapps.choreit.util.Log.logVerbose;


public class LockingBackgroundTask {
    private static final ReentrantLock lock = new ReentrantLock();

    public <T> void doActionInBackground(final BackgroundAction<T> backgroundAction) {
        new AsyncTask<Void, Void, T>() {
            @Override
            protected T doInBackground(Void... params) {
                if (!lock.tryLock()) {
                    logVerbose("Going away. Something else is holding the lock.");
                    cancel(true);
                    return null;
                }
                try {
                    publishProgress();
                    return backgroundAction.actionToDoInBackgroundThread();
                } finally {
                    lock.unlock();
                }
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(T result) {
                backgroundAction.postExecuteInUIThread(result);
            }
        }.execute((Void) null);
    }
}
