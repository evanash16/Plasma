SELECT *
FROM users
WHERE users.username = ?
LIMIT 1;