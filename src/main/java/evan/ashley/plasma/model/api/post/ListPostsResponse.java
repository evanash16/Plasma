package evan.ashley.plasma.model.api.post;

import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.util.List;

@Value.Immutable
public interface ListPostsResponse {

    List<Post> getPosts();

    @Nullable
    String getPaginationToken();
}
