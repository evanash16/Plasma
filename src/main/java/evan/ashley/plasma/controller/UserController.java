package evan.ashley.plasma.controller;

import com.google.common.collect.ImmutableList;
import evan.ashley.plasma.dao.UserDao;
import evan.ashley.plasma.model.api.*;
import evan.ashley.plasma.model.api.user.CreateUserRequest;
import evan.ashley.plasma.model.api.user.CreateUserResponse;
import evan.ashley.plasma.model.api.user.GetUserResponse;
import evan.ashley.plasma.model.api.user.ImmutableCreateUserResponse;
import evan.ashley.plasma.model.api.user.ImmutableGetUserResponse;
import evan.ashley.plasma.model.api.user.ImmutableSearchUsersResponse;
import evan.ashley.plasma.model.api.user.SearchUsersResponse;
import evan.ashley.plasma.model.api.user.UpdateUserRequest;
import evan.ashley.plasma.model.api.user.User;
import evan.ashley.plasma.model.dao.user.CreateUserOutput;
import evan.ashley.plasma.model.dao.user.GetUserOutput;
import evan.ashley.plasma.model.dao.user.ImmutableCreateUserInput;
import evan.ashley.plasma.model.dao.user.ImmutableGetUserInput;
import evan.ashley.plasma.model.dao.user.ImmutableSearchUsersInput;
import evan.ashley.plasma.model.dao.user.ImmutableUpdateUserInput;
import evan.ashley.plasma.model.dao.user.SearchUsersOutput;
import io.micrometer.common.lang.Nullable;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
public class UserController {

    @Autowired
    private UserDao userDao;

    @PostMapping("/user")
    public CreateUserResponse createUser(@RequestBody final CreateUserRequest createUserRequest) throws ValidationException {
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

    @PatchMapping("/user/{id}")
    public void updateUser(@PathVariable("id") final String id, @RequestBody final UpdateUserRequest request) throws ResourceNotFoundException, ValidationException {
        userDao.updateUser(ImmutableUpdateUserInput.builder()
                .id(id)
                .username(request.getUsername())
                .password(request.getPassword())
                .build());
    }

    @GetMapping("/user")
    public SearchUsersResponse searchUsers(
            @RequestParam("usernameSearchString") final String usernameSearchString,
            @RequestParam("maxPageSize") @Nullable final Integer maxPageSize,
            @RequestParam("paginationToken") @Nullable final String paginationToken) {
        final SearchUsersOutput output = userDao.searchUsers(ImmutableSearchUsersInput.builder()
                .usernameSearchString(usernameSearchString)
                .maxPageSize(maxPageSize)
                .paginationToken(paginationToken)
                .build());
        return ImmutableSearchUsersResponse.builder()
                .users(output.getUsers().stream()
                        .map(User::fromInternal)
                        .collect(ImmutableList.toImmutableList()))
                .paginationToken(output.getPaginationToken())
                .build();
    }
}
