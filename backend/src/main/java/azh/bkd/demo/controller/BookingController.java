package azh.bkd.demo.controller;
import azh.bkd.demo.model.Room;
import azh.bkd.demo.service.RoomService;
import azh.bkd.demo.model.RoomBooking;
import azh.bkd.demo.service.RoomBookingService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private final RoomBookingService bookingService;
private final RoomService roomService;

public BookingController(
        RoomBookingService bookingService,
        RoomService roomService) {

    this.bookingService = bookingService;
    this.roomService = roomService;
}

    // =====================================
    // Student sends booking request
    // =====================================
@PostMapping("/request")
public ResponseEntity<RoomBooking> requestBooking(
        @RequestBody RoomBooking booking) {

    // ===============================
    // Initial Request
    // ===============================

    booking.setStatus("PENDING");
    booking.setSmartEngineStage("Reading Timetable...");
    booking.setAdminMessage("Smart Engine is processing your request...");

    RoomBooking savedBooking = bookingService.saveBooking(booking);

    // Return immediately so admin can see PENDING
    ResponseEntity<RoomBooking> response =
            ResponseEntity.ok(savedBooking);

    // ===============================
    // Smart Engine Background Thread
    // ===============================

    new Thread(() -> {

        try {

            // Reading Timetable
            Thread.sleep(2000);

            savedBooking.setStatus("PROCESSING");
            savedBooking.setSmartEngineStage("Reading Timetable...");
            bookingService.saveBooking(savedBooking);

            // Checking Availability
            Thread.sleep(2000);

            savedBooking.setSmartEngineStage(
                    "Checking Room Availability...");
            bookingService.saveBooking(savedBooking);

            // Check Conflict
            List<RoomBooking> conflicts =
                    bookingService.checkConflicts(
                            savedBooking.getRoomNo(),
                            savedBooking.getDay(),
                            savedBooking.getTimeSlot());

            // Finalizing
            Thread.sleep(2000);

            savedBooking.setSmartEngineStage(
                    "Finalizing Allocation...");
            bookingService.saveBooking(savedBooking);

            Thread.sleep(2000);

            // ===============================
            // Conflict
            // ===============================

            if (!conflicts.isEmpty()) {

                bookingService.reject(
                        savedBooking,
                        "Room is already booked.");

                return;
            }

            // ===============================
            // Approved
            // ===============================

            bookingService.approve(
                    savedBooking,
                    "Automatically approved by Smart Engine.");

            Room room =
                    roomService.getRoomByRoomNo(
                            savedBooking.getRoomNo());

            if (room != null) {

                room.setStatus("OCCUPIED");
                room.setAvailable(false);

                roomService.saveRoom(room);

                System.out.println(
                        "Room Occupied Successfully");
            }

            // ===============================
            // Auto Complete after 30 sec
            // ===============================

            Thread.sleep(30000);

            RoomBooking latestBooking =
                    bookingService
                            .getBookingById(savedBooking.getId())
                            .orElse(null);

            if (latestBooking != null &&
                    "APPROVED".equals(latestBooking.getStatus())) {

                bookingService.completeBooking(
                        latestBooking);

                Room latestRoom =
                        roomService.getRoomByRoomNo(
                                latestBooking.getRoomNo());

                if (latestRoom != null) {

                    latestRoom.setStatus("AVAILABLE");
                    latestRoom.setAvailable(true);

                    roomService.saveRoom(latestRoom);

                    System.out.println(
                            "Room Available Again");
                }
            }

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

        }

    }).start();

    return response;
}
    // =====================================
    // Admin gets all bookings
    // =====================================

    @GetMapping("/all")
    public List<RoomBooking> getAllBookings() {

        return bookingService.getAllBookings();

    }

    // =====================================
    // Student booking history
    // =====================================


// =====================================
// Available Again
// =====================================

@PostMapping("/available-again/{id}")
public ResponseEntity<String> availableAgain(
        @PathVariable Long id) {

    RoomBooking booking = bookingService
            .getBookingById(id)
            .orElse(null);

    if (booking == null) {
        return ResponseEntity.notFound().build();
    }

    Room room = roomService
            .getAllRooms()
            .stream()
            .filter(r -> r.getRoomNo().equals(booking.getRoomNo()))
            .findFirst()
            .orElse(null);

    if (room != null) {
        room.setStatus("AVAILABLE");
        room.setAvailable(true);
        roomService.saveRoom(room);
    }

    bookingService.availableAgain(booking);

    return ResponseEntity.ok("Room is Available Again.");
}

// =====================================
// Complete Booking
// =====================================
@PutMapping("/{id}/complete")
public ResponseEntity<RoomBooking> completeBooking(
        @PathVariable Long id) {

    Optional<RoomBooking> booking =
            bookingService.getBookingById(id);

    if (booking.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    RoomBooking roomBooking = booking.get();

    bookingService.completeBooking(roomBooking);

    // ❌ Yahan room AVAILABLE mat karo.

    return ResponseEntity.ok(roomBooking);
  }
    // =====================================
    // Delete Booking
    // =====================================

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(
            @PathVariable Long id) {

        bookingService.deleteBooking(id);

        return ResponseEntity.ok("Booking deleted successfully.");

    }


}