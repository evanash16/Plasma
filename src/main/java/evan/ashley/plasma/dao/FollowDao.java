package evan.ashley.plasma.dao;

import evan.ashley.plasma.model.dao.*;
import evan.ashley.plasma.model.api.ResourceNotFoundException;
import evan.ashley.plasma.model.api.ValidationException;

public interface FollowDao {

    CreateFollowOutput createFollow(CreateFollowInput input) throws ValidationException;

    void deleteFollow(DeleteFollowInput input) throws ResourceNotFoundException;

    GetFollowOutput getFollow(GetFollowInput input) throws ResourceNotFoundException;
}