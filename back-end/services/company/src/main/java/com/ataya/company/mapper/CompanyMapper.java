package com.ataya.company.mapper;

import com.ataya.company.dto.company.CompanyInfoResponse;
import com.ataya.company.model.Company;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    CompanyInfoResponse toCompanyInfoResponse(Company company);
}
