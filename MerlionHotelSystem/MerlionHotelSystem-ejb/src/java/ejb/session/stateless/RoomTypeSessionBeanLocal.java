/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomType;
import java.util.List;
import javax.ejb.Local;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author jwong
 */
@Local
public interface RoomTypeSessionBeanLocal {
    
    public RoomType createRoomType(String name);
    public RoomType getRoomTypeDetails(Long roomTypeId) throws RoomTypeNotFoundException;
    public void deleteRoomType(Long roomTypeId) throws RoomTypeNotFoundException;
    public List<RoomType> retrieveAllRoomTypes();
    public RoomType getNextRoomType(RoomType current);
    public RoomType getRoomTypeDetailsByName(String roomTypeName) throws RoomTypeNotFoundException;

    public RoomType updateRoomType(Long roomTypeId, String name) throws RoomTypeNotFoundException;
    
}
