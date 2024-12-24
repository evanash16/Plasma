UPDATE users SET
  ${setExpressions}
  WHERE users.id = ?
  RETURNING id, username, password, creation_time;