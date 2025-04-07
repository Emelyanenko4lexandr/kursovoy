package dev.kursovoy.mapper;

import dev.kursovoy.DTO.LocationResponse;
import dev.kursovoy.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LocationMapper {
    LocationResponse toLocationResponse(Location location);
}
