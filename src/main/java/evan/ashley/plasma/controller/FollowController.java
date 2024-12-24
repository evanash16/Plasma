package evan.ashley.plasma.controller;

import evan.ashley.plasma.dao.FollowDao;
import evan.ashley.plasma.model.api.CreateFollowRequest;
import evan.ashley.plasma.model.api.CreateFollowResponse;
import evan.ashley.plasma.model.api.ImmutableCreateFollowResponse;
import evan.ashley.plasma.model.dao.CreateFollowOutput;
import evan.ashley.plasma.model.dao.ImmutableCreateFollowInput;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class FollowController {

    @Autowired
    private FollowDao followDao;

    @PostMapping("/follow")
    public CreateFollowResponse createFollow(@RequestBody final CreateFollowRequest request) {
        final CreateFollowOutput output = followDao.createFollow(ImmutableCreateFollowInput.builder()
                .followerId(request.getFollowerId())
                .followeeId(request.getFolloweeId())
                .build());
        return ImmutableCreateFollowResponse.builder()
                .id(output.getId())
                .build();
    }
}
