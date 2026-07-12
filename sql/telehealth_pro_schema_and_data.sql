-- ==========================================================
-- TeleHealth Pro - Database Setup Script
-- MySQL 8.x
-- ==========================================================
-- NOTE: The application uses Hibernate (spring.jpa.hibernate.ddl-auto=update)
-- to auto-create/update these tables on startup, and DataInitializer.java
-- seeds the admin account + 10 categories automatically.
--
-- This script is provided for anyone who prefers to set up the database
-- manually, or who wants to load extra sample doctors/appointments.
-- Run schema section first (or let Hibernate do it), then run the
-- sample data section.
-- ==========================================================

CREATE DATABASE IF NOT EXISTS telehealth_pro
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE telehealth_pro;

-- ----------------------------------------------------------
-- Table: users
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    mobile VARCHAR(15),
    gender VARCHAR(10),
    dob DATE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER',
    enabled BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;

-- ----------------------------------------------------------
-- Table: doctor_categories
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS doctor_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL UNIQUE,
    icon_class VARCHAR(50) DEFAULT 'bi-heart-pulse'
) ENGINE=InnoDB;

-- ----------------------------------------------------------
-- Table: doctors
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS doctors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    doctor_name VARCHAR(100) NOT NULL,
    qualification VARCHAR(150),
    specialization VARCHAR(100),
    experience_years INT,
    hospital VARCHAR(150),
    consultation_fee DECIMAL(10,2),
    rating DECIMAL(3,1),
    available_days VARCHAR(100),
    availability VARCHAR(20) DEFAULT 'Available',
    photo VARCHAR(255) DEFAULT '',
    category_id BIGINT NOT NULL,
    CONSTRAINT fk_doctor_category FOREIGN KEY (category_id) REFERENCES doctor_categories(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ----------------------------------------------------------
-- Table: appointments
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS appointments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'Pending',
    user_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    CONSTRAINT fk_appointment_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_appointment_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ==========================================================
-- SAMPLE DATA
-- ==========================================================

-- Default admin account (password: Admin@123, BCrypt-encoded)
-- This exact hash is also generated automatically by DataInitializer.java on first run.
INSERT INTO users (full_name, email, mobile, gender, dob, password, role, enabled)
SELECT 'System Administrator', 'admin@telehealthpro.com', '9999999999', 'Other', '1990-01-01',
       '$2b$10$mw0SrdQAv4mON2pzWYztOefzJf4Eik/F8.HMOGtLH/IqgmHQgbcVi', 'ROLE_ADMIN', TRUE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@telehealthpro.com');

-- Doctor Categories
INSERT INTO doctor_categories (category_name, icon_class)
SELECT * FROM (SELECT 'General Physician' AS category_name, 'bi-heart-pulse' AS icon_class) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM doctor_categories WHERE category_name = 'General Physician');

INSERT INTO doctor_categories (category_name, icon_class)
SELECT * FROM (SELECT 'Cardiologist', 'bi-heart') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM doctor_categories WHERE category_name = 'Cardiologist');

INSERT INTO doctor_categories (category_name, icon_class)
SELECT * FROM (SELECT 'Dermatologist', 'bi-bandaid') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM doctor_categories WHERE category_name = 'Dermatologist');

INSERT INTO doctor_categories (category_name, icon_class)
SELECT * FROM (SELECT 'Orthopedic', 'bi-person-wheelchair') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM doctor_categories WHERE category_name = 'Orthopedic');

INSERT INTO doctor_categories (category_name, icon_class)
SELECT * FROM (SELECT 'Neurologist', 'bi-cpu') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM doctor_categories WHERE category_name = 'Neurologist');

INSERT INTO doctor_categories (category_name, icon_class)
SELECT * FROM (SELECT 'ENT Specialist', 'bi-ear') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM doctor_categories WHERE category_name = 'ENT Specialist');

INSERT INTO doctor_categories (category_name, icon_class)
SELECT * FROM (SELECT 'Dentist', 'bi-emoji-smile') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM doctor_categories WHERE category_name = 'Dentist');

INSERT INTO doctor_categories (category_name, icon_class)
SELECT * FROM (SELECT 'Gynecologist', 'bi-gender-female') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM doctor_categories WHERE category_name = 'Gynecologist');

INSERT INTO doctor_categories (category_name, icon_class)
SELECT * FROM (SELECT 'Pediatrician', 'bi-emoji-laughing') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM doctor_categories WHERE category_name = 'Pediatrician');

INSERT INTO doctor_categories (category_name, icon_class)
SELECT * FROM (SELECT 'Psychiatrist', 'bi-brain') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM doctor_categories WHERE category_name = 'Psychiatrist');

-- Sample Doctors (a handful per category; add more via Admin Panel as needed)
INSERT INTO doctors (doctor_name, qualification, specialization, experience_years, hospital, consultation_fee, rating, available_days, availability, photo, category_id)
SELECT 'Dr. Alan Turing', 'MBBS, MD', 'General Physician', 12, 'City General Hospital', 40.00, 4.6, 'Mon-Sat, 9am-5pm', 'Available',
       '', id FROM doctor_categories WHERE category_name = 'General Physician'
UNION ALL
SELECT 'Dr. Grace Hopper', 'MBBS, MD (Cardiology)', 'Cardiologist', 15, 'Heart Care Institute', 80.00, 4.8, 'Mon-Fri, 10am-6pm', 'Available',
       '', id FROM doctor_categories WHERE category_name = 'Cardiologist'
UNION ALL
SELECT 'Dr. Ada Lovelace', 'MBBS, MD (Dermatology)', 'Dermatologist', 8, 'SkinCare Clinic', 55.00, 4.5, 'Tue-Sat, 11am-7pm', 'Available',
       '', id FROM doctor_categories WHERE category_name = 'Dermatologist'
UNION ALL
SELECT 'Dr. James Clerk', 'MBBS, MS (Ortho)', 'Orthopedic', 10, 'Bone & Joint Center', 65.00, 4.4, 'Mon-Fri, 9am-4pm', 'Busy',
       '', id FROM doctor_categories WHERE category_name = 'Orthopedic'
UNION ALL
SELECT 'Dr. Marie Curie', 'MBBS, DM (Neurology)', 'Neurologist', 18, 'NeuroHealth Hospital', 90.00, 4.9, 'Mon-Thu, 10am-5pm', 'Available',
       '', id FROM doctor_categories WHERE category_name = 'Neurologist'
UNION ALL
SELECT 'Dr. Isaac Newton', 'MBBS, MS (ENT)', 'ENT Specialist', 9, 'ENT Care Clinic', 45.00, 4.3, 'Wed-Sun, 9am-3pm', 'Available',
       '', id FROM doctor_categories WHERE category_name = 'ENT Specialist'
UNION ALL
SELECT 'Dr. Rosalind Franklin', 'BDS, MDS', 'Dentist', 7, 'Smile Dental Care', 35.00, 4.7, 'Mon-Sat, 9am-6pm', 'Available',
       '', id FROM doctor_categories WHERE category_name = 'Dentist'
UNION ALL
SELECT 'Dr. Elizabeth Blackwell', 'MBBS, MD (OB-GYN)', 'Gynecologist', 14, 'Women''s Health Center', 60.00, 4.8, 'Mon-Fri, 10am-5pm', 'Available',
       '', id FROM doctor_categories WHERE category_name = 'Gynecologist'
UNION ALL
SELECT 'Dr. Charles Darwin', 'MBBS, MD (Pediatrics)', 'Pediatrician', 11, 'Kids Care Hospital', 50.00, 4.6, 'Mon-Sat, 9am-5pm', 'Available',
       '', id FROM doctor_categories WHERE category_name = 'Pediatrician'
UNION ALL
SELECT 'Dr. Sigmund Freud', 'MBBS, MD (Psychiatry)', 'Psychiatrist', 16, 'Mind Wellness Clinic', 70.00, 4.5, 'Tue-Sat, 11am-7pm', 'Available',
       '', id FROM doctor_categories WHERE category_name = 'Psychiatrist';

-- ==========================================================
-- End of script
-- ==========================================================
