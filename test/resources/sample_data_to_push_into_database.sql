-- Specify the database (schema)
USE soen6011;

-- Add dummy data for table 'users'
INSERT INTO user (user_type, email, firstname, lastname, password, username, is_active)
VALUES
    ('user', 'admin@example.com', 'Admin', 'User', 'admin123', 'admin_user', true),
    ('user', 'candidate1@example.com', 'John', 'Doe', 'candidate123', 'john_doe', true),
    ('user', 'candidate2@example.com', 'Jane', 'Smith', 'candidate123', 'jane_smith', false),
	('user', 'candidate3@example.com', 'John', 'Smith', 'candidate123', 'john_smith', false),
    ('user', 'candidate4@example.com', 'Morteza', 'Kh', 'candidate123', 'morteza_kh', true),
    ('user', 'candidate5@example.com', 'Niousha', 'Ma', 'candidate123', 'niousha_ma', true),
    ('employer', 'employer1@example.com', 'Mike', 'Johnson', 'employer123', 'mike_johnson', true),
    ('employer', 'employer2@example.com', 'Emily', 'Davis', 'employer123', 'emily_davis', true);

INSERT INTO role (user_id, name)
VALUES
    (1, 'USER'),
    (1, 'ADMIN'),
    (2, 'USER'),
    (3, 'USER'),
    (4, 'USER'),
    (5, 'USER'),
    (6, 'USER'),
    (7, 'USER'),
    (8, 'USER');
	
-- Add dummy data for table 'resume'
INSERT INTO resume (created_date, user_id)
VALUES
    ('2023-01-01', 2),
	('2023-01-10', 3),
    ('2023-01-05', 4),
    ('2023-01-23', 5),
    ('2023-02-01', 6);
    
-- Add dummy data for table 'skills'
INSERT INTO skill (name, description, resume_id)
VALUES
    ('Software Development', 'Java, C++, Python', 1),
    ('Database Management', 'SQL, MySQL, Oracle', 1),
    ('Web Development', 'HTML, CSS, JavaScript', 2),
    ('Data Analysis', 'R, Python, SQL', 2),
    ('Project Management', 'Agile, Scrum, Kanban', 3),
    ('UX Design', 'UI/UX, Wireframing, Prototyping', 3),
    ('Marketing', 'Digital Marketing, Social Media, SEO', 4),
    ('Sales', 'Negotiation, Customer Relationship, Closing Deals', 4),
    ('Software Testing', 'Manual Testing, Automated Testing', 5),
    ('Networking', 'TCP/IP, Routing, Firewalls', 5);

-- Add dummy data for table 'experiences'
INSERT INTO experience (company, description, start_date, finish_date, resume_id)
VALUES
    ('ABC Company', 'Software Developer', '2022-06-30', '2018-09-01', 1),
    ('XYZ Corporation', 'Data Analyst', '2022-06-30', '2018-09-01', 1),
    ('123 Industries', 'Marketing Specialist', '2022-06-30', '2018-09-01', 2),
    ('ABC Company', 'Web Designer', '2022-06-30', '2018-09-01', 2),
    ('XYZ Corporation', 'Sales Representative', '2022-06-30', '2018-09-01', 3),
    ('123 Industries', 'Software Tester', '2022-06-30', '2018-09-01', 3),
    ('ABC Company', 'Network Engineer', '2022-06-30', '2018-09-01', 4),
    ('XYZ Corporation', 'UX Designer', '2022-06-30', '2018-09-01', 4),
    ('123 Industries', 'Project Manager', '2022-06-30', '2018-09-01', 5),
    ('ABC Company', 'Marketing Manager', '2022-06-30', '2018-09-01', 5);

-- Add dummy data for table 'educations'
INSERT INTO education (degree, finish_date, start_date, university, resume_id)
VALUES
    ('Bachelor of Science', '2022-06-30', '2018-09-01', 'University of Example', 1),
    ('Master of Business Administration', '2024-06-30', '2022-09-01', 'University of Example', 1),
    ('Bachelor of Arts', '2022-06-30', '2018-09-01', 'University of Example', 2),
    ('Master of Science', '2023-06-30', '2019-09-01', 'University of Example', 2),
    ('Bachelor of Engineering', '2021-06-30', '2017-09-01', 'University of Example', 3),
    ('Master of Science', '2022-06-30', '2021-09-01', 'University of Example', 3),
    ('Bachelor of Business Administration', '2021-06-30', '2017-09-01', 'University of Example', 4),
    ('Master of Arts', '2023-06-30', '2019-09-01', 'University of Example', 4),
    ('Bachelor of Science', '2020-06-30', '2016-09-01', 'University of Example', 5),
    ('Master of Engineering', '2021-06-30', '2020-09-01', 'University of Example', 5);

-- Add dummy data for table 'joboffers'
INSERT INTO company (name, user_id)
VALUES
    ('XYZ Corporation', 7),
    ('123 Industries', 8);

-- Add dummy data for table 'joboffers'
INSERT INTO joboffers (company_name, deadline, description, published, highsalery, lowsalery, staff, status, title, company_id)
VALUES
    ('XYZ Corporation', '2023-08-01', 'Software Developer', '2023-07-01', 60000, 30000, 10, 'PENDING', 'Software Developer', 1),
    ('XYZ Corporation', '2023-08-15', 'Data Analyst', '2023-07-05', 50000, 20000, 5, 'ACCEPTED', 'Data Analyst', 1),
    ('123 Industries', '2023-09-01', 'Marketing Specialist', '2023-07-10', 45000, 30000, 3, 'REJECTED', 'Marketing Specialist', 2),
    ('XYZ Corporation', '2023-08-31', 'Web Designer', '2023-07-15', 55000, 30000, 8, 'PENDING', 'Web Designer', 1),
    ('XYZ Corporation', '2023-09-15', 'Sales Representative', '2023-07-20', 40000, 20000, 6, 'ACCEPTED', 'Sales Representative', 1),
    ('123 Industries', '2023-09-30', 'Software Tester', '2023-07-25', 45000, 30000, 4, 'REJECTED', 'Software Tester', 2),
    ('XYZ Corporation', '2023-10-01', 'Network Engineer', '2023-07-30', 65000, 35000, 7, 'PENDING', 'Network Engineer', 1),
    ('XYZ Corporation', '2023-10-15', 'UX Designer', '2023-08-01', 55000, 30000, 5, 'ACCEPTED', 'UX Designer', 1),
    ('123 Industries', '2023-10-31', 'Project Manager', '2023-08-05', 70000, 40000, 8, 'REJECTED', 'Project Manager', 2),
    ('123 Industries', '2023-11-01', 'Marketing Manager', '2023-08-10', 80000, 50000, 10, 'PENDING', 'Marketing Manager', 2);

-- Add dummy data for table 'interview'
INSERT INTO interview (date, time, type )
VALUES
    ('2023-02-01', '10:00', 'Online'),
    ('2023-02-02', '10:00', 'Online' ),
    ('2023-02-03', '14:00', 'In_Person' ),
    ('2023-02-04', '14:00', 'In_Person' ),
    ('2023-02-05', '14:00', 'In_Person' ),
    ('2023-02-06', '10:00', 'Online'),
    ('2023-02-07', '10:00', 'Online' ),
    ('2023-02-08', '14:00', 'In_Person' ),
    ('2023-02-09', '14:00', 'In_Person' ),
    ('2023-02-10', '14:00', 'In_Person' ),
    ('2023-02-11', '10:00', 'Online'),
    ('2023-02-12', '10:00', 'Online' ),
    ('2023-02-13', '14:00', 'In_Person' ),
    ('2023-02-14', '14:00', 'In_Person' ),
    ('2023-02-15', '14:00', 'In_Person' ),
    ('2023-02-16', '10:00', 'Online'),
    ('2023-02-17', '10:00', 'Online' ),
    ('2023-02-18', '14:00', 'In_Person' ),
    ('2023-02-19', '14:00', 'In_Person' ),
    ('2023-02-20', '14:00', 'In_Person' );

-- Add dummy data for table 'applications'
INSERT INTO applications (status, user_id, joboffer_id, interview_id, dateapplied )
VALUES
    ('ACCEPTED', 2, 1, 1, '2023-01-01'),
    ('INPROGRESS', 3, 1, 2, '2023-01-02' ),
    ('ACCEPTED', 2, 2, 3, '2023-01-03' ),
    ('INPROGRESS', 4, 2, 4, '2023-01-04' ),
    ('ACCEPTED', 3, 3, 5, '2023-01-05' ),
    ('INPROGRESS', 5, 3, 6, '2023-01-06' ),
    ('ACCEPTED', 4, 4, 7, '2023-01-07' ),
    ('INPROGRESS', 5, 4, 8, '2023-01-08' ),
    ('ACCEPTED', 3, 5, 9, '2023-01-09' ),
    ('INPROGRESS', 4, 6, 10, '2023-01-10' ),
    ('ACCEPTED', 5, 6, 11, '2023-01-11' ),
    ('INPROGRESS', 2, 7, 12, '2023-01-12' ),
    ('ACCEPTED', 3, 7, 13, '2023-01-13' ),
    ('INPROGRESS', 4, 8, 14, '2023-01-14' ),
    ('ACCEPTED', 5, 8, 15, '2023-01-15' ),
    ('INPROGRESS', 3, 9, 16, '2023-01-16' ),
    ('ACCEPTED', 4, 9, 17, '2023-01-17' ),
    ('INPROGRESS', 2, 10, 18, '2023-01-18' ),
    ('ACCEPTED', 3, 10, 19, '2023-01-19' ),
    ('INPROGRESS', 5, 5, 20, '2023-01-20' );
	
-- Add dummy data for table 'applications'	
INSERT INTO post (user_id, comment, content, type, date_time, is_hidden)
VALUES (2, 'Happy birthday!', '', 'TEXT', '2023-02-01 10:00', false);
INSERT INTO post (user_id, comment, content, type, date_time, is_hidden)
VALUES (2, 'Check out this site!', 'http://localhost:8080/profile/john_doe', 'LINK', '2023-02-01 22:00', false);
INSERT INTO post (user_id, comment, content, type, date_time, is_hidden)
VALUES (3, 'Did you watch this video?', 'https://www.youtube.com/watch?v=co47u19cbds', 'LINK', '2023-02-01 10:00', false);
INSERT INTO post (user_id, comment, content, type, date_time, is_hidden)
VALUES (3, 'I am travelling to Mexico', '', 'TEXT', '2023-02-01 22:00', false);
INSERT INTO post (user_id, comment, content, type, date_time, is_hidden)
VALUES (4, 'Can anyone recommend a good restaurant', '', 'TEXT', '2023-02-01 10:00', false);
INSERT INTO post (user_id, comment, content, type, date_time, is_hidden)
VALUES (4, 'Go Canadians!', '', 'TEXT', '2023-02-01 22:00', false);
