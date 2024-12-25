package evan.ashley.plasma.model.db;

import evan.ashley.plasma.model.api.InternalErrorException;
import org.immutables.value.Value;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

import static evan.ashley.plasma.constant.db.Sessions.Column.CREATION_TIME;
import static evan.ashley.plasma.constant.db.Sessions.Column.ID;
import static evan.ashley.plasma.constant.db.Sessions.Column.USER_ID;

@Value.Immutable
public interface Session {

    String getId();

    String getUserId();

    Instant getCreationTime();

    static Session fromResultSet(final ResultSet resultSet) {
        try {
            return ImmutableSession.builder()
                    .id(resultSet.getString(ID))
                    .userId(resultSet.getString(USER_ID))
                    .creationTime(resultSet.getTimestamp(CREATION_TIME).toInstant())
                    .build();
        } catch (final SQLException e) {
            throw new InternalErrorException("Failed to parse session from database", e);
        }
    }
}
