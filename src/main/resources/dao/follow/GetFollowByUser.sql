SELECT *
FROM follows
WHERE 1=1
  AND follows.follower_id = ?
  AND follows.followee_id = ?
LIMIT 1;