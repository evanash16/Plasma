package evan.ashley.plasma.util;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Log4j2
@AllArgsConstructor
public class JdbcUtilImpl implements JdbcUtil {

    @Override
    public <T> List<T> run(final Connection connection, final ParameterizedSqlStatement statement, final Function<ResultSet, T> resultSetTranslator) throws SQLException {
        final ResultSet resultSet = prepareStatement(connection, statement).executeQuery();
        return iterateAndMap(resultSet, resultSetTranslator);
    }

    private <T> List<T> iterateAndMap(final ResultSet resultSet, final Function<ResultSet, T> resultSetTranslator) throws SQLException {
        ImmutableList.Builder<T> resultBuilder = ImmutableList.builder();
        while (resultSet.next()) {
            resultBuilder.add(resultSetTranslator.apply(resultSet));
        }
        return resultBuilder.build();
    }

    private PreparedStatement prepareStatement(final Connection connection, final ParameterizedSqlStatement statement) throws SQLException {
        final PreparedStatement preparedStatement = connection.prepareStatement(statement.getSql());
        addParameters(preparedStatement, statement.getParameters());
        return preparedStatement;
    }

    private void addParameters(final PreparedStatement statement, final List<Object> parameters) throws SQLException {
        for (int i = 0; i < parameters.size(); i++) {
            final Object parameterOrOptional = parameters.get(i);

            // parameter indices start at 1
            final int parameterIndex = i + 1;

            final Object parameter;
            if (parameterOrOptional instanceof Optional) {
                parameter = ((Optional<?>) parameterOrOptional).orElse(null);
            } else {
                parameter = parameterOrOptional;
            }

            if (parameter == null) {
                statement.setNull(parameterIndex, Types.NULL);
            } else if (parameter instanceof String) {
                statement.setString(parameterIndex, (String) parameter);
            } else if (parameter instanceof Integer) {
                statement.setInt(parameterIndex, (Integer) parameter);
            } else if (parameter instanceof Timestamp) {
                statement.setTimestamp(parameterIndex, (Timestamp) parameter);
            } else {
                throw new UnsupportedOperationException(String.format("Cannot add parameter of type '%s'", parameter.getClass()));
            }
        }
    }
}
