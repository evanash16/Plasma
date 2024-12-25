package evan.ashley.plasma.dao;

import evan.ashley.plasma.model.dao.session.CreateSessionInput;
import evan.ashley.plasma.model.dao.session.CreateSessionOutput;
import evan.ashley.plasma.model.dao.session.ImmutableCreateSessionOutput;
import evan.ashley.plasma.model.db.Session;
import evan.ashley.plasma.util.JdbcUtil;
import evan.ashley.plasma.util.ParameterizedSqlStatementUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Log4j2
@AllArgsConstructor
public class SessionDaoImpl implements SessionDao {

    private final DataSource dataSource;
    private final JdbcUtil jdbcUtil;

    @Override
    public CreateSessionOutput createSession(CreateSessionInput input) {
        try (Connection connection = dataSource.getConnection()) {
            final Session session = jdbcUtil.run(
                    connection,
                    ParameterizedSqlStatementUtil.build("dao/session/CreateSession.sql", input.getUserId()),
                    Session::fromResultSet).getFirst();
            return ImmutableCreateSessionOutput.builder()
                    .id(session.getId())
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
