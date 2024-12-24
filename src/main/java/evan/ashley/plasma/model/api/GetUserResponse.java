package evan.ashley.plasma.model.api;

import org.immutables.value.Value;

import java.time.Instant;

@Value.Immutable
public interface GetUserResponse {

    String getId();

    String getUsername();

    Instant getCreationTime();
}
