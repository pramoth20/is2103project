/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;


import entity.RoomType;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.RoomAllocationException;

/**
 *
 * @author jwong
 */
@Local
public interface RoomAllocationSessionBeanLocal {


    public void allocateRoomReservationsToday() throws RoomAllocationException;

    public List<RoomType> findAvailableRoomTypes(Date checkInDate, Date checkOutDate, int numOfRooms);

    public void allocateRoom(Date date) throws RoomAllocationException;

    
}
