package com.ataya.company.service;

import com.ataya.company.model.Worker;

public interface WorkerService {
    Worker getWorkerById(String id);

    void saveWorker(Worker worker);
}
