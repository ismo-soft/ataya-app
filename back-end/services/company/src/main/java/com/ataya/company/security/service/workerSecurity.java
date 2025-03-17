package com.ataya.company.security.service;

import com.ataya.company.model.Worker;
import com.ataya.company.service.WorkerService;
import org.springframework.stereotype.Service;

@Service
public class workerSecurity {

    private final WorkerService workerService;

    public workerSecurity(WorkerService workerService) {
        this.workerService = workerService;
    }

    public boolean hasAccess(String workerId, String companyId, String storeId) {
        Worker worker = workerService.getWorkerById(workerId);
        if (companyId != null) {
            return worker.getCompanyId().equals(companyId);
        } else {
            return worker.getStoreId().equals(storeId);
        }
    }
}
