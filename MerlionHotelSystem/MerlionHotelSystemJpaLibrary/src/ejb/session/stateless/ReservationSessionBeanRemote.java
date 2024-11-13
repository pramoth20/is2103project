/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Reservation;
import entity.RoomType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exception.ReservationNotFoundException;
import util.exception.RoomRateNotFoundException;

/**
 *
 * @author jwong
 */
@Remote
public interface ReservationSessionBeanRemote {
    public Reservation findReservation(Long reservationId) throws ReservationNotFoundException;
    
    public List<Reservation> getAllReservationsForPartner(Long partnerId);
    
    public List<Reservation> getAllReservationsForGuest(Long guestId);

    public BigDecimal calculateTotalCostForWalkInReservation(RoomType roomType, Date checkInDate, Date checkOutDate, int numberOfRooms) throws RoomRateNotFoundException;
    
    public Long createReservationForOnline(Customer customer, RoomType roomType, Date checkInDate, Date checkOutDate, int numberOfRooms);
    
    public Long createWalkInReservation(RoomType roomType, Date checkInDate, Date checkOutDate, int numberOfRooms) throws RoomRateNotFoundException;
    
    public void updateReservation(Reservation reservation);
    
    public BigDecimal calculateTotalCostForOnlineReservation(RoomType roomType, Date checkInDate, Date checkOutDate, int numberOfRooms);

}
