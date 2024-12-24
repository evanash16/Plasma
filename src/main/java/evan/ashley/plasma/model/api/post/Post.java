package evan.ashley.plasma.model.api.post;

import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.time.Instant;

@Value.Immutable
public interface Post {

    String getId();

    String getPostedById();

    Instant getCreationTime();

    Instant getLastModificationTime();

    String getTitle();

    @Nullable
    String getBody();

    static Post fromInternal(final evan.ashley.plasma.model.dao.post.Post post) {
        return ImmutablePost.builder()
                .id(post.getId())
                .postedById(post.getPostedById())
                .creationTime(post.getCreationTime())
                .lastModificationTime(post.getLastModificationTime())
                .title(post.getTitle())
                .body(post.getBody())
                .build();
    }
}
