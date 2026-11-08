package azh.bkd.demo.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "room_bookings")
public class RoomBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentName;

    private String roomNo;

    private String day;

    private String timeSlot;

    private String purpose;

    private String status;

    private String adminMessage;

    // Smart Engine Fields

    private String smartEngineStage;

    private String approvedRoom;

    private String alternateRoom;

    private String conflictWith;

    private String userDecision;

    private LocalDateTime approvedAt;

public LocalDateTime getApprovedAt() {
    return approvedAt;
}



    // ===========================
    // Getters & Setters
    // ===========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }



    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    @Column(name = "booked_by")
private String bookedBy;

public String getBookedBy() {
    return bookedBy;
}

public void setBookedBy(String bookedBy) {
    this.bookedBy = bookedBy;
}


    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdminMessage() {
        return adminMessage;
    }

    public void setAdminMessage(String adminMessage) {
        this.adminMessage = adminMessage;
    }

    public String getSmartEngineStage() {
        return smartEngineStage;
    }

    public void setSmartEngineStage(String smartEngineStage) {
        this.smartEngineStage = smartEngineStage;
    }

    public String getApprovedRoom() {
        return approvedRoom;
    }

    public void setApprovedRoom(String approvedRoom) {
        this.approvedRoom = approvedRoom;
    }

    public String getAlternateRoom() {
        return alternateRoom;
    }

    public void setAlternateRoom(String alternateRoom) {
        this.alternateRoom = alternateRoom;
    }

    public String getConflictWith() {
        return conflictWith;
    }

    public void setConflictWith(String conflictWith) {
        this.conflictWith = conflictWith;
    }

    public String getUserDecision() {
        return userDecision;
    }

    public void setUserDecision(String userDecision) {
        this.userDecision = userDecision;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
    this.approvedAt = approvedAt;
}
}