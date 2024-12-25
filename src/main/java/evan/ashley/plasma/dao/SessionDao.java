package evan.ashley.plasma.dao;

import evan.ashley.plasma.model.api.ResourceNotFoundException;
import evan.ashley.plasma.model.api.ValidationException;
import evan.ashley.plasma.model.dao.session.CreateSessionInput;
import evan.ashley.plasma.model.dao.session.CreateSessionOutput;
import evan.ashley.plasma.model.dao.session.GetSessionInput;
import evan.ashley.plasma.model.dao.session.GetSessionOutput;

public interface SessionDao {

    CreateSessionOutput createSession(CreateSessionInput input) throws ValidationException;

    GetSessionOutput getSession(GetSessionInput input) throws ResourceNotFoundException;
}
