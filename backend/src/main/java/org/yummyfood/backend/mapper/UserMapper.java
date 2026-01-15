package org.yummyfood.backend.mapper;

import org.mapstruct.Mapper;
import org.yummyfood.backend.domain.User;
import org.yummyfood.backend.dto.response.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);
}
