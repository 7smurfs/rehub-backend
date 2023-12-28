-- Change admin roles for ReHub

----------------------------------------------------
DELETE FROM user_role
WHERE user_id = (SELECT id FROM rehub_user WHERE username = 'PQbJWZTo0xbYBf3cCJK8TQ==')
  AND role_id = (SELECT id FROM role WHERE name = 'ADMIN');

INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM rehub_user WHERE username = 'PQbJWZTo0xbYBf3cCJK8TQ=='),
        (SELECT id FROM role WHERE name = 'SUPERADMIN'));

----------------------------------------------------
DELETE FROM user_role
WHERE user_id = (SELECT id FROM rehub_user WHERE username = 'bLLxtCsLuTem7n0S3EeTNA==')
  AND role_id = (SELECT id FROM role WHERE name = 'ADMIN');

INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM rehub_user WHERE username = 'bLLxtCsLuTem7n0S3EeTNA=='),
        (SELECT id FROM role WHERE name = 'SUPERADMIN'));

----------------------------------------------------
DELETE FROM user_role
WHERE user_id = (SELECT id FROM rehub_user WHERE username = '+hxNF5knFjVZ3kEvCx7eoQ==')
  AND role_id = (SELECT id FROM role WHERE name = 'ADMIN');

INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM rehub_user WHERE username = '+hxNF5knFjVZ3kEvCx7eoQ=='),
        (SELECT id FROM role WHERE name = 'SUPERADMIN'));

----------------------------------------------------
DELETE FROM user_role
WHERE user_id = (SELECT id FROM rehub_user WHERE username = 'H7sA2Gtwrfchu59WEmvOJw==')
  AND role_id = (SELECT id FROM role WHERE name = 'ADMIN');

INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM rehub_user WHERE username = 'H7sA2Gtwrfchu59WEmvOJw=='),
        (SELECT id FROM role WHERE name = 'SUPERADMIN'));

----------------------------------------------------
DELETE FROM user_role
WHERE user_id = (SELECT id FROM rehub_user WHERE username = 'o0uhacfnVZiOCyvz/tu6GQ==')
  AND role_id = (SELECT id FROM role WHERE name = 'ADMIN');

INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM rehub_user WHERE username = 'o0uhacfnVZiOCyvz/tu6GQ=='),
        (SELECT id FROM role WHERE name = 'SUPERADMIN'));
----------------------------------------------------

DELETE FROM user_role
WHERE user_id = (SELECT id FROM rehub_user WHERE username = 'T66K6qm4Tqd8KCIrTrVP1w==')
  AND role_id = (SELECT id FROM role WHERE name = 'ADMIN');

INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM rehub_user WHERE username = 'T66K6qm4Tqd8KCIrTrVP1w=='),
        (SELECT id FROM role WHERE name = 'SUPERADMIN'));

----------------------------------------------------

DELETE FROM user_role
WHERE user_id = (SELECT id FROM rehub_user WHERE username = '9WMT4JYQfa4DkDxlxrvRnA==')
  AND role_id = (SELECT id FROM role WHERE name = 'ADMIN');

INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM rehub_user WHERE username = '9WMT4JYQfa4DkDxlxrvRnA=='),
        (SELECT id FROM role WHERE name = 'SUPERADMIN'));
