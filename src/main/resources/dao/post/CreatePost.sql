INSERT INTO posts (posted_by_id, title, body)
  VALUES (?, ?, ?)
  RETURNING id, posted_by_id, creation_time, last_modification_time, title, body;
