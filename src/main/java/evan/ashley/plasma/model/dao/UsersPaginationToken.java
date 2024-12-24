package evan.ashley.plasma.model.dao;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.time.Instant;

@Value.Immutable
@JsonDeserialize(as = ImmutableUsersPaginationToken.class)
public interface UsersPaginationToken {

    Integer getPreviousItemCount();

    Instant getPointInTime();
}
