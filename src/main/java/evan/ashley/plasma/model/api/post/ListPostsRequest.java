package evan.ashley.plasma.model.api.post;

import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
public interface ListPostsRequest {

    String getPostedById();

    @Nullable
    Integer getMaxPageSize();

    @Nullable
    PostsSortOrder getSortOrder();

    @Nullable
    String getPaginationToken();
}
