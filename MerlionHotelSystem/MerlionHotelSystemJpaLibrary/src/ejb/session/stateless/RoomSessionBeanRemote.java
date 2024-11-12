/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Room;
import entity.RoomType;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author pramoth
 */
@Remote
public interface RoomSessionBeanRemote {
        public Long createRoom(Room room);
        public void updateRoom(Room room);
        public void deleteRoom(Long roomId);
        public List<Room> retrieveAllRooms();
        public Room retrieveRoomById(Long roomId);
        public List<Room> searchAvailableRoomsForDates(RoomType roomType, Date checkInDate, Date checkOutDate);
        public void checkInGuest(Long roomId);
        public void checkOutGuest(Long roomId);
        public Room retrieveRoomByNumber(String roomNumber);
}
