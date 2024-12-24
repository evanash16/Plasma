package evan.ashley.plasma.model.dao;

import org.immutables.value.Value;

@Value.Immutable
public interface CreateUserInput {

    String getUsername();

    String getPassword();
}
