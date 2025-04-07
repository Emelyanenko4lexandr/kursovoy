package dev.kursovoy.mapper;

import dev.kursovoy.DTO.PhotoResponse;
import dev.kursovoy.entity.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PhotoMapper {
    PhotoResponse toResponse(Photo photo);

    List<PhotoResponse> toResponseList(List<Photo> photos);
}
