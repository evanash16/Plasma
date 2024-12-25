DELETE FROM posts
  WHERE 1=1
    AND posts.id = ?
    AND posts.posted_by_id = ?
  RETURNING id, posted_by_id, creation_time, last_modification_time, title, body;
