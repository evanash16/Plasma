package evan.ashley.plasma.dao;

import evan.ashley.plasma.model.api.ResourceNotFoundException;
import evan.ashley.plasma.model.api.ValidationException;
import evan.ashley.plasma.model.dao.follow.CreateFollowInput;
import evan.ashley.plasma.model.dao.follow.CreateFollowOutput;
import evan.ashley.plasma.model.dao.follow.DeleteFollowInput;
import evan.ashley.plasma.model.dao.follow.GetFollowInput;
import evan.ashley.plasma.model.dao.follow.GetFollowOutput;
import evan.ashley.plasma.model.dao.follow.ListFollowsInput;
import evan.ashley.plasma.model.dao.follow.ListFollowsOutput;

public interface FollowDao {

    CreateFollowOutput createFollow(CreateFollowInput input) throws ValidationException;

    void deleteFollow(DeleteFollowInput input) throws ResourceNotFoundException;

    GetFollowOutput getFollow(GetFollowInput input) throws ResourceNotFoundException, ValidationException;

    ListFollowsOutput listFollows(ListFollowsInput input);
}