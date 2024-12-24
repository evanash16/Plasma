package evan.ashley.plasma.model.api;

import org.immutables.value.Value;

import java.time.Instant;

@Value.Immutable
public interface GetFollowResponse {

    String getId();

    String getFollowerId();

    String getFolloweeId();

    Instant getCreationTime();
}
