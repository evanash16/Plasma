package evan.ashley.plasma.model.api.post;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
@JsonDeserialize(as = ImmutableCreatePostRequest.class)
public interface CreatePostRequest {

    String getPostedById();

    String getTitle();

    @Nullable
    String getBody();
}
