SELECT *
FROM users
WHERE 1=1
  AND creation_time < ?
  AND username % ?
ORDER BY similarity(username, ?) DESC, username ASC
LIMIT ?
OFFSET ?
