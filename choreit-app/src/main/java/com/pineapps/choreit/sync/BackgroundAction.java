package com.pineapps.choreit.sync;

public interface BackgroundAction<T> {
    T actionToDoInBackgroundThread();

    void postExecuteInUIThread(T result);
}
