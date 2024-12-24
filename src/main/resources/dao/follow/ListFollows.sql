SELECT *
FROM follows
WHERE 1=1
  AND follower_id = ?
  AND creation_time < ?
  AND ${paginationExpression}
ORDER BY ${sortOrder}
LIMIT ?;