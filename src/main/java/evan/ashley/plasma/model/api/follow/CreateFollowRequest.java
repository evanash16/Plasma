package evan.ashley.plasma.model.api.follow;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import evan.ashley.plasma.model.api.ImmutableCreateFollowRequest;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableCreateFollowRequest.class)
public interface CreateFollowRequest {

    String getFollowerId();

    String getFolloweeId();
}
