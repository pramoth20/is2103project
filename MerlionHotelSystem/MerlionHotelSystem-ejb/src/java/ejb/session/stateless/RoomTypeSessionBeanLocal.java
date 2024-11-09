/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomType;
import javax.ejb.Local;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author jwong
 */
@Local
public interface RoomTypeSessionBeanLocal {

    public RoomType getRoomTypeDetails(Long roomTypeId) throws RoomTypeNotFoundException;
    
}
