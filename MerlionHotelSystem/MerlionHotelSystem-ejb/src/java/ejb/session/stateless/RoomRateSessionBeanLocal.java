/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Rate;
import entity.RoomType;
import enums.RateType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.InputDataValidationException;
import util.exception.NoApplicableRateException;
import util.exception.RoomRateNotFoundException;
import util.exception.UpdateRoomRateException;


public interface RoomRateSessionBeanLocal {

    public Rate createRate(Rate rate);

    public Rate viewRoomRateDetails(Long rateId) throws RoomRateNotFoundException;

    public void deleteRoomRate(Long rateId) throws RoomRateNotFoundException;

    public List<Rate> retrieveAllRoomRates();

    public BigDecimal getReservationRate(RoomType roomType, Date date) throws NoApplicableRateException;

    public BigDecimal getWalkInRate(RoomType roomType) throws RoomRateNotFoundException;

    public Rate updateRoomRateDetails(Rate updatedRate) throws RoomRateNotFoundException, UpdateRoomRateException, InputDataValidationException;

    
    
    // Use Case 17: Create New Room Rate
    public Rate createRoomRate(String name, RoomType roomType, RateType rateType, BigDecimal ratePerNight, Date startDate, Date endDate);
    
    // Use Case 18: View Room Rate Details
    public Rate viewRoomRateDetails(Long rateId) throws RoomRateNotFoundException;
    
    // Use Case 19: Update Room Rate
    public Rate updateRoomRateDetails(Long rateId, String name, RoomType roomType, RateType rateType, BigDecimal ratePerNight, Date startDate, Date endDate) throws RoomRateNotFoundException;
    
    // Use Case 20: Delete Room Rate
    public void deleteRoomRate(Long rateId) throws RoomRateNotFoundException;
    
    // Use Case 21: View All Room Rates
    public List<Rate> retrieveAllRoomRates();
    
    // Get Published Rate for Room Type
    public BigDecimal getPublishedRateForRoomType(RoomType roomType) throws RoomRateNotFoundException;
}
