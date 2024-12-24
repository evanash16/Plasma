package evan.ashley.plasma.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ParameterizedSqlStatementUtil {

    public static ParameterizedSqlStatement build(final String path, Object... parameters) {
        try (InputStream inputStream = ParameterizedSqlStatementUtil.class.getClassLoader().getResourceAsStream(path)) {
            return ImmutableParameterizedSqlStatement.builder()
                    .sql(new String(Objects.requireNonNull(inputStream).readAllBytes(), StandardCharsets.UTF_8))
                    .addParameters(parameters)
                    .build();
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
