package evan.ashley.plasma.dao;

import evan.ashley.plasma.model.dao.CreateFollowInput;
import evan.ashley.plasma.model.dao.CreateFollowOutput;

public interface FollowDao {

    CreateFollowOutput createFollow(CreateFollowInput input);
}
