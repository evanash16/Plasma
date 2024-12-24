package evan.ashley.plasma.dao;

import evan.ashley.plasma.model.api.ResourceNotFoundException;
import evan.ashley.plasma.model.dao.CreateUserInput;
import evan.ashley.plasma.model.dao.CreateUserOutput;
import evan.ashley.plasma.model.dao.GetUserInput;
import evan.ashley.plasma.model.dao.GetUserOutput;

public interface UserDao {

    CreateUserOutput createUser(CreateUserInput input);

    GetUserOutput getUser(GetUserInput input) throws ResourceNotFoundException;
}
