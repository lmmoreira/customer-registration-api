package com.test.customer.application.usecases;

@FunctionalInterface
public interface VoidUseCase<INPUT> {

    void execute(INPUT input);
}