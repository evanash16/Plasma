DELETE FROM follows
  WHERE follows.id = ?
  RETURNING id, follower_id, followee_id, creation_time;