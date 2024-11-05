/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.RoomType;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jwong
 */
@Stateless
public class RoomAllocationSessionBean implements RoomAllocationSessionBeanRemote, RoomAllocationSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotelSystem-ejbPU")
    private EntityManager em;

    @EJB 
    private ExceptionReportSessionBeanLocal exceptionReportSessionBean;
    
    //Automatic Timer
    @Schedule(hour = "2", minute = "0", info = "Daily Room Allocation Timer")
    public void allocateRoomReservationsToday() {
        Date today = new Date();
        allocateRoom(today);
    }
    
    public void allocateRoom(Date date) {
        List<Reservation> reservations = getReservationForCheckInDate(date);
        
        for(Reservation reservation: reservations) {
            boolean allRoomsAllocated = true;
            
            for (ReservationRoom reservationRoom: reservation.getReservationRooms()) {
            boolean allocated = allocateRoomForReservationRoom(reservationRoom);
            if (!allocated) {
                if (reservation.getRoomType().getNextRoomType() != null) {
                    //Create Type1 of the exception report
                    exceptionReportSessionBean.createExceptionReport(new Date(), reservation, "Upgrade Available");
                } else {
                    exceptionReportSessionBean.createExceptionReport(new Date(), reservation, "No Upgrade Available");
                }
                allRoomsAllocated = false;
            }
            }
            if (!allRoomsAllocated) {
                System.out.println("Reservation ID " + reservation.getReservationId() + " could not be fully allocated.");
            }
                    
        }
    }

    private List<Reservation> getReservationForCheckInDate(Date date) {
        em.find(Reservation.class, date);
        Query query = em.createQuery("SELECT r from Reservation r WHERE r.reservationDate = :date && r.isAllocated = false");
        query.setParameter("date", date);
        query.getResultList();      
        
    }
    
    private boolean allocateRoomForReservationRoom(ReservationRoom reservationRoom) {
        RoomType roomType = reservationRoom.getRoomType();
        List<Room> availableRoom = findAvailableRoom(roomType);
        
        if (!availableRooms.isEmpty()) {
            Room room = availableRooms.get(0);
            room.setOccupied(true); // Mark room as occupied
            reservationRoom.setRoom(room); // Link room to reservationRoom

            em.merge(room);
            em.merge(reservationRoom);
            return true;
        }
        return false;
    }

    
    //method to see if there are available rooms       
    private List<Room> findAvailableRoomsByType(RoomType roomType) {
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.roomType = :roomType AND r.isOccupied = FALSE");
        query.setParameter("roomType", roomType);
        return query.getResultList();
    }
        
    
    
    
}
