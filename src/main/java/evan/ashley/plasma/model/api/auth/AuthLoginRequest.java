package evan.ashley.plasma.model.api.auth;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableAuthLoginRequest.class)
public interface AuthLoginRequest {

    String getUsername();

    String getPassword();
}
