package dev.kursovoy.mapper;

import dev.kursovoy.DTO.MessageResponse;
import dev.kursovoy.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MessageMapper {
    MessageResponse toResponse(Message message);

    List<MessageResponse> toResponseList(List<Message> messages);
}
