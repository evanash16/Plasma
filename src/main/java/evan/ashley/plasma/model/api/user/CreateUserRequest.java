package evan.ashley.plasma.model.api.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableCreateUserRequest.class)
public interface CreateUserRequest {

    String getUsername();

    String getPassword();
}
