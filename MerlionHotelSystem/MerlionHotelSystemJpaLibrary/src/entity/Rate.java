/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import enums.RateType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jwong
 */
@Entity
public class Rate implements Serializable {


    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rateId;

    @Column(nullable = false, unique = true, length = 64)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private RoomType roomType;  // Association with RoomType entity

    @Enumerated(EnumType.STRING)
    private RateType rateType;  // Published, Normal, Peak, Promotion

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal ratePerNight;

    @Temporal(TemporalType.DATE)
    private Date startDate;  // Validity period start (for peak/promotion rates)

    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column(nullable = false)
    private boolean isDisabled = false;
    
    @Column(nullable = false)
    private Boolean isAvailable = true;

    @ManyToMany
    @JoinTable(
        name = "reservation_rate", // The join table name
        joinColumns = @JoinColumn(name = "rate_id"), // Foreign key for the rate entity
        inverseJoinColumns = @JoinColumn(name = "reservation_id") // Foreign key for the reservation entity
    )
    private List<Reservation> reservations = new ArrayList<>();

    public Rate() {
    }

    public Rate(String name, RoomType roomType, RateType rateType, BigDecimal ratePerNight, Date startDate, Date endDate) {
        this.name = name;
        this.roomType = roomType;
        this.rateType = rateType;
        this.ratePerNight = ratePerNight;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Method to check if the rate can be deleted
    public boolean canBeDeleted() {
        return reservations.isEmpty();
    }

    // Mark rate as disabled
    public void disableRate() {
        this.isDisabled = true;
    }

    // Getters and Setters
    public Long getRateId() {
        return rateId;
    }

    public void setRateId(Long rateId) {
        this.rateId = rateId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rateId != null ? rateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rateId fields are not set
        if (!(object instanceof Rate)) {
            return false;
        }
        Rate other = (Rate) object;
        if ((this.rateId == null && other.rateId != null) || (this.rateId != null && !this.rateId.equals(other.rateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Rate[ id=" + rateId + " ]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public RateType getRateType() {
        return rateType;
    }

    public void setRateType(RateType rateType) {
        this.rateType = rateType;
    }

    public BigDecimal getRatePerNight() {
        return ratePerNight;
    }

    public void setRatePerNight(BigDecimal ratePerNight) {
        this.ratePerNight = ratePerNight;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(boolean disabled) {
        this.isDisabled = disabled;
        if (disabled) {
            isAvailable = false; // If the room is disabled, it cannot be available
        }
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
    
    /**
     * @return the isAvailable
     */
    public Boolean getIsAvailable() {
        return isAvailable;
    }

    /**
     * @param isAvailable the isAvailable to set
     */
    public void setIsAvailable(boolean available) {
        if (!isDisabled) {
            this.isAvailable = available;
        } else {
            this.isAvailable = false; // Ensures that a disabled room cannot be set as available
        }
    }
}
