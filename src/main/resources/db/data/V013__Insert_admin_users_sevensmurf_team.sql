-- Insert admins users for ReHub

----------------------------------------------------

INSERT INTO rehub_user (username, password, status)
VALUES ('PQbJWZTo0xbYBf3cCJK8TQ==', '$2a$12$GB4VUSd8qosPkfzvvr68H.QwmC3vmCF7EPyFToJKES9rfJIqYUDd2',
        'ACTIVE');

INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM rehub_user WHERE username = 'PQbJWZTo0xbYBf3cCJK8TQ=='),
        (SELECT id FROM role WHERE name = 'ADMIN'));

----------------------------------------------------

INSERT INTO rehub_user (username, password, status)
VALUES ('bLLxtCsLuTem7n0S3EeTNA==', '$2a$12$L8BVVP4qdr1aopQSOaN/3./c/bV3XRslcYPzxNNcT7YfHdUsaNw2y',
        'ACTIVE');

INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM rehub_user WHERE username = 'bLLxtCsLuTem7n0S3EeTNA=='),
        (SELECT id FROM role WHERE name = 'ADMIN'));

----------------------------------------------------

INSERT INTO rehub_user (username, password, status)
VALUES ('+hxNF5knFjVZ3kEvCx7eoQ==', '$2a$12$PZpJpepbGV0udCPOcW1wEu4tkZCHQk8FxdIR3DZof1dlGabL.zKzS',
        'ACTIVE');

INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM rehub_user WHERE username = '+hxNF5knFjVZ3kEvCx7eoQ=='),
        (SELECT id FROM role WHERE name = 'ADMIN'));

----------------------------------------------------

INSERT INTO rehub_user (username, password, status)
VALUES ('H7sA2Gtwrfchu59WEmvOJw==',
        '$2a$12$xVqBwKUzSQC/wPS36IzxNOyzitdrPVgvj98pFduENd2bLUxAc/IVq', 'ACTIVE');

INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM rehub_user WHERE username = 'H7sA2Gtwrfchu59WEmvOJw=='),
        (SELECT id FROM role WHERE name = 'ADMIN'));

----------------------------------------------------

INSERT INTO rehub_user (username, password, status)
VALUES ('o0uhacfnVZiOCyvz/tu6GQ==', '$2a$12$8GkROzZS2yn1RyrWRyNP7eZVRSxb0v5YpLlj6RJyUxgmISaO6H9D2',
        'ACTIVE');

INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM rehub_user WHERE username = 'o0uhacfnVZiOCyvz/tu6GQ=='),
        (SELECT id FROM role WHERE name = 'ADMIN'));
----------------------------------------------------

INSERT INTO rehub_user (username, password, status)
VALUES ('T66K6qm4Tqd8KCIrTrVP1w==', '$2a$12$dJhiAIMv45MRGsCU4VW7veuplRDWxbEd14yPbd2yLsYl3zVAWrd96',
        'ACTIVE');

INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM rehub_user WHERE username = 'T66K6qm4Tqd8KCIrTrVP1w=='),
        (SELECT id FROM role WHERE name = 'ADMIN'));

----------------------------------------------------

INSERT INTO rehub_user (username, password, status)
VALUES ('9WMT4JYQfa4DkDxlxrvRnA==', '$2a$12$PZpJpepbGV0udCPOcW1wEu4tkZCHQk8FxdIR3DZof1dlGabL.zKzS',
        'ACTIVE');

INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM rehub_user WHERE username = '9WMT4JYQfa4DkDxlxrvRnA=='),
        (SELECT id FROM role WHERE name = 'ADMIN'));
