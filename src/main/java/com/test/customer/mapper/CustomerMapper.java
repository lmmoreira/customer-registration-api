package com.test.customer.mapper;

import com.test.customer.domain.CustomerEntity;
import com.test.customer.dto.CustomerDTO;
import com.test.customer.dto.request.RequestCustomerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(source = "phone", target = "telephone")
    @Mapping(source = "zipCode", target = "postCode")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "country", ignore = true)
    @Mapping(target = "place", ignore = true)
    @Mapping(target = "state", ignore = true)
    CustomerEntity requestToEntity(RequestCustomerDTO dto);

    @Mapping(source = "telephone", target = "phone")
    CustomerDTO toDto(CustomerEntity entity);

}