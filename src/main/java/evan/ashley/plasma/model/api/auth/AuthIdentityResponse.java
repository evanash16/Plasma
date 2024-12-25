package evan.ashley.plasma.model.api.auth;

import org.immutables.value.Value;

import java.time.Instant;

@Value.Immutable
public interface AuthIdentityResponse {

    String getId();

    String getUserId();

    Instant getCreationTime();
}
