ALTER TABLE "user" ALTER COLUMN id RESTART WITH 1;
ALTER TABLE resume ALTER COLUMN id RESTART WITH 1;
ALTER TABLE resume_file ALTER COLUMN id RESTART WITH 1;
ALTER TABLE education ALTER COLUMN id RESTART WITH 1;
ALTER TABLE experience ALTER COLUMN id RESTART WITH 1;
ALTER TABLE skill ALTER COLUMN id RESTART WITH 1;
ALTER TABLE company ALTER COLUMN id RESTART WITH 1;
ALTER TABLE joboffers ALTER COLUMN id RESTART WITH 1;
ALTER TABLE applications ALTER COLUMN id RESTART WITH 1;
ALTER TABLE post ALTER COLUMN id RESTART WITH 1;

INSERT INTO "user" (user_type, firstname, lastname, username)
VALUES ('user', 'first', 'last', 'testuser');

INSERT INTO "user" (user_type, firstname, lastname, username)
VALUES ('employer', 'first', 'last', 'testemployer');

INSERT INTO resume (user_id)
VALUES (1);

INSERT INTO resume_file (name, type, content, resume_id)
VALUES ('Resume Name', 'Resume Type', '0x0', 1);

INSERT INTO education (degree, university, resume_id)
VALUES ('A Degree', 'A University', 1);

INSERT INTO experience (company, description, resume_id)
VALUES ('A Company', 'A Description', 1);

INSERT INTO skill (name, description, resume_id)
VALUES ('A Name', 'A Description', 1);

INSERT INTO company (name, user_id)
VALUES ('A Company', 2);

INSERT INTO joboffers (company_name, description, highsalery, lowsalery, staff, company_id)
VALUES ('A Company', 'A Description', 0, 0, 0, 1);

INSERT INTO applications (user_id, joboffer_id)
VALUES (1,1);

INSERT INTO post (user_id, comment, content, type, is_hidden)
VALUES (1, 'a comment', 'a content', 'TEXT', false);
