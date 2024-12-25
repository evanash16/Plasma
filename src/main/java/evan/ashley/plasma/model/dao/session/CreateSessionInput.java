package evan.ashley.plasma.model.dao.session;

import org.immutables.value.Value;

@Value.Immutable
public interface CreateSessionInput {

    String getUserId();
}
