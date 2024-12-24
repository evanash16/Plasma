INSERT INTO users (username, password)
  VALUES (?, ?)
  RETURNING id, username, password, creation_time;