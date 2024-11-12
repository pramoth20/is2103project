/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author jwong
 */
@Entity
public class RoomType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomTypeId;
    
    @JoinColumn(nullable = false, unique = true)
    private String name;//name or a roomType
            
    @OneToOne
    @JoinColumn(name = "next_room_type_id")
    private RoomType nextRoomType;
    
    @Column(length = 225)
    private String details;
    
    /*@Column(nullable = false)
    private Boolean canChange; //facilitate the changing of the rooms
    */
    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL)
    private List<Rate> roomRate;
    
//    @OneToMany(mappedBy = "roomType")
//    private List<Room> rooms;
    
    //mark as isDisabled if the room is sitll in use, USE CASE 10
    @Column(nullable = false)
    private Boolean isDisabled = false;
    
    @Column(nullable = false)
    private Boolean isAvailable = true;
    
    public RoomType() {
        roomRate = new ArrayList<>();
        //rooms = new ArrayList<>();
    }

    public RoomType(String name, String details) {
        this();
        this.name = name;
        this.nextRoomType = nextRoomType;
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomTypeId != null ? roomTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomTypeId fields are not set
        if (!(object instanceof RoomType)) {
            return false;
        }
        RoomType other = (RoomType) object;
        if ((this.roomTypeId == null && other.roomTypeId != null) || (this.roomTypeId != null && !this.roomTypeId.equals(other.roomTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomType[ id=" + roomTypeId + " ]";
    }


    /**
     * @return the details
     */
    public String getDetails() {
        return details;
    }

    /**
     * @param details the details to set
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * @return the roomRate
     */
    public List<Rate> getRoomRate() {
        return roomRate;
    }

    /**
     * @param roomRate the roomRate to set
     */
    public void setRoomRate(List<Rate> roomRate) {
        this.roomRate = roomRate;
    }

    /**
     * @return the nextRoomType
     */
    public RoomType getNextRoomType() {
        return nextRoomType;
    }

    /**
     * @param nextRoomType the nextRoomType to set
     */
    public void setNextRoomType(RoomType nextRoomType) {
        this.nextRoomType = nextRoomType;
    }

    /**
     * @return the isDisabled
     */
    public Boolean getIsDisabled() {
        return isDisabled;
    }

    /**
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
    
}
