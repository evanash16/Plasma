package evan.ashley.plasma.dao;

import evan.ashley.plasma.model.api.ResourceNotFoundException;
import evan.ashley.plasma.model.api.ValidationException;
import evan.ashley.plasma.model.dao.user.CreateUserInput;
import evan.ashley.plasma.model.dao.user.CreateUserOutput;
import evan.ashley.plasma.model.dao.user.GetUserInput;
import evan.ashley.plasma.model.dao.user.GetUserOutput;
import evan.ashley.plasma.model.dao.user.SearchUsersInput;
import evan.ashley.plasma.model.dao.user.SearchUsersOutput;
import evan.ashley.plasma.model.dao.user.UpdateUserInput;

public interface UserDao {

    CreateUserOutput createUser(CreateUserInput input) throws ValidationException;

    void updateUser(UpdateUserInput input) throws ResourceNotFoundException, ValidationException;

    GetUserOutput getUser(GetUserInput input) throws ResourceNotFoundException, ValidationException;

    SearchUsersOutput searchUsers(SearchUsersInput input);
}
