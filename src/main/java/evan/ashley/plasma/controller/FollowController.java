package evan.ashley.plasma.controller;

import evan.ashley.plasma.dao.FollowDao;
import evan.ashley.plasma.model.api.*;
import evan.ashley.plasma.model.dao.CreateFollowOutput;
import evan.ashley.plasma.model.dao.ImmutableCreateFollowInput;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
public class FollowController {

    @Autowired
    private FollowDao followDao;

    @PostMapping("/follow")
    public CreateFollowResponse createFollow(@RequestBody final CreateFollowRequest request) throws ValidationException {
        final CreateFollowOutput output = followDao.createFollow(ImmutableCreateFollowInput.builder()
                .followerId(request.getFollowerId())
                .followeeId(request.getFolloweeId())
                .build());
        return ImmutableCreateFollowResponse.builder()
                .id(output.getId())
                .build();
    }

    @DeleteMapping("/follow/{id}")
    public void deleteFollow(@PathVariable("id") final String id) throws ResourceNotFoundException {
        followDao.deleteFollow(ImmutableDeleteFollowInput.builder()
                .id(id)
                .build());
    }
}
