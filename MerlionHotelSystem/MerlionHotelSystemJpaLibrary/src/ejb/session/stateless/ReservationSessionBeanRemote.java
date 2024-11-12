/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.RoomType;
import javax.ejb.Remote;

/**
 *
 * @author jwong
 */
@Remote
public interface ReservationSessionBeanRemote {
    
    public Long createNewReservation(Reservation reservation);

    public void registerReservation(String roomNumber, boolean status, RoomType roomType);

    public Reservation findReservation(Long reservationId);
    
    public void updateReservation(Reservation reservation);
    
}
