package evan.ashley.plasma.controller;

import evan.ashley.plasma.dao.UserDao;
import evan.ashley.plasma.model.api.CreateUserRequest;
import evan.ashley.plasma.model.api.CreateUserResponse;
import evan.ashley.plasma.model.api.ImmutableCreateUserRequest;
import evan.ashley.plasma.model.api.ImmutableCreateUserResponse;
import evan.ashley.plasma.model.dao.CreateUserOutput;
import evan.ashley.plasma.model.dao.ImmutableCreateUserInput;
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
    public CreateUserResponse createUser(@RequestBody CreateUserRequest createUserRequest) {
        final CreateUserOutput output = userDao.createUser(ImmutableCreateUserInput.builder()
                .username(createUserRequest.getUsername())
                .password(createUserRequest.getPassword())
                .build());
        return ImmutableCreateUserResponse.builder()
                .id(output.getId())
                .build();
    }
}
