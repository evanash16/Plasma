SELECT *
FROM posts
WHERE 1=1
  AND posted_by_id = ?
  AND creation_time < ?
  AND ${paginationExpression}
ORDER BY ${sortOrder}
LIMIT ?;