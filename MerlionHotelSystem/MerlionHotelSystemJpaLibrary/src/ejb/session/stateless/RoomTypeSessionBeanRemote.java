/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomType;
import javax.ejb.Remote;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author jwong
 */
@Remote
public interface RoomTypeSessionBeanRemote {
    public RoomType getRoomTypeDetails(Long roomTypeId) throws RoomTypeNotFoundException;
}
