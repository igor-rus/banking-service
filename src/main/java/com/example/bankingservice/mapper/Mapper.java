package com.example.bankingservice.mapper;

public interface Mapper<T, R> {
    R toDTO(T t);
}
