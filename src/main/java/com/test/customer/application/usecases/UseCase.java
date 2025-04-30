package com.test.customer.application.usecases;

@FunctionalInterface
public interface UseCase<INPUT, OUTPUT> {

    OUTPUT execute(INPUT input);

}
