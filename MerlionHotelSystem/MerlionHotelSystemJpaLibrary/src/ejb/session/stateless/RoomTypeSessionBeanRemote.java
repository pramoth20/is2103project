/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomType;
import java.util.List;
import javax.ejb.Remote;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author jwong
 */
@Remote
public interface RoomTypeSessionBeanRemote {
    
    public RoomType createRoomType(String name, String details);
    public RoomType getRoomTypeDetails(Long roomTypeId) throws RoomTypeNotFoundException;
    public RoomType updateRoomType(Long roomTypeId, String name, String details) throws RoomTypeNotFoundException;
    public void deleteRoomType(Long roomTypeId) throws RoomTypeNotFoundException;
    public List<RoomType> retrieveAllRoomTypes();
    public RoomType getNextRoomType(RoomType current);
    public RoomType getRoomTypeDetailsByName(String roomTypeName) throws RoomTypeNotFoundException;
    
}
