package evan.ashley.plasma.controller;

import evan.ashley.plasma.constant.Header;
import evan.ashley.plasma.dao.SessionDao;
import evan.ashley.plasma.dao.UserDao;
import evan.ashley.plasma.model.api.ResourceNotFoundException;
import evan.ashley.plasma.model.api.UnauthorizedException;
import evan.ashley.plasma.model.api.ValidationException;
import evan.ashley.plasma.model.api.auth.AuthIdentityResponse;
import evan.ashley.plasma.model.api.auth.AuthLoginRequest;
import evan.ashley.plasma.model.api.auth.AuthLoginResponse;
import evan.ashley.plasma.model.api.auth.ImmutableAuthIdentityResponse;
import evan.ashley.plasma.model.api.auth.ImmutableAuthLoginResponse;
import evan.ashley.plasma.model.dao.session.CreateSessionOutput;
import evan.ashley.plasma.model.dao.session.GetSessionOutput;
import evan.ashley.plasma.model.dao.session.ImmutableCreateSessionInput;
import evan.ashley.plasma.model.dao.session.ImmutableGetSessionInput;
import evan.ashley.plasma.model.dao.user.GetUserOutput;
import evan.ashley.plasma.model.dao.user.ImmutableGetUserInput;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private UserDao userDao;
    @Autowired
    private SessionDao sessionDao;

    @PostMapping("/auth/login")
    public AuthLoginResponse createAuthLogin(@RequestBody final AuthLoginRequest request)
            throws ResourceNotFoundException, UnauthorizedException, ValidationException {
        final GetUserOutput getUserOutput = userDao.getUser(ImmutableGetUserInput.builder()
                .username(request.getUsername())
                .build());
        if (!BCrypt.checkpw(request.getPassword(), getUserOutput.getPassword())) {
            throw new UnauthorizedException("The entered password was not correct.");
        }

        final CreateSessionOutput createSessionOutput = sessionDao.createSession(ImmutableCreateSessionInput.builder()
                .userId(getUserOutput.getId())
                .build());
        return ImmutableAuthLoginResponse.builder()
                .id(createSessionOutput.getId())
                .build();
    }

    @GetMapping("/auth/identity")
    public AuthIdentityResponse getAuthIdentity(@RequestHeader(Header.SESSION) final String sessionId) throws ResourceNotFoundException {
        final GetSessionOutput output = sessionDao.getSession(ImmutableGetSessionInput.builder()
                .id(sessionId)
                .build());
        return ImmutableAuthIdentityResponse.builder()
                .id(output.getId())
                .userId(output.getUserId())
                .creationTime(output.getCreationTime())
                .build();
    }
}
