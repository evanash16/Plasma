package evan.ashley.plasma.dao;

import evan.ashley.plasma.model.dao.DeleteFollowInput;
import evan.ashley.plasma.model.api.ResourceNotFoundException;
import evan.ashley.plasma.model.api.ValidationException;
import evan.ashley.plasma.model.dao.CreateFollowInput;
import evan.ashley.plasma.model.dao.CreateFollowOutput;

public interface FollowDao {

    CreateFollowOutput createFollow(CreateFollowInput input) throws ValidationException;

    void deleteFollow(DeleteFollowInput input) throws ResourceNotFoundException;
}
