package com.ataya.address.mapper;

import com.ataya.address.model.Address;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

@Service
public class AddressMapper {

    public Object toDto(Address address, Class<?> objectClass) {
        try {
            Object dto = objectClass.getDeclaredConstructor().newInstance();
            Arrays.stream(objectClass.getDeclaredFields())
                    .forEach(field -> {
                        try {
                            field.setAccessible(true);
                            Field addressField = address.getClass().getDeclaredField(field.getName());
                            addressField.setAccessible(true);
                            field.set(dto, address.getClass().getDeclaredField(field.getName()).get(address));
                        } catch (NoSuchFieldException e) {
                            throw new RuntimeException(e);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    });
            return dto;
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

}
