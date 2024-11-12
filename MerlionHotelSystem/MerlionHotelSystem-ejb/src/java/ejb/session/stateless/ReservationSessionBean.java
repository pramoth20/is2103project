/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;
import entity.Reservation;
import entity.RoomType;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jwong
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    //Inject entity Manager
    @PersistenceContext(unitName = "MerlionHotelSystem-ejbPU")
    private EntityManager em;

    /*public void persist(Object object) {
        em.persist(object);
    }*/

    @Override
    public Long createNewReservation(Reservation reservation) {
        em.persist(reservation);
        em.flush();
        return reservation.getReservationId();
    }
            
    @Override
    public void registerReservation(String roomNumber, boolean status, RoomType roomType) {
        Reservation reservation = new Reservation(roomNumber, status, roomType);
        em.persist(reservation);
    }

    @Override
    public Reservation findReservation(Long reservationId) {
        return em.find(Reservation.class, reservationId);
    }
    
    @Override
    public void updateReservation(Reservation reservation) {
        // Find the existing reservation to ensure it is present
        Reservation existingReservation = em.find(Reservation.class, reservation.getReservationId());

        if (existingReservation != null) {
            // Use the EntityManager's merge method to update the reservation
            em.merge(reservation);
        } else {
            throw new IllegalArgumentException("Reservation with ID " + reservation.getReservationId() + " does not exist.");
        }
    }
    
}
