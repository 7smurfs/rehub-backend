-- Insert admins users for ReHub

----------------------------------------------------

INSERT INTO rehub_user (username, password)
VALUES ('jb53089@fer.hr', '$2a$12$GB4VUSd8qosPkfzvvr68H.QwmC3vmCF7EPyFToJKES9rfJIqYUDd2');

INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM rehub_user WHERE username = 'jb53089@fer.hr'),
        (SELECT id FROM role WHERE name = 'ADMIN'));

----------------------------------------------------

INSERT INTO rehub_user (username, password)
VALUES ('rk54056@fer.hr', '$2a$12$L8BVVP4qdr1aopQSOaN/3./c/bV3XRslcYPzxNNcT7YfHdUsaNw2y');

INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM rehub_user WHERE username = 'rk54056@fer.hr'),
        (SELECT id FROM role WHERE name = 'ADMIN'));

----------------------------------------------------

INSERT INTO rehub_user (username, password)
VALUES ('dk53948@fer.hr', '$2a$12$PZpJpepbGV0udCPOcW1wEu4tkZCHQk8FxdIR3DZof1dlGabL.zKzS');

INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM rehub_user WHERE username = 'dk53948@fer.hr'),
        (SELECT id FROM role WHERE name = 'ADMIN'));

----------------------------------------------------

INSERT INTO rehub_user (username, password)
VALUES ('km54317@fer.hr', '$2a$12$xVqBwKUzSQC/wPS36IzxNOyzitdrPVgvj98pFduENd2bLUxAc/IVq');

INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM rehub_user WHERE username = 'km54317@fer.hr'),
        (SELECT id FROM role WHERE name = 'ADMIN'));

----------------------------------------------------

INSERT INTO rehub_user (username, password)
VALUES ('mm54325@fer.hr', '$2a$12$8GkROzZS2yn1RyrWRyNP7eZVRSxb0v5YpLlj6RJyUxgmISaO6H9D2');

INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM rehub_user WHERE username = 'mm54325@fer.hr'),
        (SELECT id FROM role WHERE name = 'ADMIN'));
----------------------------------------------------

INSERT INTO rehub_user (username, password)
VALUES ('eg53773@fer.hr', '$2a$12$dJhiAIMv45MRGsCU4VW7veuplRDWxbEd14yPbd2yLsYl3zVAWrd96');

INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM rehub_user WHERE username = 'eg53773@fer.hr'),
        (SELECT id FROM role WHERE name = 'ADMIN'));

----------------------------------------------------

INSERT INTO rehub_user (username, password)
VALUES ('av54189@fer.hr', '$2a$12$PZpJpepbGV0udCPOcW1wEu4tkZCHQk8FxdIR3DZof1dlGabL.zKzS');

INSERT INTO user_role (user_id, role_id)
VALUES ((SELECT id FROM rehub_user WHERE username = 'av54189@fer.hr'),
        (SELECT id FROM role WHERE name = 'ADMIN'));
