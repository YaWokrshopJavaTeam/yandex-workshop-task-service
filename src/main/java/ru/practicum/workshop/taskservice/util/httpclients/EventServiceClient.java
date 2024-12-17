package ru.practicum.workshop.taskservice.util.httpclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.practicum.workshop.taskservice.util.httpclients.dto.EventDto;
import ru.practicum.workshop.taskservice.util.httpclients.dto.PublicOrgTeamMemberDto;

import java.util.List;

@FeignClient(name = "eventServiceClient", url = "http://host.docker.internal:8082")
public interface EventServiceClient {

    @GetMapping("/events/{eventId}")
    EventDto getEvent(@RequestHeader(name = "X-User-Id") Long userId,
                      @PathVariable(name = "eventId") Long eventId);

    @GetMapping("/events/orgs/{eventId}")
    List<PublicOrgTeamMemberDto> getTeamMembers(@PathVariable(name = "eventId") Long eventId);

}
