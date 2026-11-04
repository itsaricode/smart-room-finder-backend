package azh.bkd.demo.controller;

import azh.bkd.demo.repository.RoomBookingRepository;
import azh.bkd.demo.repository.RoomRepository;
import azh.bkd.demo.repository.UserRepository;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")

public class AnalyticsController {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final RoomBookingRepository bookingRepository;

    public AnalyticsController(UserRepository userRepository, RoomRepository roomRepository,
                               RoomBookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("/dashboard-stats")
    public Map<String, Object> getDashboardStats() {

        Map<String, Object> stats = new HashMap<>();

        stats.put("totalUsers", userRepository.count());
        stats.put("totalRooms", roomRepository.count());
        stats.put("totalBookings", bookingRepository.count());

        return stats;
    }
}