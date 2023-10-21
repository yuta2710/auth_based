CREATE TABLE application_user (
                          id BIGSERIAL PRIMARY KEY,
                          firstName TEXT NOT NULL,
                          lastName TEXT NOT NULL,
                          email TEXT NOT NULL,
                          password TEXT NOT NULL,
);