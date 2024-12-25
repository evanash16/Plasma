package evan.ashley.plasma.model.dao.session;

import org.immutables.value.Value;

import java.time.Instant;

@Value.Immutable
public interface GetSessionOutput {

    String getId();

    String getUserId();

    Instant getCreationTime();
}
