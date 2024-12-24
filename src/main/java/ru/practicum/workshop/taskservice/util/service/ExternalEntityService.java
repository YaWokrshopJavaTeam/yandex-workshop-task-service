package ru.practicum.workshop.taskservice.util.service;

import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.workshop.taskservice.exceptions.ConflictException;
import ru.practicum.workshop.taskservice.util.httpclients.EventServiceClient;
import ru.practicum.workshop.taskservice.util.httpclients.UserServiceClient;
import ru.practicum.workshop.taskservice.util.httpclients.dto.PublicOrgTeamMemberDto;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExternalEntityService {

    private final UserServiceClient userServiceClient;
    private final EventServiceClient eventServiceClient;

    public void checkUserExistence(Long userId) throws EntityNotFoundException {
        try {
            userServiceClient.getUser(userId);
        } catch (FeignException.NotFound e) {
            throw new EntityNotFoundException(String.format("User with id=%d not found.", userId));
        }
    }

    public void checkEventExistence(Long eventId) throws EntityNotFoundException {
        try {
            eventServiceClient.getEvent(eventId);
        } catch (FeignException.NotFound e) {
            throw new EntityNotFoundException(String.format("Event with id=%d not found.", eventId));
        }
    }

    public void checkEventTeamAffiliation(Long eventId, Collection<Long> usersIds) {
        Long eventOwnerId = null;

        try {
            eventOwnerId = eventServiceClient.getEvent(eventId).getOwnerId();
        } catch (FeignException.NotFound e) {
            throw new EntityNotFoundException(String.format("Event with id=%d not found.", eventId));
        }

        List<PublicOrgTeamMemberDto> teamMembers = eventServiceClient.getTeamMembers(eventId);
        Set<Long> teamMembersIds = teamMembers.stream().map(PublicOrgTeamMemberDto::getUserId)
                .collect(Collectors.toCollection(HashSet::new));
        teamMembersIds.add(eventOwnerId);

        Set<Long> notTeamMembersIds = new HashSet<>(usersIds);
        notTeamMembersIds.removeAll(teamMembersIds);

        if (!notTeamMembersIds.isEmpty()) {
            throw new ConflictException(String.format(
                    "Users %s are not in event(id=%d) org team.", notTeamMembersIds.toString(), eventId));
        }

    }

}
