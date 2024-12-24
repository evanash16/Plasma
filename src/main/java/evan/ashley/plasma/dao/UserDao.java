package evan.ashley.plasma.dao;

import evan.ashley.plasma.model.api.ResourceNotFoundException;
import evan.ashley.plasma.model.api.ValidationException;
import evan.ashley.plasma.model.dao.*;

public interface UserDao {

    CreateUserOutput createUser(CreateUserInput input) throws ValidationException;

    void updateUser(UpdateUserInput input) throws ResourceNotFoundException, ValidationException;

    GetUserOutput getUser(GetUserInput input) throws ResourceNotFoundException;

    SearchUsersOutput searchUsers(SearchUsersInput input);
}
