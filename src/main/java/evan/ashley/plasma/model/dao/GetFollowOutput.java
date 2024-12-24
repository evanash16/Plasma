package evan.ashley.plasma.model.dao;

import org.immutables.value.Value;

import java.time.Instant;

@Value.Immutable
public interface GetFollowOutput {

    String getId();

    String getFollowerId();

    String getFolloweeId();

    Instant getCreationTime();
}
