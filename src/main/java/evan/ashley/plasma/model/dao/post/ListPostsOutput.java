package evan.ashley.plasma.model.dao.post;

import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.util.List;

@Value.Immutable
public interface ListPostsOutput {

    List<Post> getPosts();

    @Nullable
    String getPaginationToken();
}
