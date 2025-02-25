package evan.ashley.plasma.model.dao.user;

import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
public interface SearchUsersInput {

    String getUsernameSearchString();

    @Nullable
    Integer getMaxPageSize();

    @Nullable
    String getPaginationToken();
}
