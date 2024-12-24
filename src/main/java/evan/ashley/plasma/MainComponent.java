package evan.ashley.plasma;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import evan.ashley.plasma.dao.FollowDao;
import evan.ashley.plasma.dao.FollowDaoImpl;
import evan.ashley.plasma.dao.UserDao;
import evan.ashley.plasma.dao.UserDaoImpl;
import evan.ashley.plasma.util.JdbcUtil;
import evan.ashley.plasma.util.JdbcUtilImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

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
    public UserDao getUserDao(final DataSource dataSource, final JdbcUtil jdbcUtil) {
        return new UserDaoImpl(dataSource, jdbcUtil);
    }

    @Bean
    public FollowDao getFollowDao(final DataSource dataSource, final JdbcUtil jdbcUtil) {
        return new FollowDaoImpl(dataSource, jdbcUtil);
    }
}
