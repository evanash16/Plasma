package evan.ashley.plasma.model.dao.post;

import evan.ashley.plasma.model.OptionalString;
import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
public interface UpdatePostInput {

    String getId();

    String getPostedById();

    @Nullable
    String getTitle();

    @Nullable
    OptionalString getBody();
}
