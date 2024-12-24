package evan.ashley.plasma.model.dao;

import org.immutables.value.Value;

@Value.Immutable
public interface CreateFollowInput {

    String getFollowerId();

    String getFolloweeId();
}
