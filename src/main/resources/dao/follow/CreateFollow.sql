INSERT INTO follows (follower_id, followee_id)
  VALUES (?, ?)
  RETURNING id, follower_id, followee_id, creation_time;