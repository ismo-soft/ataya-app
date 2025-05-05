package com.ataya.inventory.service.impl;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class CommonService {
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
                try {

                        Double lowerBound = Double.parseDouble(range[0]);
                        Double upperBound = Double.parseDouble(range[1]);
                        criteria.add(Criteria.where(field).gte(lowerBound).lte(upperBound));

                } catch (NumberFormatException e) {
                    // Handle parsing error - could log warning or throw custom exception
                    System.err.println("Invalid number format for " + field + " range: " + value);
                }
            }
        }

    }
}
