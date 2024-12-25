package evan.ashley.plasma.dao;

import evan.ashley.plasma.constant.db.PostgreSQL;
import evan.ashley.plasma.model.api.InternalErrorException;
import evan.ashley.plasma.model.api.ResourceNotFoundException;
import evan.ashley.plasma.model.api.ValidationException;
import evan.ashley.plasma.model.dao.session.CreateSessionInput;
import evan.ashley.plasma.model.dao.session.CreateSessionOutput;
import evan.ashley.plasma.model.dao.session.GetSessionInput;
import evan.ashley.plasma.model.dao.session.GetSessionOutput;
import evan.ashley.plasma.model.dao.session.ImmutableCreateSessionOutput;
import evan.ashley.plasma.model.dao.session.ImmutableGetSessionOutput;
import evan.ashley.plasma.model.db.Session;
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
public class SessionDaoImpl implements SessionDao {

    private final DataSource dataSource;
    private final JdbcUtil jdbcUtil;

    @Override
    public CreateSessionOutput createSession(final CreateSessionInput input) throws ValidationException {
        try (Connection connection = dataSource.getConnection()) {
            final Session session = jdbcUtil.run(
                    connection,
                    ParameterizedSqlStatementUtil.build("dao/session/CreateSession.sql", input.getUserId()),
                    Session::fromResultSet).getFirst();
            return ImmutableCreateSessionOutput.builder()
                    .id(session.getId())
                    .build();
        } catch (final SQLException e) {
            if (PostgreSQL.SqlState.ConstraintViolation.FOREIGN_KEY.equals(e.getSQLState())) {
                throw new ValidationException(String.format(
                        "The requested user does not exist: '%s'",
                        input.getUserId()), e);
            }
            log.error("Something went wrong creating post", e);
            throw new InternalErrorException("Failed to create new post", e);
        }
    }

    @Override
    public GetSessionOutput getSession(final GetSessionInput input) throws ResourceNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            final Session session = jdbcUtil.run(
                    connection,
                    ParameterizedSqlStatementUtil.build("dao/session/GetSession.sql", input.getId()),
                    Session::fromResultSet).getFirst();
            return ImmutableGetSessionOutput.builder()
                    .id(session.getId())
                    .userId(session.getUserId())
                    .creationTime(session.getCreationTime())
                    .build();
        } catch (final SQLException e) {
            throw new InternalErrorException(String.format("Failed to retrieve session with id '%s'", input.getId()), e);
        } catch (final NoSuchElementException e) {
            throw new ResourceNotFoundException(String.format("No session found with id '%s'", input.getId()));
        }
    }
}
