package evan.ashley.plasma.model.db;

import evan.ashley.plasma.model.api.InternalErrorException;
import org.immutables.value.Value;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

@Value.Immutable
public interface User {

    String getId();

    String getUsername();

    String getPassword();

    Instant getCreationTime();

    static User fromResultSet(final ResultSet resultSet) {
        try {
            return ImmutableUser.builder()
                    .id(resultSet.getString("id"))
                    .username(resultSet.getString("username"))
                    .password(resultSet.getString("password"))
                    .creationTime(resultSet.getTimestamp("creation_time").toInstant())
                    .build();
        } catch (final SQLException e) {
            throw new InternalErrorException("Could not parse user from database.", e);
        }
    }
}
