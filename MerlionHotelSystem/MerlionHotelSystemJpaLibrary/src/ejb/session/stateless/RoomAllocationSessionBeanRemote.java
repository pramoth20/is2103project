/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomType;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exception.RoomAllocationException;

/**
 *
 * @author jwong
 */
@Remote
public interface RoomAllocationSessionBeanRemote {
    public List<RoomType> findAvailableRoomTypes(Date checkInDate, Date checkOutDate, int numOfRooms);
    public void allocateRoomReservationsToday() throws RoomAllocationException;
    public void allocateRoom(Date date) throws RoomAllocationException;
    
}
