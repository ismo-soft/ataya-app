package com.ataya.company.mapper;

import com.ataya.company.model.Worker;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;

@Service
public class WorkerMapper {


    public Object workerToWorkerDto(Worker worker, Class<?> workerDtoClass) {
        try {
            Object dto = workerDtoClass.getDeclaredConstructor().newInstance();
            Arrays.stream(workerDtoClass.getDeclaredFields())
                    .forEach(field -> {
                        try {
                            field.setAccessible(true);
                            Field workerField = Worker.class.getDeclaredField(field.getName());
                            workerField.setAccessible(true);
                            field.set(dto, workerField.get(worker));
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    });
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
