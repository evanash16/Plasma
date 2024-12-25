package evan.ashley.plasma.model.dao.post;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.time.Instant;

@Value.Immutable
@JsonDeserialize(as = ImmutablePostsPaginationToken.class)
public interface PostsPaginationToken {

    Instant getLastCreationTime();

    Instant getPointInTime();
}
