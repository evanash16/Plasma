package evan.ashley.plasma.model.dao;

import org.immutables.value.Value;

import java.time.Instant;

@Value.Immutable
public interface GetUserOutput {

    String getId();

    String getUsername();

    Instant getCreationTime();
}
