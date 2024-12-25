DELETE FROM follows
  WHERE 1=1
    AND follows.id = ?
    AND follows.follower_id = ?
  RETURNING id, follower_id, followee_id, creation_time;