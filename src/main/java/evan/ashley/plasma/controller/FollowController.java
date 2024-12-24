package evan.ashley.plasma.controller;

import com.google.common.collect.ImmutableList;
import evan.ashley.plasma.dao.FollowDao;
import evan.ashley.plasma.model.api.follow.CreateFollowRequest;
import evan.ashley.plasma.model.api.follow.CreateFollowResponse;
import evan.ashley.plasma.model.api.follow.Follow;
import evan.ashley.plasma.model.api.follow.FollowsSortOrder;
import evan.ashley.plasma.model.api.follow.GetFollowResponse;
import evan.ashley.plasma.model.api.follow.ImmutableCreateFollowResponse;
import evan.ashley.plasma.model.api.follow.ImmutableGetFollowResponse;
import evan.ashley.plasma.model.api.follow.ImmutableListFollowsResponse;
import evan.ashley.plasma.model.api.follow.ListFollowsResponse;
import evan.ashley.plasma.model.api.ResourceNotFoundException;
import evan.ashley.plasma.model.api.ValidationException;
import evan.ashley.plasma.model.dao.follow.CreateFollowOutput;
import evan.ashley.plasma.model.dao.follow.GetFollowOutput;
import evan.ashley.plasma.model.dao.follow.ImmutableCreateFollowInput;
import evan.ashley.plasma.model.dao.follow.ImmutableDeleteFollowInput;
import evan.ashley.plasma.model.dao.follow.ImmutableGetFollowInput;
import evan.ashley.plasma.model.dao.follow.ImmutableListFollowsInput;
import evan.ashley.plasma.model.dao.follow.ListFollowsOutput;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;
import java.util.Optional;

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

    @GetMapping("/follow")
    public ListFollowsResponse listFollows(
            @RequestParam("followerId") final String followerId,
            @RequestParam("sortOrder") @Nullable final FollowsSortOrder sortOrder,
            @RequestParam("maxPageSize") @Nullable final Integer maxPageSize,
            @RequestParam("paginationToken") @Nullable final String paginationToken) {
        final ListFollowsOutput output = followDao.listFollows(ImmutableListFollowsInput.builder()
                .followerId(followerId)
                .sortOrder(Optional.ofNullable(sortOrder)
                        .map(FollowsSortOrder::toString)
                        .map(evan.ashley.plasma.model.dao.follow.FollowsSortOrder::valueOf)
                        .orElse(null))
                .maxPageSize(maxPageSize)
                .paginationToken(paginationToken)
                .build());
        return ImmutableListFollowsResponse.builder()
                .follows(output.getFollows().stream()
                        .map(Follow::fromInternal)
                        .collect(ImmutableList.toImmutableList()))
                .paginationToken(output.getPaginationToken())
                .build();
    }
}
