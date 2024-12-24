package evan.ashley.plasma.dao;

import evan.ashley.plasma.model.dao.CreateUserInput;
import evan.ashley.plasma.model.dao.CreateUserOutput;

public interface UserDao {

    CreateUserOutput createUser(CreateUserInput input);
}
