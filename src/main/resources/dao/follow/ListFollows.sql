SELECT *
FROM follows
WHERE 1=1
  AND follower_id = ?
  AND ${paginationExpression}
ORDER BY ${sortOrder}
LIMIT ?;