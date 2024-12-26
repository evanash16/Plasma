package evan.ashley.plasma.model.dao.follow;

import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
public interface GetFollowInput {

    @Nullable
    String getId();

    @Nullable
    String getFollowerId();

    @Nullable
    String getFolloweeId();
}
