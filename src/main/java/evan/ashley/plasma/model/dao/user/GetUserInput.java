package evan.ashley.plasma.model.dao.user;

import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
public interface GetUserInput {

    @Nullable
    String getId();

    @Nullable
    String getUsername();
}
