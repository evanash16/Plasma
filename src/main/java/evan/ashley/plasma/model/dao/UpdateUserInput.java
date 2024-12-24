package evan.ashley.plasma.model.dao;

import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
public interface UpdateUserInput {

    String getId();

    @Nullable
    String getUsername();

    @Nullable
    String getPassword();
}
