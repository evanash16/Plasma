package evan.ashley.plasma.model.dao.post;

import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
public interface ListPostsInput {

    @Nullable
    String getFollowerId();

    @Nullable
    String getPostedById();

    @Nullable
    Integer getMaxPageSize();

    @Nullable
    PostsSortOrder getSortOrder();

    @Nullable
    String getPaginationToken();
}
