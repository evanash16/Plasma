package evan.ashley.plasma.model.api.user;

import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
public interface SearchUsersRequest {

    String getUsernameSearchString();

    @Nullable
    Integer getMaxPageSize();

    @Nullable
    String getPaginationToken();
}
