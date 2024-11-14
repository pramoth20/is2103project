/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.ExceptionReport;
import entity.Reservation;
import entity.ReservationRoom;
import entity.Room;
import entity.RoomType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.RoomAllocationException;
import util.exception.RoomUnavailableException;

/**
 *
 * @author jwong
 */
@Stateless
public class RoomAllocationSessionBean implements RoomAllocationSessionBeanRemote, RoomAllocationSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotelSystem-ejbPU")
    private EntityManager em;

    @EJB
    private RoomRateSessionBeanLocal roomRateSessionBean;
    
    //Automatic Timer
    @Schedule(hour = "2", minute = "0", info = "Daily Room Allocation Timer")
    @Override
    public void allocateRoomReservationsToday() throws RoomAllocationException {
        Date today = new Date();
        allocateRoom(today);
    }
    

    /*public void allocateRoom(Date date) throws RoomAllocationException {

        List<Reservation> reservations = getReservationForCheckInDate(date);
 
        
        for(Reservation reservation: reservations) {
            boolean allRoomsAllocated = true;
            
            for (ReservationRoom reservationRoom: reservation.getReservationRooms()) {
            boolean allocated = allocateRoomForReservationRoom(reservationRoom);
            if (!allocated) {
                RoomType nextRoomType = reservationRoom.getRoom().getRoomType().getNextRoomType();
                if (nextRoomType != null && !findAvailableRoomsByType(nextRoomType).isEmpty()) {
                    //Create Type1 of the exception report
                    createExceptionReportForUpgradeAvailable(reservationRoom);
                } else {
                    createExceptionReportForNoUpgradeAvailable(reservationRoom);                }
                allRoomsAllocated = false;
            }
            }
            if (!allRoomsAllocated) {
                System.out.println("Reservation ID " + reservation.getReservationId() + " could not be fully allocated.");
            }
                    
        }
        
    }*/
    
    //Allocate immediately 
    @Override
    public void allocateRoomImmediately(Reservation reservation) throws RoomAllocationException {
        boolean allRoomsAllocated = true;
        Date checkInDate = reservation.getCheckInDate();
        Date checkOutDate = reservation.getCheckOutDate();
        RoomType roomType = reservation.getRoomType();
        

        for (ReservationRoom reservationRoom : reservation.getReservationRooms()) {
            try {
                allocateRoomForReservationRoom(reservationRoom, checkInDate, checkOutDate, roomType);
            } catch (RoomUnavailableException ex) {
                // Handle the exception by trying to allocate a higher room type if available
                RoomType nextRoomType = reservationRoom.getRoom().getRoomType().getNextRoomType();
                try {
                    if (nextRoomType != null && !findAvailableRoomsForPeriod(nextRoomType, checkInDate, checkOutDate).isEmpty()) {
                        // Exception report: Room not available, but upgrade possible
                        createExceptionReportForUpgradeAvailable(reservationRoom);
                    } else {
                        // Exception report: No room available, and no upgrade option
                        createExceptionReportForNoUpgradeAvailable(reservationRoom);
                        allRoomsAllocated = false; // Mark this reservation as not fully allocated
                    }
                } catch (RoomUnavailableException ex1) {
                    
                }
            }
        }

        if (!allRoomsAllocated) {
            throw new RoomAllocationException("Reservation ID " + reservation.getReservationId() + " could not be fully allocated.");
        }
    }


    
    @Override
    public void allocateRoom(Date date) throws RoomAllocationException {
    List<Reservation> reservations = getReservationForCheckInDate(date);
    boolean allocationFailed = false;  // Track if any reservation couldn't be fully allocated

    for (Reservation reservation : reservations) {
        boolean allRoomsAllocated = true;
        Date checkInDate = reservation.getCheckInDate();
        Date checkOutDate = reservation.getCheckOutDate();
        RoomType roomType = reservation.getRoomType();

        for (ReservationRoom reservationRoom : reservation.getReservationRooms()) {
            try {
                // Try to allocate a room for this reservation room
                allocateRoomForReservationRoom(reservationRoom, checkInDate, checkOutDate, roomType);
                //allocateRoomForReservationRoom(reservationRoom);
            } catch (RoomUnavailableException ex) {
                // Handle the exception by trying to allocate a higher room type if available
                RoomType nextRoomType = reservationRoom.getRoom().getRoomType().getNextRoomType();

                try {
                    if (nextRoomType != null && !findAvailableRoomsForPeriod(nextRoomType, checkInDate, checkOutDate).isEmpty()) {
                        // Exception report: Room not available, but upgrade possible
                        createExceptionReportForUpgradeAvailable(reservationRoom);
                    } else {
                        // Exception report: No room available, and no upgrade option
                        createExceptionReportForNoUpgradeAvailable(reservationRoom);
                        allRoomsAllocated = false;  // Mark this reservation as not fully allocated
                    }
                } catch (RoomUnavailableException ex1) {
                    
                }
            } 
          
        }

        // If not all rooms were allocated, mark the reservation as partially allocated
        if (!allRoomsAllocated) {
            allocationFailed = true;
            System.out.println("Reservation ID " + reservation.getReservationId() + " could not be fully allocated.");
        }
    }
    

    // If any reservations failed allocation, throw a RoomAllocationException
    if (allocationFailed) {
        throw new RoomAllocationException("Some reservations could not be fully allocated for the date: " + date);
    }
}
    
    
    private List<Reservation> getReservationForCheckInDate(Date date) {
        //em.find(Reservation.class, date);
        Query query = em.createQuery("SELECT r from Reservation r WHERE r.reservationDate = :date AND r.isAllocated = false", Reservation.class);
        query.setParameter("date", date);
        return query.getResultList();      
        
    }
    
    private boolean allocateRoomForReservationRoom(ReservationRoom reservationRoom, Date checkInDate, Date checkOutDate, RoomType roomType) throws RoomUnavailableException {
        //RoomType roomType = reservationRoom.getRoom().getRoomType();
        //List<Room> availableRooms = findAvailableRoomsByType(roomType);
        
        List<Room> availableRooms = findAvailableRoomsForPeriod(roomType, checkInDate, checkOutDate);
        
        if (availableRooms.isEmpty()) {
        // If no rooms are available, throw an exception
        throw new RoomUnavailableException("No available rooms for RoomType: " + roomType.getName());
        }
        
       
        Room room = availableRooms.get(0);
        room.setIsAvailable(false);
            //room.setOccupied(true); // Mark room as occupied
        reservationRoom.setRoom(room); // Link room to reservationRoom

        em.merge(room);
        em.merge(reservationRoom);
        return true;
       
    }

    
    //method to see if there are available rooms       
    /*private List<Room> findAvailableRoomsByType(RoomType roomType) {
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.roomType = :roomType AND r.isAvailable = TRUE");
        query.setParameter("roomType", roomType);
        return query.getResultList();
    }*/
    
    private List<Room> findAvailableRoomsForPeriod(RoomType roomType, Date checkInDate, Date checkOutDate) throws RoomUnavailableException {
        List<Room> availableRooms = new ArrayList<>();
    try {
        //  Get all reservations that are reserved for any part of the duration of the stay
        Query reservationQuery = em.createQuery(
                "SELECT DISTINCT res FROM Reservation res " +
                        "JOIN res.reservationRooms rr "                      
                + "WHERE rr.room.roomType = :roomType " 
                + "AND res.checkInDate < :checkOutDate "
                + "AND res.checkOutDate > :checkInDate"
        );
        reservationQuery.setParameter("roomType", roomType);
        reservationQuery.setParameter("checkInDate", checkInDate);
        reservationQuery.setParameter("checkOutDate", checkOutDate);

        List<Reservation> overlappingReservations = reservationQuery.getResultList();

        // If no overlapping reservations are found, simply return all available rooms
        if (overlappingReservations == null || overlappingReservations.isEmpty()) {
            Query roomQuery = em.createQuery(
                    "SELECT r FROM Room r "
                    + "WHERE r.roomType = :roomType "
                    + "AND r.isAvailable = TRUE"
            );
            roomQuery.setParameter("roomType", roomType);
            availableRooms = roomQuery.getResultList();
            return availableRooms;
        }

        //Collect all reserved room IDs from the overlapping reservations
        List<Long> reservedRoomIds = new ArrayList<>();
        for (Reservation reservation : overlappingReservations) {
            for (ReservationRoom reservationRoom : reservation.getReservationRooms()) {
                reservedRoomIds.add(reservationRoom.getRoom().getRoomId());
            }
        }

        // Step 4: Retrieve available rooms of the specified type that are not reserved for the period
        Query roomQuery = em.createQuery(
                "SELECT r FROM Room r "
                + "WHERE r.roomType = :roomType "
                + "AND r.isAvailable = TRUE "
                + "AND r.roomId NOT IN :reservedRoomIds"
        );
        roomQuery.setParameter("roomType", roomType);
        roomQuery.setParameter("reservedRoomIds", reservedRoomIds);

        availableRooms = roomQuery.getResultList();

        // If no available rooms found, throw the custom exception
        if (availableRooms == null || availableRooms.isEmpty()) {
            throw new RoomUnavailableException("No available rooms for the selected period.");
        }

    } catch (Exception e) {
        // Log the error message and stack trace for debugging
        System.out.println("Error while searching for available rooms: " + e.getMessage());
        e.printStackTrace();
        // Optionally, rethrow the exception if you want it to propagate further
        // throw e;
    }

    return availableRooms;
}
        
    
    
    //For use case 3, finding available RoomTypes, need to find avaialble rooms across all available types
    @Override
    public List<RoomType> findAvailableRoomTypes(Date checkInDate, Date checkOutDate, int numOfRooms) throws RoomUnavailableException {
        List<RoomType> availableRoomTypes = new ArrayList<>();

        // Fetch all room types
        List<RoomType> allRoomTypes = em.createQuery("SELECT rt FROM RoomType rt", RoomType.class).getResultList();

        // For each room type, check if there is at least one available room for the entire period
        for (RoomType roomType : allRoomTypes) {
        List<Room> availableRooms = findAvailableRoomsForPeriod(roomType, checkInDate, checkOutDate);

        if (availableRooms.size() >= numOfRooms) {
            availableRoomTypes.add(roomType);
        }
    }

        return availableRoomTypes;
    }
    
    
    
        
    //Use case 16 - Generating 1st type of exception report(no available room but can upgrade)
    private void createExceptionReportForUpgradeAvailable(ReservationRoom reservationRoom) {
    ExceptionReport exceptionReport = new ExceptionReport(new Date(), false, null, "Upgrade Available");
    reservationRoom.getExceptionReports().add(exceptionReport);
    em.persist(exceptionReport);
    ;
}
    // Use case 16 - Generate second type of exception report (No room and no upgrade available)
    private void createExceptionReportForNoUpgradeAvailable(ReservationRoom reservationRoom) {
        ExceptionReport exceptionReport = new ExceptionReport(new Date(), false, null, "No Upgrade Available");
        reservationRoom.getExceptionReports().add(exceptionReport);
        em.persist(exceptionReport);
    }
    
    
    
}
