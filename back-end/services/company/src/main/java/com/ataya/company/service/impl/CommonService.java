package com.ataya.company.service.impl;

import com.ataya.company.dto.company.CompanyInfoResponse;
import com.ataya.company.dto.worker.response.WorkerInfoResponse;
import com.ataya.company.enums.Role;
import com.ataya.company.mapper.CompanyMapper;
import com.ataya.company.model.Company;
import com.ataya.company.model.Worker;
import com.ataya.company.repo.CompanyRepository;
import com.ataya.company.repo.WorkerRepository;
import com.ataya.company.service.CompanyService;
import com.ataya.company.service.WorkerService;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.expression.spel.ast.Operator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class CommonService {

    private final CompanyRepository companyRepository;
    private final WorkerRepository workerRepository;
    private final CompanyMapper companyMapper;

    public CommonService(CompanyRepository companyRepository, WorkerRepository workerRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.workerRepository = workerRepository;
        this.companyMapper = companyMapper;
    }

    public static void addCriteria(List<Criteria> criteria, String field, String value) {
        if (value != null && !value.isEmpty()) {
            List<Criteria> fieldCriteria = Stream.of(value.split(","))
                    .map(str -> Criteria.where(field).regex(".*" + str + ".*", "i"))
                    .toList();
            criteria.add(new Criteria().orOperator(fieldCriteria.toArray(new Criteria[0])));
        }
    }

    public static void addCriteriaWithRange(List<Criteria> criteria, String field, String value) {
        if (value != null && !value.isEmpty()) {
            String[] range = value.split("-");
            if (range.length == 2) {
                criteria.add(Criteria.where(field).gte(range[0]).lte(range[1]));
            }
        }

    }

    public CompanyInfoResponse getCompanyById(String companyId) {
        Company company = companyRepository.findById(companyId).orElse(null);
        if (company == null) {
            return null;
        }
        return companyMapper.toCompanyInfoResponse(company);
    }

    public WorkerInfoResponse getWorkerById(String updatedBy) {
        Worker worker = workerRepository.findById(updatedBy).orElse(null);
        if (worker == null) {
            return null;
        }
        return WorkerInfoResponse.builder()
                .id(worker.getId())
                .name(worker.getName())
                .surname(worker.getSurname())
                .username(worker.getUsername())
                .email(worker.getEmail())
                .phone(worker.getPhoneNumber())
                .profilePicture(worker.getProfilePicture())
                .companyId(worker.getCompanyId())
                .storeId(worker.getStoreId())
                .roles(worker.getRoles().stream().map(Role::name).toList())
                .build();

    }
}
