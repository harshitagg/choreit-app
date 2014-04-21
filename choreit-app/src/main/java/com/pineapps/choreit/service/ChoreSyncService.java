package com.pineapps.choreit.service;

import com.google.gson.Gson;
import com.pineapps.choreit.domain.Chore;
import com.pineapps.choreit.domain.FetchStatus;
import com.pineapps.choreit.domain.Response;
import com.pineapps.choreit.dto.ChoreDTO;
import com.pineapps.choreit.repository.ChoreRepository;

import java.util.ArrayList;
import java.util.List;

import static com.pineapps.choreit.AllConstants.CHORE_POST_PATH;
import static com.pineapps.choreit.AllConstants.SERVER_BASE_URL;
import static com.pineapps.choreit.domain.FetchStatus.success;
import static com.pineapps.choreit.domain.FetchStatus.failure;
import static com.pineapps.choreit.util.Log.logError;
import static com.pineapps.choreit.util.Log.logInfo;
import static java.text.MessageFormat.format;

public class ChoreSyncService {
    private ChoreRepository choreRepository;
    private HTTPAgent httpAgent;

    public ChoreSyncService(ChoreRepository choreRepository, HTTPAgent httpAgent) {
        this.choreRepository = choreRepository;
        this.httpAgent = httpAgent;
    }

    public FetchStatus pushToServer() {
        List<Chore> pendingChores = choreRepository.getPendingChores();
        if (pendingChores.isEmpty()) {
            return null;
        }
        String jsonPayload = mapToChoreDTO(pendingChores);
        Response<String> response = httpAgent.postJSONRequest(SERVER_BASE_URL + CHORE_POST_PATH, jsonPayload);
        if (response.isFailure()) {
            logError(format("Chores sync failed. Submissions:  {0}", pendingChores));
            return failure;
        }
        choreRepository.markChoresAsSynced(pendingChores);
        logInfo(format("Chores sync successfully. Submissions:  {0}", pendingChores));
        return success;
    }

    private String mapToChoreDTO(List<Chore> pendingChores) {
        List<ChoreDTO> choreDTOs = new ArrayList<ChoreDTO>();
        for (Chore pendingChore : pendingChores) {
            choreDTOs.add(new ChoreDTO(pendingChore.id(), pendingChore.title(), pendingChore.description(), pendingChore.dueDate(),
                    pendingChore.groupId(), pendingChore.isDone()));
        }
        return new Gson().toJson(choreDTOs);
    }
}
