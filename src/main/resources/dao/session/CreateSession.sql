INSERT INTO sessions (user_id)
  VALUES (?)
  RETURNING id, user_id, creation_time;