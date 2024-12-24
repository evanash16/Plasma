package evan.ashley.plasma.model.db;

import evan.ashley.plasma.model.api.InternalErrorException;
import org.immutables.value.Value;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

import static evan.ashley.plasma.constant.db.Follows.Column.*;

@Value.Immutable
public interface Follow {

    String getId();

    String getFollowerId();

    String getFolloweeId();

    Instant getCreationTime();

    static Follow fromResultSet(final ResultSet resultSet) {
        try {
            return ImmutableFollow.builder()
                    .id(resultSet.getString(ID))
                    .followerId(resultSet.getString(FOLLOWER_ID))
                    .followeeId(resultSet.getString(FOLLOWEE_ID))
                    .creationTime(resultSet.getTimestamp(CREATION_TIME).toInstant())
                    .build();
        } catch (final SQLException e) {
            throw new InternalErrorException("Could not parse follow from database.", e);
        }
    }
}
