/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Rate;
import entity.RoomType;
import enums.RateType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.RoomRateNotFoundException;

/**
 *
 * @author jwong
 */
@Stateless
public class RoomRateSessionBean implements RoomRateSessionBeanRemote, RoomRateSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotelSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
       
    //create room rate
    public Rate createEmployee(String name, RoomType roomType, RateType rateType, BigDecimal ratePerNight, Date startDate, Date endDate) {
        Rate roomRate = new Rate(name, roomType, rateType, ratePerNight, startDate, endDate);
        em.persist(roomRate);
        em.flush();
        return roomRate;
    }  
    
    //View Room Details
    public Rate viewRoomRateDetails(Long rateId) throws RoomRateNotFoundException {
       Rate roomRate = em.find(Rate.class, rateId);
       if (roomRate == null) {
           throw new RoomRateNotFoundException("Room Rate of Id " + rateId + " cannnot be found");
       } 
       return roomRate;
    }
    
    public Rate updateRoomRateDetails(Long rateId, String name, RoomType roomType, RateType rateType,BigDecimal ratePerNight, Date startDate, Date endDate ) throws RoomRateNotFoundException {
        Rate rate = em.find(Rate.class, rateId);
    if (rate == null) {
        throw new RoomRateNotFoundException("Rate ID " + rateId + " not found.");
    }
    if (name != null) {
        rate.setName(name);
    }
    if (roomType != null) {
        rate.setRoomType(roomType);
    }
    if (rateType != null) {
        rate.setRateType(rateType);
    }
    if (ratePerNight != null){
        rate.setRatePerNight(ratePerNight);
    }
    if (startDate != null) {
        rate.setStartDate(startDate);
    }
    if (endDate != null) {
        rate.setEndDate(endDate);
    }
  
    em.merge(rate);
    return rate;
}   
    //Delete RoomRate details
    public void deleteRoomRate(Long rateId) throws RoomRateNotFoundException {
        Rate roomRate = em.find(Rate.class, rateId);
       if (roomRate == null) {
           throw new RoomRateNotFoundException("Room Rate of Id " + rateId + " cannnot be found");
       } 
       if (isRateInUse(roomRate)) {
            roomRate.setIsDisabled(true);  // Mark as disabled if it's in use
            em.merge(roomRate); // Update to save disabled status
        } else {
            em.remove(roomRate);  // Delete if not in use
        }
    }
       
    private boolean isRateInUse(Rate rate) {
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.rate = :rate");
        query.setParameter("rate", rate);
        return !query.getResultList().isEmpty();
    }
    
    //View All Room Rates
    public List<Rate> retrieveAllEmployees() {
    Query query = em.createQuery("SELECT r FROM Rate r");
    return query.getResultList();
    }

   
    
}
