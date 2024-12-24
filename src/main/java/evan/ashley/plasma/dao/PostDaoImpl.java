package evan.ashley.plasma.dao;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import evan.ashley.plasma.constant.db.PostgreSQL;
import evan.ashley.plasma.model.api.InternalErrorException;
import evan.ashley.plasma.model.api.ResourceNotFoundException;
import evan.ashley.plasma.model.api.ValidationException;
import evan.ashley.plasma.model.OptionalString;
import evan.ashley.plasma.model.dao.post.CreatePostInput;
import evan.ashley.plasma.model.dao.post.CreatePostOutput;
import evan.ashley.plasma.model.dao.post.DeletePostInput;
import evan.ashley.plasma.model.dao.post.GetPostInput;
import evan.ashley.plasma.model.dao.post.GetPostOutput;
import evan.ashley.plasma.model.dao.post.ImmutableCreatePostOutput;
import evan.ashley.plasma.model.dao.post.ImmutableGetPostOutput;
import evan.ashley.plasma.model.dao.post.UpdatePostInput;
import evan.ashley.plasma.model.db.Post;
import evan.ashley.plasma.model.db.User;
import evan.ashley.plasma.util.ImmutableParameterizedSqlStatement;
import evan.ashley.plasma.util.JdbcUtil;
import evan.ashley.plasma.util.OptionalUtil;
import evan.ashley.plasma.util.ParameterizedSqlStatement;
import evan.ashley.plasma.util.ParameterizedSqlStatementUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static evan.ashley.plasma.constant.db.Posts.Column.BODY;
import static evan.ashley.plasma.constant.db.Posts.Column.TITLE;

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
    public void updatePost(final UpdatePostInput input) throws ResourceNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            final List<ParameterizedSqlStatement> updateExpressions = getUpdateExpressions(input);
            if (updateExpressions.isEmpty()) {
                return;
            }

            final String updateSql = updateExpressions.stream()
                    .map(ParameterizedSqlStatement::getSql)
                    .collect(Collectors.joining(", "));
            final List<Object> allParameters = ImmutableList.builder()
                    .addAll(updateExpressions.stream()
                            .map(ParameterizedSqlStatement::getParameters)
                            .flatMap(List::stream)
                            .collect(ImmutableList.toImmutableList()))
                    .add(input.getId())
                    .build();

            jdbcUtil.run(
                    connection,
                    ParameterizedSqlStatementUtil.buildFromTemplate(
                            "dao/post/UpdatePost.sql",
                            ImmutableMap.of("setExpressions", updateSql),
                            allParameters.toArray()),
                    Post::fromResultSet).getFirst();
        } catch (final NoSuchElementException e) {
            throw new ResourceNotFoundException(String.format("No post found with id '%s'", input.getId()));
        } catch (final SQLException e) {
            log.error("Something went wrong updating post with id: {}", input.getId(), e);
            throw new InternalErrorException(String.format("Failed to update post with id '%s'", input.getId()), e);
        }
    }

    private List<ParameterizedSqlStatement> getUpdateExpressions(final UpdatePostInput input) {
        final ImmutableList.Builder<ParameterizedSqlStatement> updateExpressionsBuilder = ImmutableList.builder();
        if (input.getTitle() != null) {
            updateExpressionsBuilder.add(ImmutableParameterizedSqlStatement.builder()
                    .sql(String.format("%s = ?", TITLE))
                    .addParameters(input.getTitle())
                    .build());
        }
        if (input.getBody() != null) {
            updateExpressionsBuilder.add(ImmutableParameterizedSqlStatement.builder()
                    .sql(String.format("%s = ?", BODY))
                    .addParameters(Optional.ofNullable(OptionalUtil.resolveOptionalValue(
                            input.getBody(),
                            OptionalString::isRemove,
                            OptionalString::getValue)))
                    .build());
        }
        return updateExpressionsBuilder.build();
    }

    @Override
    public void deletePost(final DeletePostInput input) throws ResourceNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            final Post ignored = jdbcUtil.run(connection, ParameterizedSqlStatementUtil.build(
                            "dao/post/DeletePost.sql",
                            input.getId()),
                    Post::fromResultSet).getFirst();
        } catch (final NoSuchElementException e) {
            throw new ResourceNotFoundException(String.format("No post found with id '%s'", input.getId()));
        } catch (final SQLException e) {
            log.error(
                    "Something went wrong deleting post '{}'",
                    input.getId(), e);
            throw new InternalErrorException(String.format(
                    "Failed to delete post '%s'",
                    input.getId()), e);
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
            throw new InternalErrorException(String.format("Failed to retrieve post with id '%s'", input.getId()), e);
        } catch (final NoSuchElementException e) {
            throw new ResourceNotFoundException(String.format("No post found with id '%s'", input.getId()));
        }
    }
}
