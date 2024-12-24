package evan.ashley.plasma.dao;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import evan.ashley.plasma.constant.db.Follows;
import evan.ashley.plasma.constant.db.PostgreSQL;
import evan.ashley.plasma.model.dao.*;
import evan.ashley.plasma.model.api.InternalErrorException;
import evan.ashley.plasma.model.api.ResourceNotFoundException;
import evan.ashley.plasma.model.api.ValidationException;
import evan.ashley.plasma.model.db.Follow;
import evan.ashley.plasma.translator.TokenTranslator;
import evan.ashley.plasma.util.JdbcUtil;
import evan.ashley.plasma.util.ParameterizedSqlStatementUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.sql.DataSource;
import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Log4j2
@AllArgsConstructor
public class FollowDaoImpl implements FollowDao {

    private static final int DEFAULT_FOLLOWS_LIMIT = 100;

    private final DataSource dataSource;
    private final JdbcUtil jdbcUtil;
    private final TokenTranslator<FollowsPaginationToken> tokenTranslator;

    @Override
    public CreateFollowOutput createFollow(final CreateFollowInput input) throws ValidationException {
        try (Connection connection = dataSource.getConnection()) {
            final Follow follow = jdbcUtil.run(connection, ParameterizedSqlStatementUtil.build(
                    "dao/follow/CreateFollow.sql",
                    input.getFollowerId(),
                    input.getFolloweeId()),
                    Follow::fromResultSet).getFirst();
            return ImmutableCreateFollowOutput.builder()
                    .id(follow.getId())
                    .build();
        } catch (final SQLException e) {
            if (PostgreSQL.SqlState.ConstraintViolation.UNIQUE.equals(e.getSQLState())) {
                throw new ValidationException(String.format(
                        "User '%s' is already following '%s'",
                        input.getFollowerId(),
                        input.getFolloweeId()), e);
            } else if (PostgreSQL.SqlState.ConstraintViolation.FOREIGN_KEY.equals(e.getSQLState())) {
                throw new ValidationException(String.format(
                        "At least one of the requested users do not exist: '%s', '%s'",
                        input.getFollowerId(),
                        input.getFolloweeId()), e);
            }
            log.error(
                    "Something went wrong configuring user '{}' to follow '{}'",
                    input.getFollowerId(),
                    input.getFolloweeId(), e);
            throw new InternalErrorException(String.format(
                    "Failed to configure user '%s' to follow '%s'",
                    input.getFollowerId(),
                    input.getFolloweeId()), e);
        }
    }

    @Override
    public void deleteFollow(final DeleteFollowInput input) throws ResourceNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            final Follow ignored = jdbcUtil.run(connection, ParameterizedSqlStatementUtil.build(
                            "dao/follow/DeleteFollow.sql",
                            input.getId()),
                    Follow::fromResultSet).getFirst();
        } catch (final NoSuchElementException e) {
            throw new ResourceNotFoundException(String.format("No follow found with id '%s'", input.getId()));
        } catch (final SQLException e) {
            log.error(
                    "Something went wrong deleting follow '{}'",
                    input.getId(), e);
            throw new InternalErrorException(String.format(
                    "Failed to delete follow '%s'",
                    input.getId()), e);
        }
    }

    @Override
    public GetFollowOutput getFollow(final GetFollowInput input) throws ResourceNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            final Follow follow = jdbcUtil.run(
                    connection,
                    ParameterizedSqlStatementUtil.build(
                            "dao/follow/GetFollow.sql",
                            input.getId()),
                    Follow::fromResultSet).getFirst();
            return ImmutableGetFollowOutput.builder()
                    .id(follow.getId())
                    .followerId(follow.getFollowerId())
                    .followeeId(follow.getFolloweeId())
                    .creationTime(follow.getCreationTime())
                    .build();
        } catch (final SQLException e) {
            throw new InternalErrorException(String.format("Failed to retrieve follow with id '%s'", input.getId()), e);
        } catch (final NoSuchElementException e) {
            throw new ResourceNotFoundException(String.format("No follow found with id '%s'", input.getId()));
        }
    }

    @Override
    public ListFollowsOutput listFollows(final ListFollowsInput input) {
        try (Connection connection = dataSource.getConnection()) {
            final FollowsPaginationToken paginationToken = tokenTranslator.decode(input.getPaginationToken());
            final FollowsSortOrder sortOrder = Optional.ofNullable(input.getSortOrder())
                    .orElse(FollowsSortOrder.CREATION_TIME_DESCENDING);

            final Instant defaultPaginationConstraint = switch (sortOrder) {
                case FollowsSortOrder.CREATION_TIME_ASCENDING -> Instant.EPOCH;
                case FollowsSortOrder.CREATION_TIME_DESCENDING -> Instant.now();
            };
            final Instant paginationConstraint = Optional.ofNullable(paginationToken)
                    .map(FollowsPaginationToken::getLastCreationTime)
                    .orElse(defaultPaginationConstraint);

            final String paginationExpression = switch (sortOrder) {
                case FollowsSortOrder.CREATION_TIME_ASCENDING -> String.format("%s > ?", Follows.Column.CREATION_TIME);
                case FollowsSortOrder.CREATION_TIME_DESCENDING -> String.format("%s < ?", Follows.Column.CREATION_TIME);
            };

            final String sortOrderExpression = switch (sortOrder) {
                case FollowsSortOrder.CREATION_TIME_ASCENDING -> String.format("%s ASC", Follows.Column.CREATION_TIME);
                case FollowsSortOrder.CREATION_TIME_DESCENDING -> String.format("%s DESC", Follows.Column.CREATION_TIME);
            };

            final int maxPageSize = Optional.ofNullable(input.getMaxPageSize())
                    .orElse(DEFAULT_FOLLOWS_LIMIT);

            final List<Follow> follows = jdbcUtil.run(
                    connection,
                    ParameterizedSqlStatementUtil.buildFromTemplate(
                            "dao/follow/ListFollows.sql",
                            ImmutableMap.of(
                                    "paginationExpression", paginationExpression,
                                    "sortOrder", sortOrderExpression),
                            input.getFollowerId(),
                            Timestamp.from(paginationConstraint),
                            maxPageSize),
                    Follow::fromResultSet);
            final List<evan.ashley.plasma.model.dao.Follow> externalFollows = follows.stream()
                    .map(evan.ashley.plasma.model.dao.Follow::fromInternal)
                    .collect(ImmutableList.toImmutableList());

            final ImmutableListFollowsOutput.Builder outputBuilder = ImmutableListFollowsOutput.builder()
                    .follows(externalFollows);
            if (follows.isEmpty()) {
                return outputBuilder.build();
            }

            final FollowsPaginationToken newPaginationToken = ImmutableFollowsPaginationToken.builder()
                    .lastCreationTime(follows.getLast().getCreationTime())
                    .build();
            return outputBuilder
                    .paginationToken(tokenTranslator.encode(newPaginationToken))
                    .build();
        } catch (final SQLException e) {
            throw new InternalErrorException(String.format(
                    "Failed to retrieve follows for follower id '%s'",
                    input.getFollowerId()), e);
        }
    }
}
