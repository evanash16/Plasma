package evan.ashley.plasma.model.dao.follow;

import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
public interface ListFollowsInput {

    String getFollowerId();

    @Nullable
    FollowsSortOrder getSortOrder();

    @Nullable
    String getPaginationToken();

    @Nullable
    Integer getMaxPageSize();
}
