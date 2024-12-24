package evan.ashley.plasma.model.dao.post;

import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.time.Instant;

@Value.Immutable
public interface GetPostOutput {

    String getId();

    String getPostedById();

    Instant getCreationTime();

    Instant getLastModificationTime();

    String getTitle();

    @Nullable
    String getBody();
}
