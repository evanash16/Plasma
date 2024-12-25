package evan.ashley.plasma.model.dao.follow;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.time.Instant;

@Value.Immutable
@JsonDeserialize(as = ImmutableFollowsPaginationToken.class)
public interface FollowsPaginationToken {

    Instant getLastCreationTime();

    Instant getPointInTime();
}
