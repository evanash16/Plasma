package evan.ashley.plasma.controller;

import evan.ashley.plasma.dao.FollowDao;
import evan.ashley.plasma.model.api.CreateFollowRequest;
import evan.ashley.plasma.model.api.CreateFollowResponse;
import evan.ashley.plasma.model.api.GetFollowResponse;
import evan.ashley.plasma.model.api.ImmutableCreateFollowResponse;
import evan.ashley.plasma.model.api.ImmutableGetFollowResponse;
import evan.ashley.plasma.model.api.ResourceNotFoundException;
import evan.ashley.plasma.model.api.ValidationException;
import evan.ashley.plasma.model.dao.CreateFollowOutput;
import evan.ashley.plasma.model.dao.GetFollowOutput;
import evan.ashley.plasma.model.dao.ImmutableCreateFollowInput;
import evan.ashley.plasma.model.dao.ImmutableDeleteFollowInput;
import evan.ashley.plasma.model.dao.ImmutableGetFollowInput;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/follow/{id}")
    public GetFollowResponse getFollow(@PathVariable("id") final String id) throws ResourceNotFoundException {
        final GetFollowOutput output = followDao.getFollow(ImmutableGetFollowInput.builder()
                .id(id)
                .build());
        return ImmutableGetFollowResponse.builder()
                .id(output.getId())
                .followerId(output.getFollowerId())
                .followeeId(output.getFolloweeId())
                .creationTime(output.getCreationTime())
                .build();
    }
}
