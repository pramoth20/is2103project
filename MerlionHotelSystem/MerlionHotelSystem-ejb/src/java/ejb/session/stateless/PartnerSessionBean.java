package ejb.session.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.PersistenceException;

@Stateless
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotelSystem-ejbPU")
    private EntityManager em;

    // Use Case 5: Create New Partner
    @Override
    public Long createPartner(Partner partner) {
            em.persist(partner);
            em.flush(); 
            return partner.getPartnerId();
    }

    // Use Case 6: View All Partners
    @Override
    public List<Partner> retrieveAllPartners() {
        Query query = em.createQuery("SELECT p FROM Partner p");
        List<Partner> partners = query.getResultList();

        if (partners.isEmpty()) {
            System.out.println("No partner records available in the system.");
        }
        return partners;
    }
}