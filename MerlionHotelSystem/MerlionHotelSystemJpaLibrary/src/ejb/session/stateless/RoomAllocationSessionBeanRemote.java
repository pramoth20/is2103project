/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.RoomType;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exception.RoomAllocationException;
import util.exception.RoomUnavailableException;

/**
 *
 * @author jwong
 */
@Remote
public interface RoomAllocationSessionBeanRemote {
    public List<RoomType> findAvailableRoomTypes(Date checkInDate, Date checkOutDate, int numOfRooms) throws RoomUnavailableException;
    
     public void allocateRoomImmediately(Reservation reservation, RoomType roomType) throws RoomAllocationException;

     public void allocateRoom(Date date, RoomType roomType) throws RoomAllocationException;
     
     public void allocateRoomReservationsToday(RoomType roomType) throws RoomAllocationException;
     
    
}
