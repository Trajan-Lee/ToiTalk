-- Insert data into languages
INSERT INTO `toitalk`.`languages` (`language_id`, `language_name`) VALUES
(1, 'French'),
(2, 'Spanish'),
(3, 'German');

-- Insert data into users
INSERT INTO `toitalk`.`users` (`user_id`, `username`, `email`, `password`, `user_type`, `create_time`) VALUES
(1, 'tutor_john', 'john@example.com', 'password123', 'tutor', NOW()),
(2, 'studet_jaen', 'jane@example.com', 'password123', 'student', NOW()),
(3, 'tutor_alex', 'alex@example.com', 'password123', 'tutor', NOW()),
(4, 'student_mary', 'mary@example.com', 'password123', 'student', NOW()),
(5, 'tutor_sara', 'sara@example.com', 'password123', 'tutor', NOW());

-- Insert data into tutors
INSERT INTO `toitalk`.`tutors` (`tutor_id`, `language`, `experience_years`, `bio`, `rating`, `user_id`) VALUES
(1, 'French', 5, 'Experienced French tutor', 4.8, 1),
(2, 'Spanish', 3, 'Native Spanish speaker with teaching experience', 4.6, 3),
(3, 'German', 7, 'Passionate about teaching German', 4.9, 5);

-- Insert data into students
INSERT INTO `toitalk`.`students` (`student_id`, `user_id`) VALUES
(1, 2),
(2, 4);

-- Insert data into tutor_languages
INSERT INTO `toitalk`.`tutor_languages` (`tutor_id`, `language_id`) VALUES
(1, 1),  -- John Doe teaches French
(2, 2),  -- Alex teaches Spanish
(3, 3);  -- Sara teaches German

-- Insert data into bookings
INSERT INTO `toitalk`.`bookings` (`booking_id`, `tutor_id`, `student_id`, `tutor_schedule_id`, `date`, `status`) VALUES
(1, 1, 1, 1, '2024-11-05 10:00:00', 'scheduled'),
(2, 2, 1, 2, '2024-11-06 11:00:00', 'completed'),
(3, 3, 2, 3, '2024-11-07 09:00:00', 'scheduled'),
(4, 1, 2, 4, '2024-11-08 12:00:00', 'canceled'),
(5, 2, 2, 5, '2024-11-09 14:00:00', 'scheduled');

-- Insert data into feedback
INSERT INTO `toitalk`.`feedback` (`feedback_id`, `booking_id`, `rating`, `comment`, `created_at`, `tutor_id`) VALUES
(1, 1, 5, 'Great tutor, very helpful!', '2024-11-05 11:00:00', 1),
(2, 2, 4, 'Good session, learned a lot.', '2024-11-06 12:00:00', 2),
(3, 3, 5, 'Excellent tutor!', '2024-11-07 10:00:00', 3),
(4, 4, 3, 'Session was canceled', '2024-11-08 13:00:00', 1),
(5, 5, 4, 'Helpful and friendly', '2024-11-09 15:00:00', 2);
