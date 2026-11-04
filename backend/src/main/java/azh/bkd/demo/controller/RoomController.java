package azh.bkd.demo.controller;

import azh.bkd.demo.model.Room;
import azh.bkd.demo.service.RoomService;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @PostMapping
    public Room addRoom(@RequestBody Room room) {
        return roomService.saveRoom(room);
    }

    @GetMapping("/{id}")
    public Optional<Room> getRoom(@PathVariable Long id) {
        return roomService.getRoomById(id);
    }

    @GetMapping("/department/{department}")
    public List<Room> getDepartmentRooms(@PathVariable String department) {
        return roomService.getDepartmentRooms(department);
    }

    @GetMapping("/status/{status}")
    public List<Room> getRoomsByStatus(@PathVariable String status) {
        return roomService.getRoomsByStatus(status);
    }

    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
    }
}