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
import java.util.List;
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
            final Object parameter = parameters.get(i);
            final int parameterIndex = i + 1;
            if (parameter instanceof String) {
                // parameter indices start at 1
                statement.setString(parameterIndex, (String) parameter);
            } else {
                throw new UnsupportedOperationException(String.format("Cannot add parameter of type '%s'", parameter.getClass()));
            }
        }
    }
}
