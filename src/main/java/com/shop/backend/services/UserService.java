package com.shop.backend.services;
import com.shop.backend.dtos.CredentialsDto;
        import com.shop.backend.dtos.SignUpDto;
        import com.shop.backend.dtos.UserDto;
        import com.shop.backend.entities.User;
        import com.shop.backend.entities.VerificationToken;
        import com.shop.backend.exceptions.AppException;
        import com.shop.backend.mappers.UserMapper;
        import com.shop.backend.repositories.UserRepository;
        import lombok.RequiredArgsConstructor;
        import org.springframework.http.HttpStatus;
        import org.springframework.security.crypto.password.PasswordEncoder;
        import org.springframework.stereotype.Service;

        import java.nio.CharBuffer;
        import java.util.Optional;
        import java.util.UUID;
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationService verificationService;
    private final UserMapper userMapper;
    public UserDto login(CredentialsDto credentialsDto) {
        User user = userRepository.findByLogin(credentialsDto.login())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()), user.getPassword()) && !user.isOauth_provider()) {

            return userMapper.toUserDto(user);
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto register(SignUpDto userDto) {
        Optional<User> optionalUser = userRepository.findByLogin(userDto.login());

        if (optionalUser.isPresent()) {
            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.signUpToUser(userDto);

        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.password())));

        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }

        if(user.isOauth_provider())
        {
            user.setVerified(true);
        }
        else
        {
            user.setVerified(false);
        }

        User savedUser = userRepository.save(user);
        verificationService.registerUser(user);
        return userMapper.toUserDto(savedUser);
    }
    public UserDto findByLogin(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }
    public UserDto updateUser(String email, UserDto updatedUserDto) {
        User existingUser = userRepository.findByLogin(email)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        existingUser.setCountry(updatedUserDto.getCountry());
        existingUser.setHouseNumber(updatedUserDto.getHouseNumber());
        existingUser.setPhone(updatedUserDto.getPhone());
        existingUser.setState(updatedUserDto.getState());
        existingUser.setStreet(updatedUserDto.getStreet());
        existingUser.setStreetNumber(updatedUserDto.getStreetNumber());
        existingUser.setZipCode(updatedUserDto.getZipCode());
        User updatedUser = userRepository.save(existingUser);

        return userMapper.toUserDto(updatedUser);
    }

    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByLogin(email)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }

    public boolean checkOldPassword(String email, String oldPassword) {
        User user = userRepository.findByLogin(email)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }
    public boolean updatePassword(String email, String newPassword) {
        User user = userRepository.findByLogin(email)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }
}

