package com.ataya.company.service.impl;

import com.ataya.company.exception.Custom.ResourceNotFoundException;
import com.ataya.company.model.Worker;
import com.ataya.company.repo.WorkerRepository;
import com.ataya.company.service.WorkerService;
import org.springframework.stereotype.Service;

@Service
public class WorkerServiceImpl implements WorkerService {
    
    private final WorkerRepository workerRepository;

    public WorkerServiceImpl(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }

    @Override
    public Worker getWorkerById(String id) {
        return workerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Worker",
                        "id",
                        id,
                        "Worker with id " + id + " not found"
                )
        );
    }

    @Override
    public void saveWorker(Worker worker) {
        workerRepository.save(worker);
    }
}
