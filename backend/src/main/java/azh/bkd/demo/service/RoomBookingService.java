package azh.bkd.demo.service;

import azh.bkd.demo.model.RoomBooking;
import azh.bkd.demo.repository.RoomBookingRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import java.time.LocalDateTime;

@Service
public class RoomBookingService {

    private final RoomBookingRepository bookingRepository;

    public RoomBookingService(RoomBookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    // Save Booking
    public RoomBooking saveBooking(RoomBooking booking) {
        return bookingRepository.save(booking);
    }

    // Get All Bookings
    public List<RoomBooking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Get Booking By ID
    public Optional<RoomBooking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    // Student Booking History
    public List<RoomBooking> getStudentBookings(String studentName) {
        return bookingRepository.findByStudentName(studentName);
    }

    // Delete Booking
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    // Conflict Detection
    public List<RoomBooking> checkConflicts(
            String roomNo,
            String day,
            String timeSlot) {

        return bookingRepository.findByRoomNoAndDayAndTimeSlotAndStatus(
                roomNo,
                day,
                timeSlot,
                "APPROVED");
    }

    // Smart Engine Methods

    public void updateStage(RoomBooking booking, String stage) {
        booking.setSmartEngineStage(stage);
        bookingRepository.save(booking);
    }

   public void approve(RoomBooking booking, String message) {

    booking.setStatus("APPROVED");
    booking.setAdminMessage(message);
    booking.setSmartEngineStage("Completed");
    booking.setApprovedRoom(booking.getRoomNo());

    booking.setApprovedAt(LocalDateTime.now());

    bookingRepository.save(booking);
}
    public void reject(RoomBooking booking, String reason) {

        booking.setStatus("REJECTED");
        booking.setAdminMessage(reason);
        booking.setSmartEngineStage("Completed");

        bookingRepository.save(booking);
    }



// ======================================
// AVAILABLE AGAIN
// ======================================

public void availableAgain(RoomBooking booking) {

    booking.setStatus("COMPLETED");
    booking.setSmartEngineStage("Completed");

    bookingRepository.save(booking);
}
public void completeBooking(RoomBooking booking) {

    booking.setStatus("COMPLETED");

    booking.setSmartEngineStage("Completed");

    
    booking.setAdminMessage("Room is available again.");

    bookingRepository.save(booking);
}

public List<RoomBooking> getApprovedBookings() {

    return bookingRepository.findByStatus("APPROVED");
}

public void markCompleted(Long id) {

    Optional<RoomBooking> booking = bookingRepository.findById(id);

    if (booking.isPresent()) {

        RoomBooking b = booking.get();

        b.setStatus("COMPLETED");

        b.setSmartEngineStage("Completed");

        bookingRepository.save(b);
    }
    
}

}