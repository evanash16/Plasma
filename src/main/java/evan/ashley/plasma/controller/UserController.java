package evan.ashley.plasma.controller;

import evan.ashley.plasma.dao.UserDao;
import evan.ashley.plasma.model.api.*;
import evan.ashley.plasma.model.dao.CreateUserOutput;
import evan.ashley.plasma.model.dao.GetUserOutput;
import evan.ashley.plasma.model.dao.ImmutableCreateUserInput;
import evan.ashley.plasma.model.dao.ImmutableGetUserInput;
import jakarta.websocket.server.PathParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;

@Log4j2
@RestController
public class UserController {

    @Autowired
    private UserDao userDao;

    @PostMapping("/user")
    public CreateUserResponse createUser(@RequestBody final CreateUserRequest createUserRequest) {
        final CreateUserOutput output = userDao.createUser(ImmutableCreateUserInput.builder()
                .username(createUserRequest.getUsername())
                .password(createUserRequest.getPassword())
                .build());
        return ImmutableCreateUserResponse.builder()
                .id(output.getId())
                .build();
    }

    @GetMapping("/user/{id}")
    public GetUserResponse getUser(@PathVariable("id") final String id) throws ResourceNotFoundException {
        final GetUserOutput output = userDao.getUser(ImmutableGetUserInput.builder()
                .id(id)
                .build());
        return ImmutableGetUserResponse.builder()
                .id(output.getId())
                .username(output.getUsername())
                .creationTime(output.getCreationTime())
                .build();
    }
}
