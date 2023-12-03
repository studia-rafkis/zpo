package zpo.zpo.mappers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import zpo.zpo.dtos.SignUpDto;
import zpo.zpo.dtos.UserDto;
import zpo.zpo.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);

//    @Mapping(target = "email", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "verified", ignore = true)
    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto userDto);
}