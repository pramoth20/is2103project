/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;
import entity.Customer;
import entity.Reservation;
import entity.ReservationRoom;
import entity.RoomType;
import enums.ReservationType;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import util.exception.NoApplicableRateException;
import util.exception.ReservationNotFoundException;
import util.exception.RoomRateNotFoundException;

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
    
    @EJB
    private RoomAllocationSessionBeanLocal roomAllocationSessionBean;

    @EJB
    private RoomRateSessionBeanLocal rateSessionBean;
    
    @EJB
    private RoomRateSessionBeanLocal roomRateSessionBean;

   
    /*public Long createNewReservation(Reservation reservation) throws GeneralException {
        try {
            em.persist(reservation);
            em.flush(); // To ensure ID is generated and available immediately
            return reservation.getReservationId();
        } catch (PersistenceException ex) {
            throw new GeneralException("An unexpected error occurred while creating the reservation: " + ex.getMessage());
        }
    } 
        return reservation.getReservationId();
} */
    
    //For online reservations
    @Override
   public Long createReservationForOnline(Customer customer, RoomType roomType, Date checkInDate, Date checkOutDate, int numberOfRooms) {
    // Calculate total cost for the reservation
    BigDecimal totalCost = calculateTotalCostForOnlineReservation(roomType, checkInDate, checkOutDate);

    // Create the Reservation
    Reservation reservation = new Reservation();
    reservation.setCustomer(customer);
    reservation.setCheckInDate(checkInDate);
    reservation.setCheckOutDate(checkOutDate);
    reservation.setReservationDate(new Date());
    reservation.setTotalCost(totalCost);
    reservation.setIsAllocated(true); 
    reservation.setReservationType(ReservationType.ONLINE);

    // Create and add ReservationRoom entities
    for (int i = 0; i < numberOfRooms; i++) {
        ReservationRoom reservationRoom = new ReservationRoom();
        reservation.getReservationRooms().add(reservationRoom);
    }

    em.persist(reservation);
    em.flush(); 

    return reservation.getReservationId();
   }
    
   
   //Walk in reservations
    @Override
    public Long createWalkInReservation(RoomType roomType, Date checkInDate, Date checkOutDate, int numberOfRooms) throws RoomRateNotFoundException {
    //Calculate the total cost of the reservation
    BigDecimal totalCost = calculateTotalCostForWalkInReservation(roomType, checkInDate, checkOutDate);

    //  Create the Reservation
    Reservation reservation = new Reservation();
    reservation.setCheckInDate(checkInDate);
    reservation.setCheckOutDate(checkOutDate);
    reservation.setReservationDate(new Date());
    reservation.setTotalCost(totalCost);
    reservation.setIsAllocated(false); // Allocation status
    reservation.setReservationType(ReservationType.WALK_IN);


    // Step 4: Create ReservationRoom instances and link to the Reservation
    for (int i = 0; i < numberOfRooms; i++) {
        ReservationRoom reservationRoom = new ReservationRoom();
        reservation.getReservationRooms().add(reservationRoom);
    }

    em.persist(reservation);
    em.flush();

    //System.out.println("Walk-in reservation created successfully with ID: " + reservation.getReservationId());
    return reservation.getReservationId();
}

     
    
    //Use case 5 for guest
    @Override
    public Reservation findReservation(Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = em.find(Reservation.class, reservationId);
        if (reservation == null) {
            throw new ReservationNotFoundException("Reservation ID " + reservationId + " does not exist.");
        }
        return reservation;
    }
    
    //Reservation list for partner
    @Override
    public List<Reservation> getAllReservationsForPartner(Long partnerId) {
        TypedQuery<Reservation> query = em.createQuery("SELECT r FROM Reservation r WHERE r.partner.id = :partnerId", Reservation.class);
        query.setParameter("partnerId", partnerId);
        return query.getResultList();
    }

    
    //For use case 6 for guest
    @Override
    public List<Reservation> getAllReservationsForGuest(Long guestId) {
        TypedQuery<Reservation> query = em.createQuery("SELECT r FROM Reservation r WHERE r.guest.id = :guestId", Reservation.class);
        query.setParameter("guestId", guestId);
        return query.getResultList();
    }
    
    //Calculating the amount of online reservation - Use case 23
    @Override
    public BigDecimal calculateTotalCostForOnlineReservation(RoomType roomType, Date checkInDate, Date checkOutDate) {
        BigDecimal totalCost = BigDecimal.ZERO;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkInDate);

        // Loop from check-in date to check-out date
        /*(while (!calendar.getTime().after(checkOutDate)) {
            Date currentDate = calendar.getTime();

            // Get the prevailing rate for this day from the RoomRateSessionBean
            BigDecimal dailyRate = roomRateSessionBean.getReservationRate(roomType, currentDate);

            // Add the daily rate to the total cost
            totalCost = totalCost.add(dailyRate);

            // Move to the next day
            calendar.add(Calendar.DATE, 1);
        }*/
        while (!calendar.getTime().after(checkOutDate)) {
            Date currentDate = calendar.getTime();

            try {
                // Get the prevailing rate for this day from the RoomRateSessionBean
                BigDecimal dailyRate = roomRateSessionBean.getReservationRate(roomType, currentDate);
                // Add the daily rate to the total cost
                totalCost = totalCost.add(dailyRate);
            } catch (NoApplicableRateException e) {
                // Handle the exception (e.g., log it, skip this date, or set a default rate)
                System.err.println("No applicable rate found for " + currentDate + ": " + e.getMessage());
                // Optionally, you could set a default rate or skip this day in the cost calculation.
            }

            // Move to the next day
            calendar.add(Calendar.DATE, 1);
        }

        return totalCost;
    }
    
    @Override
    public BigDecimal calculateTotalCostForWalkInReservation(RoomType roomType, Date checkInDate, Date checkOutDate) throws RoomRateNotFoundException {
    BigDecimal totalCost = BigDecimal.ZERO;

    BigDecimal publishedRate = roomRateSessionBean.getWalkInRate(roomType);

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(checkInDate);

    // Loop from check-in date to check-out date (inclusive)
    while (!calendar.getTime().after(checkOutDate)) {
        // For walk-in reservations, use the same published rate for each day
        totalCost = totalCost.add(publishedRate);

        // Move to the next day
        calendar.add(Calendar.DATE, 1);
    }

    return totalCost;
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
