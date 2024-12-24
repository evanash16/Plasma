package evan.ashley.plasma.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;

public interface JdbcUtil {

    <T> List<T> run(
            Connection connection,
            ParameterizedSqlStatement statement,
            Function<ResultSet, T> resultSetTranslator) throws SQLException;
}
