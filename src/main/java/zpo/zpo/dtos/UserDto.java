package zpo.zpo.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Long id;
//    private String email;
    private String firstName;
    private String lastName;
    private String login;
    private String token;
    private String role;
    private Boolean verified;
}
