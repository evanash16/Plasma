package evan.ashley.plasma.dao;

import evan.ashley.plasma.model.api.InternalErrorException;
import evan.ashley.plasma.model.api.ResourceNotFoundException;
import evan.ashley.plasma.model.dao.*;
import evan.ashley.plasma.model.db.User;
import evan.ashley.plasma.util.JdbcUtil;
import evan.ashley.plasma.util.ParameterizedSqlStatementUtil;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.NoSuchElementException;


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
            throw new InternalErrorException("Failed to create new user", e);
        }
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
