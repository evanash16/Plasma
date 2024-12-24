package evan.ashley.plasma.dao;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import evan.ashley.plasma.constant.db.PostgreSQL;
import evan.ashley.plasma.model.api.InternalErrorException;
import evan.ashley.plasma.model.api.ResourceNotFoundException;
import evan.ashley.plasma.model.api.ValidationException;
import evan.ashley.plasma.model.dao.*;
import evan.ashley.plasma.model.db.User;
import evan.ashley.plasma.util.ImmutableParameterizedSqlStatement;
import evan.ashley.plasma.util.JdbcUtil;
import evan.ashley.plasma.util.ParameterizedSqlStatement;
import evan.ashley.plasma.util.ParameterizedSqlStatementUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static evan.ashley.plasma.constant.db.Users.Column.PASSWORD;
import static evan.ashley.plasma.constant.db.Users.Column.USERNAME;

@Log4j2
@AllArgsConstructor
public class UserDaoImpl implements UserDao {

    private final DataSource dataSource;
    private final JdbcUtil jdbcUtil;

    @Override
    public CreateUserOutput createUser(final CreateUserInput input) {
        try (Connection connection = dataSource.getConnection()) {
            final User user = jdbcUtil.run(
                    connection,
                    ParameterizedSqlStatementUtil.build(
                            "dao/user/CreateUser.sql",
                            input.getUsername(),
                            input.getPassword()),
                    User::fromResultSet).getFirst();
            return ImmutableCreateUserOutput.builder()
                    .id(user.getId())
                    .build();
        } catch (final SQLException e) {
            if (PostgreSQL.SqlState.ConstraintViolation.UNIQUE.equals(e.getSQLState())) {
                throw new ValidationException(String.format("A user already exists with username '%s'", input.getUsername()), e);
            }
            log.error("Something went wrong creating user", e);
            throw new InternalErrorException("Failed to create new user", e);
        }
    }

    @Override
    public void updateUser(final UpdateUserInput input) throws ResourceNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            final List<ParameterizedSqlStatement> updateExpressions = getUpdateExpressions(input);
            if (updateExpressions.isEmpty()) {
                return;
            }

            final String updateSql = updateExpressions.stream()
                    .map(ParameterizedSqlStatement::getSql)
                    .collect(Collectors.joining(", "));
            final List<Object> allParameters = ImmutableList.builder()
                    .addAll(updateExpressions.stream()
                            .map(ParameterizedSqlStatement::getParameters)
                            .flatMap(List::stream)
                            .collect(ImmutableList.toImmutableList()))
                    .add(input.getId())
                    .build();

            jdbcUtil.run(
                    connection,
                    ParameterizedSqlStatementUtil.buildFromTemplate(
                            "dao/user/UpdateUser.sql",
                            ImmutableMap.of("setExpressions", updateSql),
                            allParameters.toArray()),
                    User::fromResultSet).getFirst();
        } catch (final SQLException e) {
            if (PostgreSQL.SqlState.ConstraintViolation.UNIQUE.equals(e.getSQLState())) {
                throw new ValidationException(String.format("A user already exists with username '%s'", input.getUsername()), e);
            }
            log.error("Something went wrong updating user with id: {}", input.getId(), e);
            throw new InternalErrorException(String.format("Failed to update user with id '%s'", input.getId()), e);
        }
    }

    private List<ParameterizedSqlStatement> getUpdateExpressions(final UpdateUserInput input) {
        final ImmutableList.Builder<ParameterizedSqlStatement> updateExpressionsBuilder = ImmutableList.builder();
        if (input.getUsername() != null) {
            updateExpressionsBuilder.add(ImmutableParameterizedSqlStatement.builder()
                    .sql(String.format("%s = ?", USERNAME))
                    .addParameters(input.getUsername())
                    .build());
        }
        if (input.getPassword() != null) {
            updateExpressionsBuilder.add(ImmutableParameterizedSqlStatement.builder()
                    .sql(String.format("%s = ?", PASSWORD))
                    .addParameters(input.getPassword())
                    .build());
        }
        return updateExpressionsBuilder.build();
    }

    @Override
    public GetUserOutput getUser(final GetUserInput input) throws ResourceNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            final User user = jdbcUtil.run(
                    connection,
                    ParameterizedSqlStatementUtil.build(
                            "dao/user/GetUser.sql",
                            input.getId()),
                    User::fromResultSet).getFirst();
            return ImmutableGetUserOutput.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .creationTime(user.getCreationTime())
                    .build();
        } catch (final SQLException e) {
            throw new InternalErrorException("Failed to create new user", e);
        } catch (final NoSuchElementException e) {
            throw new ResourceNotFoundException(String.format("No user found with id '%s'", input.getId()));
        }
    }
}
