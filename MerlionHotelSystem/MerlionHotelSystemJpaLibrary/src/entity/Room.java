/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author pramoth
 */
@Entity
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long roomId;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType roomType;
    
    //2 digit floor and 2 digit room number
//    @Column(length = 4, nullable = false)
//    private String roomNumber;
    
    @Column(nullable = false)
    private boolean roomStatus;
    @NotNull
    @Min(1) @Max(99)
    @Column(name = "floor_number", nullable = false)
    private Integer floorNumber;
    
    @NotNull
    @Min(1) @Max(99)
    @Column(nullable = false)
    private Integer sequenceNumber;

    @Column(length = 4, nullable = false, unique = true)
    private String roomNumber; // Computed field for the 4-digit room number
    
    @Column(nullable = false)
    private Boolean isDisabled;
    
    @Column(nullable = false)
    private Boolean isAvailable;

    public Room(){
    }
    
    public Room(RoomType roomType, boolean roomStatus,Integer floorNumber, Integer sequenceNumber, String roomNumber, Boolean isDisabled, Boolean isAvailable) {
        this.roomType = roomType;
        this.roomStatus = roomStatus;
        this.floorNumber = floorNumber;
        this.sequenceNumber = sequenceNumber;
        this.isDisabled = false; // By default, a room is not disabled
        this.isAvailable = true; // By default, a room is available if it is not disabled
    }



    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
    
    
    /**
     * @return the roomType
     */
    public RoomType getRoomType() {
        return roomType;
    }

    /**
     * @param roomType the roomType to set
     */
    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    /**
     * @return the roomNumber
     */
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
     * @return the roomStatus
     */
    public boolean isRoomStatus() {
        return roomStatus;
    }

    /**
     * @param roomStatus the rooåmStatus to set
     */
    public void setRoomStatus(boolean roomStatus) {
        this.roomStatus = roomStatus;
    }
    
    /**
     * @return the isDisabled
     */
    public Boolean getIsDisabled() {
        return isDisabled;
    }

    /**
     * @param disabled
     * @param isDisabled the isDisabled to set
     */
    public void setIsDisabled(boolean disabled) {
        this.isDisabled = disabled;
        if (disabled) {
            isAvailable = false; // If the room is disabled, it cannot be available
        }
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
    
    @PrePersist
    @PreUpdate
    private void generateRoomNumber() {
        this.roomNumber = String.format("%02d%02d", floorNumber, sequenceNumber);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomId != null ? roomId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomId fields are not set
        if (!(object instanceof Room)) {
            return false;
        }
        Room other = (Room) object;
        if ((this.roomId == null && other.roomId != null) || (this.roomId != null && !this.roomId.equals(other.roomId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Room[ id=" + roomId + " ]";
    }
    
}
