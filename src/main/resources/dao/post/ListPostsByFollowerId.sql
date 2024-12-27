SELECT
  posts.*
FROM follows
JOIN posts
  ON follows.followee_id = posts.posted_by_id
WHERE 1=1
  AND follows.follower_id = ?
  AND posts.creation_time < ?
  AND ${paginationExpression}
ORDER BY ${sortOrder}
LIMIT ?;