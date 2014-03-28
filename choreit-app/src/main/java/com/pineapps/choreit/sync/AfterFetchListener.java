package com.pineapps.choreit.sync;

import com.pineapps.choreit.domain.FetchStatus;

public interface AfterFetchListener {
    void afterFetch(FetchStatus fetchStatus);
}
