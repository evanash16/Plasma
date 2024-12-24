package evan.ashley.plasma.model.dao.follow;

import org.immutables.value.Value;

import java.time.Instant;

@Value.Immutable
public interface Follow {

    String getId();

    String getFollowerId();

    String getFolloweeId();

    Instant getCreationTime();

    static Follow fromInternal(final evan.ashley.plasma.model.db.Follow follow) {
        return ImmutableFollow.builder()
                .id(follow.getId())
                .followerId(follow.getFollowerId())
                .followeeId(follow.getFolloweeId())
                .creationTime(follow.getCreationTime())
                .build();
    }
}
