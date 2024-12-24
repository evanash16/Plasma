package evan.ashley.plasma.model.dao.user;

import org.immutables.value.Value;

@Value.Immutable
public interface CreateUserInput {

    String getUsername();

    String getPassword();
}
