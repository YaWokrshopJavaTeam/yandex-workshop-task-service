package ru.practicum.workshop.taskservice.util.httpclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.workshop.taskservice.util.httpclients.dto.EventDto;
import ru.practicum.workshop.taskservice.util.httpclients.dto.PublicOrgTeamMemberDto;

import java.util.List;

@FeignClient(name = "eventServiceClient", url = "http://host.docker.internal:8082", path = "/events")
public interface EventServiceClient {

    @GetMapping("{eventId}")
    EventDto getEvent(@PathVariable(name = "eventId") Long eventId);

    @GetMapping("/orgs/{eventId}")
    List<PublicOrgTeamMemberDto> getTeamMembers(@PathVariable(name = "eventId") Long eventId);

}
