package evan.ashley.plasma;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import evan.ashley.plasma.dao.FollowDao;
import evan.ashley.plasma.dao.FollowDaoImpl;
import evan.ashley.plasma.dao.PostDao;
import evan.ashley.plasma.dao.PostDaoImpl;
import evan.ashley.plasma.dao.UserDao;
import evan.ashley.plasma.dao.UserDaoImpl;
import evan.ashley.plasma.model.dao.FollowsPaginationToken;
import evan.ashley.plasma.model.dao.UsersPaginationToken;
import evan.ashley.plasma.translator.TokenTranslator;
import evan.ashley.plasma.translator.TokenTranslatorImpl;
import evan.ashley.plasma.util.JdbcUtil;
import evan.ashley.plasma.util.JdbcUtilImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Base64;

@Component
public class MainComponent {

    @Bean
    public JdbcUtil getJdbcUtil() {
        return new JdbcUtilImpl();
    }

    @Bean
    public DataSource getComboPooledDataSource() {
        final ComboPooledDataSource dataSource = new ComboPooledDataSource();

        final Integer port = 5432;
        final String host = "localhost";
        final String database = "plasma";
        dataSource.setJdbcUrl(String.format("jdbc:postgresql://%s:%d/%s", host, port, database));
        dataSource.setUser(System.getenv("DATABASE_USERNAME"));
        dataSource.setPassword(System.getenv("DATABASE_PASSWORD"));

        dataSource.setMaxPoolSize(5);

        return dataSource;
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper()
                .findAndRegisterModules();
    }

    @Bean
    public TokenTranslator<UsersPaginationToken> getUsersPaginationToken(final ObjectMapper objectMapper) {
        final TypeReference<UsersPaginationToken> typeReference = new TypeReference<UsersPaginationToken>() {
        };
        return new TokenTranslatorImpl<>(objectMapper, typeReference, Base64.getEncoder(), Base64.getDecoder());
    }

    @Bean
    public UserDao getUserDao(
            final DataSource dataSource,
            final JdbcUtil jdbcUtil,
            final TokenTranslator<UsersPaginationToken> tokenTranslator) {
        return new UserDaoImpl(dataSource, jdbcUtil, tokenTranslator);
    }

    @Bean
    public TokenTranslator<FollowsPaginationToken> getFollowsTokenTranslator(final ObjectMapper objectMapper) {
        final TypeReference<FollowsPaginationToken> typeReference = new TypeReference<FollowsPaginationToken>() {
        };
        return new TokenTranslatorImpl<>(objectMapper, typeReference, Base64.getEncoder(), Base64.getDecoder());
    }

    @Bean
    public FollowDao getFollowDao(
            final DataSource dataSource,
            final JdbcUtil jdbcUtil,
            final TokenTranslator<FollowsPaginationToken> tokenTranslator) {
        return new FollowDaoImpl(dataSource, jdbcUtil, tokenTranslator);
    }

    @Bean
    public PostDao getPostDao(
            final DataSource dataSource,
            final JdbcUtil jdbcUtil) {
        return new PostDaoImpl(dataSource, jdbcUtil);
    }
}
