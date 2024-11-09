/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 *
 * @author pramoth
 */
@Entity
public class Guest extends Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Column(nullable = false, length = 20)
    protected String guestId;
    
    public Guest() {
        super();
    }

    public Guest(String email, String firstName, String lastName, String phoneNumber, String identificationNumber) {
        super(email, firstName, lastName, phoneNumber);
        this.guestId = identificationNumber;
    }

    // Getters and Setters
    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getCustomerId() != null ? getCustomerId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customerId fields are not set
        if (!(object instanceof Guest)) {
            return false;
        }
        Guest other = (Guest) object;
        if ((this.getCustomerId() == null && other.getCustomerId() != null) || (this.getCustomerId() != null && !this.getCustomerId().equals(other.getCustomerId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Guest[ id=" + getCustomerId() + " ]";
    }
    
}
