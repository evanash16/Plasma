package evan.ashley.plasma.dao;

import evan.ashley.plasma.model.dao.session.CreateSessionInput;
import evan.ashley.plasma.model.dao.session.CreateSessionOutput;

public interface SessionDao {

    CreateSessionOutput createSession(CreateSessionInput input);
}
