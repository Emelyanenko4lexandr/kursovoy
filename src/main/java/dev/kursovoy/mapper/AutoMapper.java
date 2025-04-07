package dev.kursovoy.mapper;

import dev.kursovoy.DTO.AllParametersDTO;
import dev.kursovoy.DTO.AutoResponse;
import dev.kursovoy.DTO.OwnerAutoResponse;
import dev.kursovoy.entity.Automobile;
import dev.kursovoy.entity.Rent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AutoMapper {
    AutoResponse toAutoResponse(Automobile auto);

    List<AutoResponse> toAutoResponseList(List<Automobile> autos);

    @Mapping(target = "owner", source = "owner.fio")
    @Mapping(target = "tenant", source = "rent.tenant.fio")
    @Mapping(target = "latitude", source = "location.latitude")
    @Mapping(target = "longitude", source = "location.longitude")
    AllParametersDTO toAllParametersDTO(Automobile auto);

    List<AllParametersDTO> toAllParametersDTOList(List<Automobile> autos);

    OwnerAutoResponse toOwnerAutoResponse(Automobile auto);

    List<OwnerAutoResponse> toOwnerAutoResponseList(List<Automobile> autos);

    @Mapping(target = "id", source = "auto.id")
    @Mapping(target = "brand", source = "auto.brand")
    @Mapping(target = "model", source = "auto.model")
    @Mapping(target = "registration_number", source = "auto.registrationNumber")
    @Mapping(target = "status", source = "auto.status")
    @Mapping(target = "startRental", source = "startRental")
    AutoResponse toAutoResponseFromRent(Rent rent);

    List<AutoResponse> toAutoResponseListFromRent(List<Rent> rents);
}
