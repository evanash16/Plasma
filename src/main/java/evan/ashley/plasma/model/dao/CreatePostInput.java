package evan.ashley.plasma.model.dao;

import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
public interface CreatePostInput {

    String getPostedById();

    String getTitle();

    @Nullable
    String getBody();
}
