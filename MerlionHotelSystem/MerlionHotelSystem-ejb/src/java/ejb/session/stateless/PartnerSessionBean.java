package ejb.session.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InvalidPasswordException;
import util.exception.PartnerExistsException;
import util.exception.PartnerNotFoundException;

@Stateless
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotelSystem-ejbPU")
    private EntityManager em;

    // Use Case 5: Create New Partner

    /*public Long createPartner(Partner partner) {
            em.persist(partner);
            em.flush(); 
            return partner.getPartnerId();
    }*/
    
    @Override
    public Partner login(String email, String password) throws PartnerNotFoundException, InvalidPasswordException {
        try {
            // Query to find partner by email and password
            Query query = em.createQuery("SELECT p FROM Partner p WHERE p.email = :email AND p.password = :password");
            query.setParameter("email", email);
            query.setParameter("password", password);
            Partner partner = (Partner) query.getSingleResult();
            // Retrieve the single result (Partner entity) if credentials are correct
            if (!partner.getPassword().equals(password)) {
                throw new InvalidPasswordException("Invalid password.");
            }
            
            return partner;

        } catch (NoResultException ex) {
            // If no partner with the given email is found, throw PartnerNotFoundException
            throw new PartnerNotFoundException("Partner with email " + email + " not found.");
        }

        
    }
    
    

    @Override
    public Long createPartner(Partner partner) throws PartnerExistsException {
        // Check if a Partner with the same companyName or email already exists
        if (checkPartnerExists(partner.getCompanyName(), partner.getEmail())) {
            throw new PartnerExistsException("A partner with the same company name or email already exists.");
        }

        em.persist(partner);
        em.flush(); // Ensures the ID is generated immediately
        return partner.getPartnerId();
    }
    
    // Helper method to check if a partner with the same companyName or email already exists
    private boolean checkPartnerExists(String companyName, String email) {
        Query query = em.createQuery("SELECT p FROM Partner p WHERE p.companyName = :companyName OR p.email = :email");
        query.setParameter("companyName", companyName);
        query.setParameter("email", email);

        try {
            query.getSingleResult();
            return true; // If a result is found, a partner with the same companyName or email exists
        } catch (NoResultException ex) {
            return false; // No result means the partner with the given companyName or email doesn't exist
        }
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