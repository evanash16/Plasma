package evan.ashley.plasma.model.api.post;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
@JsonDeserialize(as = ImmutableListPostsRequest.class)
public interface ListPostsRequest {

    String getPostedById();

    @Nullable
    Integer getMaxPageSize();

    @Nullable
    PostsSortOrder getSortOrder();

    @Nullable
    String getPaginationToken();
}
