/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import entity.RoomType;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jwong
 */
@Singleton
@LocalBean
@Startup
public class RoomTypeInitialisationService {

    @PersistenceContext(unitName = "MerlionHotelSystem-ejbPU")
    private EntityManager em;

    @PostConstruct
    public void initialiseRoomTypes() {
        try {
            // Retrieve existing RoomType objects from the database
            RoomType deluxe = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = :name", RoomType.class)
                                .setParameter("name", "Deluxe Room")
                                .getSingleResult();
            RoomType premier = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = :name", RoomType.class)
                                 .setParameter("name", "Premier Room")
                                 .getSingleResult();
            RoomType family = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = :name", RoomType.class)
                                .setParameter("name", "Family Room")
                                .getSingleResult();
            RoomType juniorSuite = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = :name", RoomType.class)
                                     .setParameter("name", "Junior Suite")
                                     .getSingleResult();
            RoomType grandSuite = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = :name", RoomType.class)
                                    .setParameter("name", "Grand Suite")
                                    .getSingleResult();

            // Set the upgrade path for each RoomType
            deluxe.setNextRoomType(premier);
            premier.setNextRoomType(family);
            family.setNextRoomType(juniorSuite);
            juniorSuite.setNextRoomType(grandSuite);
            grandSuite.setNextRoomType(grandSuite);

            // Merge the changes back to the persistence context
            em.merge(deluxe);
            em.merge(premier);
            em.merge(family);
            em.merge(juniorSuite);
            em.merge(grandSuite);

            System.out.println("Room types initialized successfully.");

        } catch (Exception e) {
            System.err.println("Failed to initialize room types: " + e.getMessage());
            // Handle exception or log error as needed
        }
    }
   
   
}
