/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Room;
import entity.RoomType;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class RoomSessionBean implements RoomSessionBeanRemote, RoomSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotelSystem-ejbPU")
    private EntityManager em;

    // Use Case 12: Create New Room
    // shld be a conditional for roomtype check
    @Override
    public Long createRoom(Room room) {
        if (room.getRoomType() == null || room.getRoomType().getIsDisabled()) {
            throw new IllegalArgumentException("Invalid Room Type. Room Type is either null or disabled.");
        }
        em.persist(room);
        em.flush();
        return room.getRoomId();
    }
    
    @Override
    public Room retrieveRoomById(Long roomId) {
        //method logic
    }

    // Use Case 13: Update Room
    @Override
    public void updateRoom(Room room) {
        Room existingRoom = em.find(Room.class, room.getRoomId());
        if (existingRoom == null) {
            throw new IllegalArgumentException("Room with ID " + room.getRoomId() + " does not exist.");
        }

        // Prevent updates that would invalidate the current room state
        if (room.getIsDisabled() && existingRoom.getIsAvailable()) {
            throw new IllegalStateException("Cannot disable a room that is currently available.");
        }

        em.merge(room);
    }

    // Use Case 14: Delete Room
    @Override
    public void deleteRoom(Long roomId) {
        Room room = em.find(Room.class, roomId);
        if (room == null) {
            throw new IllegalArgumentException("Room with ID " + roomId + " does not exist.");
        }

        // If the room is in use, disable instead of delete
        if (room.getIsAvailable()) {
            em.remove(room);  // Delete only if not in use
        } else {
            room.setIsDisabled(true);
            em.merge(room);   // Mark as disabled if it cannot be deleted
        }
    }

    // Use Case 15: View All Rooms
    @Override
    public List<Room> retrieveAllRooms() {
        Query query = em.createQuery("SELECT r FROM Room r");
        return query.getResultList();
    }
    
    @Override
    public List<Room> searchAvailableRoomsForDates(RoomType roomType, Date checkInDate, Date checkOutDate) {
        Query query = em.createQuery(
            "SELECT r FROM Room r WHERE r.roomType = :roomType AND r.isOccupied = false AND r.isDisabled = false AND " +
            "r.roomId NOT IN (SELECT resRoom.room.roomId FROM ReservationRoom resRoom " +
            "WHERE resRoom.reservation.checkInDate < :checkOutDate AND resRoom.reservation.checkOutDate > :checkInDate)"
        );
        query.setParameter("roomType", roomType);
        query.setParameter("checkInDate", checkInDate);
        query.setParameter("checkOutDate", checkOutDate);

        return query.getResultList();
    }
    
    @Override
    public void checkInGuest(Long roomId) {
        Room room = em.find(Room.class, roomId);
        if (room != null && room.getIsAvailable()) {
            room.setRoomStatus(true);  // Mark the room as occupied
            em.merge(room);
        } else {
            throw new IllegalStateException("Room is not available for check-in.");
        }
    }
    
    @Override
    public void checkOutGuest(Long roomId) {
        Room room = em.find(Room.class, roomId);
        if (room != null && room.isRoomStatus()) {
            room.setRoomStatus(false);  // Mark the room as unoccupied
            room.setIsAvailable(true);
            em.merge(room);
        } else {
            throw new IllegalStateException("Room is not currently occupied.");
        }
    }
    
    @Override
    public Room retrieveRoomByNumber(String roomNumber) {
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.roomNumber = :roomNumber");
        query.setParameter("roomNumber", roomNumber);
        return (Room) query.getSingleResult();
    }
}