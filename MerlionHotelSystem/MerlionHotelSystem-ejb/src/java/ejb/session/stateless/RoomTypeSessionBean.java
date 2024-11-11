/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.RoomTypeNotFoundException;

@Stateless
public class RoomTypeSessionBean implements RoomTypeSessionBeanRemote, RoomTypeSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotelSystem-ejbPU")
    private EntityManager em;

    // Create a new Room Type
    @Override
    public RoomType createRoomType(String name) {
        // Check if RoomType with the same name already exists
        Query query = em.createQuery("SELECT r FROM RoomType r WHERE r.name = :name");
        query.setParameter("name", name);
        if (!query.getResultList().isEmpty()) {
            throw new IllegalArgumentException("Room Type with name " + name + " already exists.");
        }

        RoomType roomType = new RoomType();
        roomType.setName(name);
        em.persist(roomType);
        em.flush();
        return roomType;
    }

    // View Room Type Details
    @Override
    public RoomType getRoomTypeDetails(Long roomTypeId) throws RoomTypeNotFoundException {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        if (roomType == null) {
            throw new RoomTypeNotFoundException("Room Type with ID " + roomTypeId + " cannot be found.");
        }
        return roomType;
    }

    // Update Room Type
    @Override
    public RoomType updateRoomType(Long roomTypeId, String name, String details) throws RoomTypeNotFoundException {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        if (roomType == null) {
            throw new RoomTypeNotFoundException("Room Type with ID " + roomTypeId + " cannot be found and updated.");
        }

        if (roomType.getIsDisabled()) {
            throw new IllegalStateException("Cannot update a disabled Room Type.");
        }

        if (name != null) {
            roomType.setName(name);
        }
        if (details != null) {
            roomType.setDetails(details);
        }

        em.merge(roomType);
        return roomType;
    }

    // Delete Room Type
    public void deleteRoomType(Long roomTypeId) throws RoomTypeNotFoundException {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        if (roomType == null) {
            throw new RoomTypeNotFoundException("Room Type with ID " + roomTypeId + " cannot be found and deleted.");
        }

        Query query = em.createQuery("SELECT r FROM Room r WHERE r.roomType = :roomType");
        query.setParameter("roomType", roomType);
        boolean hasRooms = !query.getResultList().isEmpty();

        if (hasRooms) {
            roomType.setIsDisabled(true);
            em.merge(roomType);
        } else {
            em.remove(roomType);
        }
    }

    // Retrieve All Room Types
    @Override
    public List<RoomType> retrieveAllRoomTypes() {
        Query query = em.createQuery("SELECT r FROM RoomType r WHERE r.isDisabled = FALSE");
        return query.getResultList();
    }

    // Get Next Room Type
    public RoomType getNextRoomType(RoomType current) {
        if (current.getNextRoomType() != null) {
            return current.getNextRoomType();
        } else {
            throw new IllegalArgumentException("This is the highest available room type. No further upgrades available.");
        }
    }
}