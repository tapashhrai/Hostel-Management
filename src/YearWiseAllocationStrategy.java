// Import necessary package
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import HostelManagement.*;

// Implementation of the RoomAllocationStrategy interface to allocate rooms to students based on their year
public class YearWiseAllocationStrategy implements RoomAllocationStrategy {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hostelmanagement";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    // Method to allocate rooms to students based on their year
    @Override
    public void allocateRooms() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Allocate rooms to students of each year
            allocateRoomsByYear(conn, "1st Year", 1);
            allocateRoomsByYear(conn, "2nd Year", 2);
            allocateRoomsByYear(conn, "3rd Year", 3);
            allocateRoomsByYear(conn, "4th Year", 4);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Method to allocate rooms to students of a specific year
    private void allocateRoomsByYear(Connection conn, String year, int roomId) throws SQLException {
        // SQL query to retrieve students of a specific year without assigned rooms
        String query = "SELECT * FROM students WHERE year = ? AND room_id IS NULL";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, year);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int studentId = rs.getInt("id");
                    // Allocate room to each student
                    allocateRoom(conn, studentId, roomId);
                }
            }
        }
    }

    // Method to allocate room to a specific student
    private void allocateRoom(Connection conn, int studentId, int roomId) throws SQLException {
        // Check if the room has available capacity
        String checkCapacityQuery = "SELECT capacity FROM rooms WHERE room_id = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkCapacityQuery)) {
            checkStmt.setInt(1, roomId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    int capacity = rs.getInt("capacity");
                    if (capacity > 0) {
                        // Allocate the room to the student
                        String allocateQuery = "UPDATE students SET room_id = ? WHERE id = ?";
                        try (PreparedStatement allocateStmt = conn.prepareStatement(allocateQuery)) {
                            allocateStmt.setInt(1, roomId);
                            allocateStmt.setInt(2, studentId);
                            allocateStmt.executeUpdate();
                        }

                        // Decrement the room capacity
                        String updateCapacityQuery = "UPDATE rooms SET capacity = capacity - 1 WHERE room_id = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateCapacityQuery)) {
                            updateStmt.setInt(1, roomId);
                            updateStmt.executeUpdate();
                        }

                        // Check if the capacity has reached zero and update the room state to occupied
                        if (capacity - 1 == 0) {
                            String updateStateQuery = "UPDATE rooms SET state = 'occupied' WHERE room_id = ?";
                            try (PreparedStatement updateStateStmt = conn.prepareStatement(updateStateQuery)) {
                                updateStateStmt.setInt(1, roomId);
                                updateStateStmt.executeUpdate();
                            }
                        }
                    } else {
                        // Print a message if the room is full
                        System.out.println("Room " + roomId + " is full.");
                    }
                }
            }
        }
    }
}
