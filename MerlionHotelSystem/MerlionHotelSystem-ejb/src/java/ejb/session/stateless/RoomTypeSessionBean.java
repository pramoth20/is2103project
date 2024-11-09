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

/**
 *
 * @author jwong
 */
@Stateless
public class RoomTypeSessionBean implements RoomTypeSessionBeanRemote, RoomTypeSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotelSystem-ejbPU")
    private EntityManager em;

   public RoomType createRoomType(String name) {
       RoomType roomType = new RoomType(name);
       em.persist(roomType);
        em.flush();
        return roomType;
   } 
   
   //View Room Type Details
   public RoomType getRoomTypeDetails(Long roomTypeId) throws RoomTypeNotFoundException {
       RoomType roomType = em.find(RoomType.class, roomTypeId);
       if (roomType == null) {
           throw new RoomTypeNotFoundException("Room Type Id of" + roomTypeId + " cannnot be found");
       }
       return roomType;
   }
   
   //Update Room Type
   public RoomType updateRoomType(Long roomTypeId, String name, String details) throws RoomTypeNotFoundException {
       RoomType roomType = em.find(RoomType.class, roomTypeId);
       if (roomType == null) {
           throw new RoomTypeNotFoundException("Room Type of id " + roomTypeId + " cannot be found and updated");
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
   
   //Delete Room Type -> shd I be searching the database yes right 
   public void deleteRoomType(Long roomTypeId) throws RoomTypeNotFoundException {
       RoomType roomType = em.find(RoomType.class, roomTypeId);
       if (roomType == null) {
           throw new RoomTypeNotFoundException("Room Type Id of" + roomTypeId + " cannnot be found and thus deleted");
       }
       Query query = em.createQuery("SELECT r FROM Room r WHERE r.roomType = :roomType");
       query.setParameter("roomType", roomType);
        boolean hasRooms = !query.getResultList().isEmpty();

        if (hasRooms) {
        // If there are associated rooms, disable the RoomType instead
            roomType.setIsDisabled(true);
            em.merge(roomType);
        } else {
        // Otherwise, delete it
        em.remove(roomType);
        }
         
   }
   
    //Display a list of all room type records in the system
    public List<RoomType> retrieveAllRoomTypes() {
    Query query = em.createQuery("SELECT r FROM RoomType r");
    return query.getResultList();
    }
   
   public RoomType getNextRoomType(RoomType current) {
       if (current.getNextRoomType()!= null) {
           return current.getNextRoomType();
       } else {
           System.out.println("This is the Grand Suite. No further upgrades available");
           return current;
       }
       
   }
   
   
   
}
