package dev.kursovoy.mapper;

import dev.kursovoy.DTO.RentResponse;
import dev.kursovoy.entity.Rent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RentMapper {

    @Mapping(target = "tenantName", source = "tenant.cred.username")
    @Mapping(target = "carBrand", source = "auto.brand")
    @Mapping(target = "carModel", source = "auto.model")
    @Mapping(target = "carRegistrationNumber", source = "auto.registrationNumber")
    RentResponse toRentResponse(Rent rent);

    List<RentResponse> toRentResponseList(List<Rent> rents);
}
