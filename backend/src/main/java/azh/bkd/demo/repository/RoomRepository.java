package azh.bkd.demo.repository;

import azh.bkd.demo.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import java.util.Optional;



public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByDepartment(String department);

    List<Room> findByStatus(String status);

    List<Room> findByRoomType(String roomType);

    List<Room> findByAvailableTrue();

    List<Room> findByAvailableTrueAndActiveTrue();

   Room findByRoomNo(String roomNo);

    Optional<Room> findFirstByAvailableTrueAndActiveTrue();

}