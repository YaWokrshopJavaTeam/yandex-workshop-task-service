package ru.practicum.workshop.taskservice.util.httpclients.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserDto {

    private Long id;

    private String name;

    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    private String aboutMe;

}
