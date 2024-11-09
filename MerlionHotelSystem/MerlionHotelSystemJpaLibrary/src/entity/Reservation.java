/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jwong
 */
@Entity
public class Reservation implements Serializable {

    /**
     * @return the roomNumber
     */

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    
    private String roomNumber;
    private boolean isAllocated;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date reservationDate;
    
    @ManyToMany
    @JoinTable(
        name = "Reservation_Rate",
        joinColumns = @JoinColumn(name = "reservation_id"),
        inverseJoinColumns = @JoinColumn(name = "rate_id")
    )
    private List<Rate> rates = new ArrayList<>();
    
    //Just added -> haven't put the constructor yet
    private String partnerReferenceNumber;
   

    public Reservation() {
    }

    public Reservation(String roomNumber, boolean status) {
        this.roomNumber = roomNumber;
        this.isAllocated = status;
     
    }

    public Reservation(String roomNumber, boolean isAllocated, Date reservationDate, String partnerReferenceNumber) {
        this.roomNumber = roomNumber;
        this.isAllocated = isAllocated;
        this.reservationDate = reservationDate;
        this.partnerReferenceNumber = partnerReferenceNumber;
    }
    
    
    

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }

    /**
     * @param roomNumber the roomNumber to set
     */
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    /**
     * @return the isAllocated
     */
    public boolean isIsAllocated() {
        return isAllocated;
    }

    /**
     * @param isAllocated the isAllocated to set
     */
    public void setIsAllocated(boolean isAllocated) {
        this.isAllocated = isAllocated;
    }

    /**
     * @return the roomType
     */
  
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getReservationId() != null ? getReservationId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservationId fields are not set
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.getReservationId() == null && other.getReservationId() != null) || (this.getReservationId() != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Reservation[ id=" + getReservationId() + " ]";
    }
    
}
