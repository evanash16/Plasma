DELETE FROM follows
  WHERE 1=1
    AND follows.follower_id = ?
    AND follows.followee_id = ?
  RETURNING id, follower_id, followee_id, creation_time;