package evan.ashley.plasma.dao;

import evan.ashley.plasma.constant.db.PostgreSQL;
import evan.ashley.plasma.model.api.InternalErrorException;
import evan.ashley.plasma.model.api.ResourceNotFoundException;
import evan.ashley.plasma.model.api.ValidationException;
import evan.ashley.plasma.model.dao.CreatePostInput;
import evan.ashley.plasma.model.dao.CreatePostOutput;
import evan.ashley.plasma.model.dao.GetPostInput;
import evan.ashley.plasma.model.dao.GetPostOutput;
import evan.ashley.plasma.model.dao.ImmutableCreatePostOutput;
import evan.ashley.plasma.model.dao.ImmutableGetPostOutput;
import evan.ashley.plasma.model.dao.ImmutableGetUserOutput;
import evan.ashley.plasma.model.db.Post;
import evan.ashley.plasma.model.db.User;
import evan.ashley.plasma.util.JdbcUtil;
import evan.ashley.plasma.util.ParameterizedSqlStatementUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Log4j2
@AllArgsConstructor
public class PostDaoImpl implements PostDao {

    private final DataSource dataSource;
    private final JdbcUtil jdbcUtil;

    @Override
    public CreatePostOutput createPost(CreatePostInput input) throws ValidationException {
        try (Connection connection = dataSource.getConnection()) {
            final Post post = jdbcUtil.run(
                    connection,
                    ParameterizedSqlStatementUtil.build(
                            "dao/post/CreatePost.sql",
                            input.getPostedById(),
                            input.getTitle(),
                            Optional.ofNullable(input.getBody())),
                    Post::fromResultSet).getFirst();
            return ImmutableCreatePostOutput.builder()
                    .id(post.getId())
                    .build();
        } catch (final SQLException e) {
            if (PostgreSQL.SqlState.ConstraintViolation.FOREIGN_KEY.equals(e.getSQLState())) {
                throw new ValidationException(String.format(
                        "The requested user does not exist: '%s'",
                        input.getPostedById()), e);
            }
            log.error("Something went wrong creating post", e);
            throw new InternalErrorException("Failed to create new post", e);
        }
    }

    @Override
    public GetPostOutput getPost(final GetPostInput input) throws ResourceNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            final Post post = jdbcUtil.run(
                    connection,
                    ParameterizedSqlStatementUtil.build(
                            "dao/post/GetPost.sql",
                            input.getId()),
                    Post::fromResultSet).getFirst();
            return ImmutableGetPostOutput.builder()
                    .id(post.getId())
                    .postedById(post.getPostedById())
                    .creationTime(post.getCreationTime())
                    .lastModificationTime(post.getLastModificationTime())
                    .title(post.getTitle())
                    .body(post.getBody())
                    .build();
        } catch (final SQLException e) {
            throw new InternalErrorException(String.format("Failed to retrieve user with id '%s'", input.getId()), e);
        } catch (final NoSuchElementException e) {
            throw new ResourceNotFoundException(String.format("No user found with id '%s'", input.getId()));
        }
    }
}
