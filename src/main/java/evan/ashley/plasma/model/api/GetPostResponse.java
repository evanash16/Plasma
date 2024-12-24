package evan.ashley.plasma.model.api;

import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.time.Instant;

@Value.Immutable
public interface GetPostResponse {

    String getId();

    String getPostedById();

    Instant getCreationTime();

    Instant getLastModificationTime();

    String getTitle();

    @Nullable
    String getBody();
}
