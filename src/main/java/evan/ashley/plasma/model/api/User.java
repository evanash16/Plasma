package evan.ashley.plasma.model.api;

import org.immutables.value.Value;

import java.time.Instant;

@Value.Immutable
public interface User {

    String getId();

    String getUsername();

    Instant getCreationTime();

    static User fromInternal(final evan.ashley.plasma.model.dao.User user) {
        return ImmutableUser.builder()
                .id(user.getId())
                .username(user.getUsername())
                .creationTime(user.getCreationTime())
                .build();
    }
}
