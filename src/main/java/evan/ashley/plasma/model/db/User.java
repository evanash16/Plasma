package evan.ashley.plasma.model.db;

import evan.ashley.plasma.model.api.InternalErrorException;
import org.immutables.value.Value;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

import static evan.ashley.plasma.constant.db.Users.Column.CREATION_TIME;
import static evan.ashley.plasma.constant.db.Users.Column.ID;
import static evan.ashley.plasma.constant.db.Users.Column.PASSWORD;
import static evan.ashley.plasma.constant.db.Users.Column.USERNAME;

@Value.Immutable
public interface User {

    String getId();

    String getUsername();

    String getPassword();

    Instant getCreationTime();

    static User fromResultSet(final ResultSet resultSet) {
        try {
            return ImmutableUser.builder()
                    .id(resultSet.getString(ID))
                    .username(resultSet.getString(USERNAME))
                    .password(resultSet.getString(PASSWORD))
                    .creationTime(resultSet.getTimestamp(CREATION_TIME).toInstant())
                    .build();
        } catch (final SQLException e) {
            throw new InternalErrorException("Could not parse user from database.", e);
        }
    }
}
