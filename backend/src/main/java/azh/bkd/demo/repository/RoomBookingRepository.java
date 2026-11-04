package azh.bkd.demo.repository;

import azh.bkd.demo.model.RoomBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomBookingRepository extends JpaRepository<RoomBooking, Long> {

    List<RoomBooking> findByRoomNoAndDayAndTimeSlotAndStatus(
            String roomNo,
            String day,
            String timeSlot,
            String status
    );

    List<RoomBooking> findByStudentName(String studentName);

    List<RoomBooking> findByStatus(String status);
}