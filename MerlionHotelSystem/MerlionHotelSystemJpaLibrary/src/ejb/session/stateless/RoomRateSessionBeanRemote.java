/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Rate;
import entity.RoomType;
import enums.RateType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exception.InputDataValidationException;
import util.exception.NoApplicableRateException;
import util.exception.RoomRateNotFoundException;
import util.exception.UpdateRoomRateException;

/**
 *
 * @author jwong
 */
@Remote
public interface RoomRateSessionBeanRemote {
    public Rate createRate(Rate rate);
    
    public Rate viewRoomRateDetails(Long rateId) throws RoomRateNotFoundException;
    
    public void deleteRoomRate(Long rateId) throws RoomRateNotFoundException;
    
    public List<Rate> retrieveAllRoomRates();
    
    public Rate updateRoomRateDetails(Rate updatedRate) throws RoomRateNotFoundException, UpdateRoomRateException, InputDataValidationException;
    
    public BigDecimal getReservationRate(RoomType roomType, Date date) throws NoApplicableRateException;
    
    public BigDecimal getWalkInRate(RoomType roomType) throws RoomRateNotFoundException;
}
