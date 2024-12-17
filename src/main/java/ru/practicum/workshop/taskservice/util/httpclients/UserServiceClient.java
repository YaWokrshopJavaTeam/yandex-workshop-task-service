package ru.practicum.workshop.taskservice.util.httpclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.workshop.taskservice.util.httpclients.dto.UserDto;

@FeignClient(name = "userServiceClient", url = "http://host.docker.internal:8081")
public interface UserServiceClient {

    @GetMapping("/users/{userId}")
    UserDto getUser(@PathVariable(name = "userId") Long userId);

}
