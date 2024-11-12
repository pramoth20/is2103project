/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Local;
import util.exception.PartnerExistsException;

/**
 *
 * @author pramoth
 */
@Local
public interface PartnerSessionBeanLocal {

    public Long createPartner(Partner partner) throws PartnerExistsException;
    public List<Partner> retrieveAllPartners();
    
}
