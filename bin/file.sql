CREATE TABLE IF NOT EXISTS rooms (
    room_id INT AUTO_INCREMENT PRIMARY KEY,
    hostel_id INT,
    room_number INT NOT NULL,
    capacity INT NOT NULL,
    availability BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (hostel_id) REFERENCES hostels(hostel_id)
);

CREATE TABLE IF NOT EXISTS rooms (
    room_id INT AUTO_INCREMENT PRIMARY KEY,
    hostel_id INT,
    room_number INT NOT NULL,
    capacity INT NOT NULL,
    availability BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (hostel_id) REFERENCES hostels(hostel_id)
);

CREATE TABLE IF NOT EXISTS students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    student_name VARCHAR(255) NOT NULL,
    gender ENUM('Male', 'Female') NOT NULL,
    year INT NOT NULL,
    room_id INT,
    FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);

DELIMITER $$

DROP TRIGGER IF EXISTS after_student_delete $$

CREATE TRIGGER after_student_delete
AFTER DELETE ON students
FOR EACH ROW
BEGIN
    DECLARE roomCapacity INT;

    IF OLD.room_id IS NOT NULL THEN
        -- Increment the room capacity
        UPDATE rooms
        SET capacity = capacity + 1
        WHERE room_id = OLD.room_id;

        -- Check if the room's capacity is zero after incrementing
        SELECT capacity INTO roomCapacity FROM rooms WHERE room_id = OLD.room_id;
        
        -- If room capacity is greater than zero, update the room state to "vacant"
        IF roomCapacity > 0 THEN
            UPDATE rooms
            SET state = 'vacant'
            WHERE room_id = OLD.room_id;
        END IF;
    END IF;
END $$

DELIMITER ;

