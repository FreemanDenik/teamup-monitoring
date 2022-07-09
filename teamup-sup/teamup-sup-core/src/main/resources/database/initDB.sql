INSERT INTO INTERESTS VALUES
                          (1, 'Like to play football', 'Football'),
                          (2, 'Like to go fishing', 'Fishing');

INSERT INTO USER_ACCOUNT (ACCOUNT_ID, NAME, LAST_NAME, login, password, email, ACCOUNT_CREATED_TIME, LAST_ACCOUNT_ACTIVITY, role, age) VALUES
    (1, 'Ivan', 'Ivanov', 'user', '$2a$12$9tFfI2kJfHKJJr4xIlXqtOsRXl.XguQj9ZNdFeviSamwUaR6QtHL2', 'qursedwarrior@gmail.com', '20.10.2021 18:00', '20.10.2021 18:00', 0, 25);

INSERT INTO ADMIN_ACCOUNT (ACCOUNT_ID, NAME, LAST_NAME, login, password, email, ACCOUNT_CREATED_TIME, LAST_ACCOUNT_ACTIVITY, role) VALUES
    (2, 'Sergei', 'Sergeev', 'admin', '$2a$12$9tFfI2kJfHKJJr4xIlXqtOsRXl.XguQj9ZNdFeviSamwUaR6QtHL2', 'kagaminobasket@gmail.com', '20.10.2021 18:00', '20.10.2021 18:00', 1);

INSERT INTO MODERATOR_ACCOUNT (ACCOUNT_ID, NAME, LAST_NAME, login, password, email, ACCOUNT_CREATED_TIME, LAST_ACCOUNT_ACTIVITY, role) VALUES
    (3, 'Petr', 'Petrov', 'moder', '$2a$12$9tFfI2kJfHKJJr4xIlXqtOsRXl.XguQj9ZNdFeviSamwUaR6QtHL2', 'ckillast@gmail.com', '20.10.2021 18:00', '20.10.2021 18:00', 2);
