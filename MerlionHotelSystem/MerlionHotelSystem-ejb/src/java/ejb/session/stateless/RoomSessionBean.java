/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Room;
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
    @Override
    public Long createRoom(Room room) {
        em.persist(room);
        em.flush();
        return room.getId();
    }

    // Use Case 13: Update Room
    @Override
    public void updateRoom(Room room) {
        em.merge(room); 
    }

    // Use Case 14: Delete Room
    @Override
    public void deleteRoom(Long roomId) {
        Room room = em.find(Room.class, roomId);
        if (room != null) {
            em.remove(room);
        }
    }

    // Use Case 15: View All Rooms
    @Override
    public List<Room> retrieveAllRooms() {
        Query query = em.createQuery("SELECT r FROM Room r");
        return query.getResultList();
    }
}