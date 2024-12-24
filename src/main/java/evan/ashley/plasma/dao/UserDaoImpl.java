package evan.ashley.plasma.dao;

import evan.ashley.plasma.model.api.InternalErrorException;
import evan.ashley.plasma.model.dao.CreateUserInput;
import evan.ashley.plasma.model.dao.CreateUserOutput;
import evan.ashley.plasma.model.dao.ImmutableCreateUserInput;
import evan.ashley.plasma.model.dao.ImmutableCreateUserOutput;
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
}
