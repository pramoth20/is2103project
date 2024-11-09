/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomType;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jwong
 */
@Stateless
public class RoomTypeInitializationService implements RoomTypeInitializationServiceLocal {

    @PersistenceContext(unitName = "MerlionHotelSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public void initialiseRoomTypes() {
        RoomType deluxe = new RoomType("Deluxe Room");
        RoomType premier = new RoomType("Premier Room");
        RoomType family = new RoomType("Family Room");
        RoomType juniorSuite = new RoomType("Junior Suite");
        RoomType grandSuite = new RoomType("Grand Suite");
                
        deluxe.setNextRoomType(premier);
        premier.setNextRoomType(family);
        family.setNextRoomType(juniorSuite);
        juniorSuite.setNextRoomType(grandSuite);
        
        em.persist(deluxe);
        em.persist(premier);
        em.persist(family);
        em.persist(juniorSuite);
        em.persist(grandSuite);
    }
    
    
}
