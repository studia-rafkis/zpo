package zpo.zpo.services;

        import lombok.RequiredArgsConstructor;
        import org.springframework.http.HttpStatus;
        import org.springframework.security.crypto.password.PasswordEncoder;
        import org.springframework.stereotype.Service;
        import zpo.zpo.dtos.CredentialsDto;
        import zpo.zpo.dtos.SignUpDto;
        import zpo.zpo.dtos.UserDto;
        import zpo.zpo.entities.User;
        import zpo.zpo.exceptions.AppException;
        import zpo.zpo.mappers.UserMapper;
        import zpo.zpo.repositories.UserRepository;

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

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()), user.getPassword())) {

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
            user.setRole("ROLE_USER"); // Domyślna rola

        }




        user.setVerified(false);


        User savedUser = userRepository.save(user);

        verificationService.registerUser(user);

        return userMapper.toUserDto(savedUser);
    }

    public UserDto findByLogin(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }



}