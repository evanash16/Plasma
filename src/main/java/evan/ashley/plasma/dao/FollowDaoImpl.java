package evan.ashley.plasma.dao;

import evan.ashley.plasma.constant.db.PostgreSQL;
import evan.ashley.plasma.model.dao.DeleteFollowInput;
import evan.ashley.plasma.model.api.InternalErrorException;
import evan.ashley.plasma.model.api.ResourceNotFoundException;
import evan.ashley.plasma.model.api.ValidationException;
import evan.ashley.plasma.model.dao.CreateFollowInput;
import evan.ashley.plasma.model.dao.CreateFollowOutput;
import evan.ashley.plasma.model.dao.ImmutableCreateFollowOutput;
import evan.ashley.plasma.model.db.Follow;
import evan.ashley.plasma.util.JdbcUtil;
import evan.ashley.plasma.util.ParameterizedSqlStatementUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.NoSuchElementException;

@Log4j2
@AllArgsConstructor
public class FollowDaoImpl implements FollowDao {

    private final DataSource dataSource;
    private final JdbcUtil jdbcUtil;

    @Override
    public CreateFollowOutput createFollow(final CreateFollowInput input) throws ValidationException {
        try (Connection connection = dataSource.getConnection()) {
            final Follow follow = jdbcUtil.run(connection, ParameterizedSqlStatementUtil.build(
                    "dao/follow/CreateFollow.sql",
                    input.getFollowerId(),
                    input.getFolloweeId()),
                    Follow::fromResultSet).getFirst();
            return ImmutableCreateFollowOutput.builder()
                    .id(follow.getId())
                    .build();
        } catch (final SQLException e) {
            if (PostgreSQL.SqlState.ConstraintViolation.UNIQUE.equals(e.getSQLState())) {
                throw new ValidationException(String.format(
                        "User '%s' is already following '%s'",
                        input.getFollowerId(),
                        input.getFolloweeId()), e);
            } else if (PostgreSQL.SqlState.ConstraintViolation.FOREIGN_KEY.equals(e.getSQLState())) {
                throw new ValidationException(String.format(
                        "At least one of the requested users do not exist: '%s', '%s'",
                        input.getFollowerId(),
                        input.getFolloweeId()), e);
            }
            log.error(
                    "Something went wrong configuring user '{}' to follow '{}'",
                    input.getFollowerId(),
                    input.getFolloweeId(), e);
            throw new InternalErrorException(String.format(
                    "Failed to configure user '%s' to follow '%s'",
                    input.getFollowerId(),
                    input.getFolloweeId()), e);
        }
    }

    @Override
    public void deleteFollow(final DeleteFollowInput input) throws ResourceNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            final Follow ignored = jdbcUtil.run(connection, ParameterizedSqlStatementUtil.build(
                            "dao/follow/DeleteFollow.sql",
                            input.getId()),
                    Follow::fromResultSet).getFirst();
        } catch (final NoSuchElementException e) {
            throw new ResourceNotFoundException(String.format("No follow found with id '%s'", input.getId()));
        } catch (final SQLException e) {
            log.error(
                    "Something went wrong deleting follow '{}'",
                    input.getId(), e);
            throw new InternalErrorException(String.format(
                    "Failed to delete follow '%s'",
                    input.getId()), e);
        }
    }
}
