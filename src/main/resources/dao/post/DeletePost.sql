DELETE FROM posts
  WHERE posts.id = ?
  RETURNING id, posted_by_id, creation_time, last_modification_time, title, body;
