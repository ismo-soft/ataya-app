package com.ataya.company.service.impl;

import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.stream.Stream;

public class CommonService {
    public static void addCriteria(List<Criteria> criteria, String field, String value) {
        if (value != null && !value.isEmpty()) {
            List<Criteria> fieldCriteria = Stream.of(value.split(","))
                    .map(str -> Criteria.where(field).regex(".*" + str + ".*", "i"))
                    .toList();
            criteria.add(new Criteria().orOperator(fieldCriteria.toArray(new Criteria[0])));
        }
    }
}
