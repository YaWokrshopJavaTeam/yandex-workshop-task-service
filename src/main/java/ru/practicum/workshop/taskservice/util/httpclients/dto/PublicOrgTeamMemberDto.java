package ru.practicum.workshop.taskservice.util.httpclients.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicOrgTeamMemberDto {

    public enum Role {
        EXECUTOR,
        MANAGER;
    }

    private Long userId;

    private Role role;

}
