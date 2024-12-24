package evan.ashley.plasma.model.db;

import evan.ashley.plasma.model.api.InternalErrorException;
import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

import static evan.ashley.plasma.constant.db.Posts.Column.BODY;
import static evan.ashley.plasma.constant.db.Posts.Column.ID;
import static evan.ashley.plasma.constant.db.Posts.Column.CREATION_TIME;
import static evan.ashley.plasma.constant.db.Posts.Column.LAST_MODIFICATION_TIME;
import static evan.ashley.plasma.constant.db.Posts.Column.POSTED_BY_ID;
import static evan.ashley.plasma.constant.db.Posts.Column.TITLE;

@Value.Immutable
public interface Post {

    String getId();

    String getPostedById();

    Instant getCreationTime();

    Instant getLastModificationTime();

    String getTitle();

    @Nullable
    String getBody();

    static Post fromResultSet(final ResultSet resultSet) {
        try {
            return ImmutablePost.builder()
                    .id(resultSet.getString(ID))
                    .postedById(resultSet.getString(POSTED_BY_ID))
                    .creationTime(resultSet.getTimestamp(CREATION_TIME).toInstant())
                    .lastModificationTime(resultSet.getTimestamp(LAST_MODIFICATION_TIME).toInstant())
                    .title(resultSet.getString(TITLE))
                    .body(resultSet.getString(BODY))
                    .build();
        } catch (final SQLException e) {
            throw new InternalErrorException("Could not parse post from database.", e);
        }

    }
}