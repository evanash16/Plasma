package evan.ashley.plasma.dao;

import evan.ashley.plasma.model.api.ResourceNotFoundException;
import evan.ashley.plasma.model.dao.*;

public interface UserDao {

    CreateUserOutput createUser(CreateUserInput input);

    void updateUser(UpdateUserInput input) throws ResourceNotFoundException;

    GetUserOutput getUser(GetUserInput input) throws ResourceNotFoundException;
}
