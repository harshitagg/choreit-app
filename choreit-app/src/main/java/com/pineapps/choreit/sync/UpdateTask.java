package com.pineapps.choreit.sync;

import android.content.Context;
import android.widget.Toast;
import com.pineapps.choreit.domain.FetchStatus;
import com.pineapps.choreit.service.ChoreSyncService;

import static com.pineapps.choreit.domain.FetchStatus.nothingFetched;

public class UpdateTask {
    private final LockingBackgroundTask task;
    private Context context;
    private ChoreSyncService choreSyncService;

    public UpdateTask(Context context, ChoreSyncService choreSyncService) {
        this.context = context;
        this.choreSyncService = choreSyncService;
        task = new LockingBackgroundTask();
    }

    public void updateFromServer(final AfterFetchListener afterFetchListener) {
        task.doActionInBackground(new BackgroundAction<FetchStatus>() {
            public FetchStatus actionToDoInBackgroundThread() {
                return choreSyncService.pushToServer();
            }

            public void postExecuteInUIThread(FetchStatus result) {
                if (result != null && context != null && result != nothingFetched) {
                    Toast.makeText(context, result.displayValue(), Toast.LENGTH_SHORT).show();
                }
                afterFetchListener.afterFetch(result);
            }
        });
    }
}
