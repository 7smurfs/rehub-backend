-- Insert admins users for ReHub

----------------------------------------------------

INSERT INTO rehub_user (username, password, status)
VALUES ('8701840cacf67e48bcaa4f93ec96f019e99558332e39e0ca921f216df8788ccf', '$2a$12$GB4VUSd8qosPkfzvvr68H.QwmC3vmCF7EPyFToJKES9rfJIqYUDd2',
        'ACTIVE');

INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM rehub_user WHERE username = '8701840cacf67e48bcaa4f93ec96f019e99558332e39e0ca921f216df8788ccf'),
        (SELECT id FROM role WHERE name = 'ADMIN'));

----------------------------------------------------

INSERT INTO rehub_user (username, password, status)
VALUES ('d6d6a14b612d8e4b69dc7e777f7baacc3a5e546c183b436b51296d61b10c6c65', '$2a$12$L8BVVP4qdr1aopQSOaN/3./c/bV3XRslcYPzxNNcT7YfHdUsaNw2y',
        'ACTIVE');

INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM rehub_user WHERE username = 'd6d6a14b612d8e4b69dc7e777f7baacc3a5e546c183b436b51296d61b10c6c65'),
        (SELECT id FROM role WHERE name = 'ADMIN'));

----------------------------------------------------

INSERT INTO rehub_user (username, password, status)
VALUES ('e25168d1bdfbb2c044cece04bdeeda7eeadf6e8581369e915dfe694b15951cd0', '$2a$12$PZpJpepbGV0udCPOcW1wEu4tkZCHQk8FxdIR3DZof1dlGabL.zKzS',
        'ACTIVE');

INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM rehub_user WHERE username = 'e25168d1bdfbb2c044cece04bdeeda7eeadf6e8581369e915dfe694b15951cd0'),
        (SELECT id FROM role WHERE name = 'ADMIN'));

----------------------------------------------------

INSERT INTO rehub_user (username, password, status)
VALUES ('762e6cf2f1109ca35ba6d72ff472681578e88ece64695308a6fc146dbd8d2cd0',
        '$2a$12$xVqBwKUzSQC/wPS36IzxNOyzitdrPVgvj98pFduENd2bLUxAc/IVq', 'ACTIVE');

INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM rehub_user WHERE username = '762e6cf2f1109ca35ba6d72ff472681578e88ece64695308a6fc146dbd8d2cd0'),
        (SELECT id FROM role WHERE name = 'ADMIN'));

----------------------------------------------------

INSERT INTO rehub_user (username, password, status)
VALUES ('3093c921fc6b6e99c956ee4011d2618dd36a4885b523af53ef950dc9049e972a', '$2a$12$8GkROzZS2yn1RyrWRyNP7eZVRSxb0v5YpLlj6RJyUxgmISaO6H9D2',
        'ACTIVE');

INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM rehub_user WHERE username = '3093c921fc6b6e99c956ee4011d2618dd36a4885b523af53ef950dc9049e972a'),
        (SELECT id FROM role WHERE name = 'ADMIN'));
----------------------------------------------------

INSERT INTO rehub_user (username, password, status)
VALUES ('cb64fa7c0f30915e68b74ecd95ef5a2debeec8eb2fbf6913122f0df5a5498b9f', '$2a$12$dJhiAIMv45MRGsCU4VW7veuplRDWxbEd14yPbd2yLsYl3zVAWrd96',
        'ACTIVE');

INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM rehub_user WHERE username = 'cb64fa7c0f30915e68b74ecd95ef5a2debeec8eb2fbf6913122f0df5a5498b9f'),
        (SELECT id FROM role WHERE name = 'ADMIN'));

----------------------------------------------------

INSERT INTO rehub_user (username, password, status)
VALUES ('c855d0e61a9ee2787f40907b7a6abacf5b3fd87c4073c11b9ba47042659fb5a2', '$2a$12$PZpJpepbGV0udCPOcW1wEu4tkZCHQk8FxdIR3DZof1dlGabL.zKzS',
        'ACTIVE');

INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM rehub_user WHERE username = 'c855d0e61a9ee2787f40907b7a6abacf5b3fd87c4073c11b9ba47042659fb5a2'),
        (SELECT id FROM role WHERE name = 'ADMIN'));
