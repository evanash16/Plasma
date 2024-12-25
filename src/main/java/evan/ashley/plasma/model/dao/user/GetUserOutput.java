package evan.ashley.plasma.model.dao.user;

import org.immutables.value.Value;

import java.time.Instant;

@Value.Immutable
public interface GetUserOutput {

    String getId();

    String getUsername();

    String getPassword();

    Instant getCreationTime();
}
