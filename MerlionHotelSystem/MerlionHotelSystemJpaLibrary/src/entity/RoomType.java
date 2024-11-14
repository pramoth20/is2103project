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
import javax.persistence.FetchType;
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
    
    @Column(nullable = false, unique = true)
    private String name; // Name of the room type

    @Column(nullable = false)
    private String description; // Description of the room type

    @Column(nullable = false)
    private int size; // Size of the room, e.g., in square meters

    @Column(nullable = false)
    private String bed; // Bed type, e.g., King, Queen, Twin

    @Column(nullable = false)
    private int capacity; // Capacity of the room, e.g., number of people

    @Column(nullable = false)
    private String amenities; // List of amenities as a comma-separated string, e.g., "WiFi, TV, Minibar"
    
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "next_room_type_id")
    private RoomType nextRoomType;

    
    
    /*@Column(nullable = false)
    private Boolean canChange; //facilitate the changing of the rooms
    */
    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL,fetch = FetchType.EAGER)

    private List<Rate> roomRate;

    @Column(nullable = false)
    private Boolean isDisabled = false;

    @Column(nullable = false)
    private Boolean isAvailable = true;

    public RoomType() {
        roomRate = new ArrayList<>();
    }

    public RoomType(String name, String description, int size, String bed, int capacity, String amenities) {
        this();
        this.name = name;
        this.description = description;
        this.size = size;
        this.bed = bed;
        this.capacity = capacity;
        this.amenities = amenities;
    }
    
    public RoomType(String name) {
        this();
        this.name = name;

        // Logic to assign values based on the name
        switch (name) {
            case "Deluxe Room":
                this.description = "A spacious deluxe room with modern amenities.";
                this.size = 35;
                this.bed = "King";
                this.capacity = 3;
                this.amenities = "WiFi, TV, Minibar, Bathtub";
                this.nextRoomType = new RoomType("Premier Room");
                break;
            case "Premier Room":
                this.description = "An upgraded room with premium features.";
                this.size = 45;
                this.bed = "King";
                this.capacity = 4;
                this.amenities = "WiFi, TV, Minibar, Bathtub, Balcony";
                this.nextRoomType = new RoomType("Family Room");
                break;
            case "Family Room":
                this.description = "A family-friendly room with additional space.";
                this.size = 50;
                this.bed = "Two Queen Beds";
                this.capacity = 5;
                this.amenities = "WiFi, TV, Minibar, Kitchenette";
                this.nextRoomType = new RoomType("Junior Suite");
                break;
            case "Junior Suite":
                this.description = "A junior suite with luxury amenities.";
                this.size = 60;
                this.bed = "King";
                this.capacity = 4;
                this.amenities = "WiFi, TV, Minibar, Living Area, Balcony";
                this.nextRoomType = new RoomType("Grand Suite");
                break;
            case "Grand Suite":
                this.description = "The grandest suite with exclusive luxury amenities.";
                this.size = 80;
                this.bed = "King";
                this.capacity = 6;
                this.amenities = "WiFi, TV, Minibar, Living Area, Balcony, Private Pool";
                this.nextRoomType = null; // No higher room type
                break;
            default:
                throw new IllegalArgumentException("Invalid room type name: " + name);
        }
    }


    // Getters and setters for the new attributes

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    // Existing getters and setters

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
        if (!(object instanceof RoomType)) {
            return false;
        }
        RoomType other = (RoomType) object;
        return !((this.roomTypeId == null && other.roomTypeId != null) || (this.roomTypeId != null && !this.roomTypeId.equals(other.roomTypeId)));
    }

    @Override
    public String toString() {
        return "entity.RoomType[ id=" + roomTypeId + " ]";
    }

    public List<Rate> getRoomRate() {
        return roomRate;
    }

    public void setRoomRate(List<Rate> roomRate) {
        this.roomRate = roomRate;
    }

    public RoomType getNextRoomType() {
        return nextRoomType;
    }

    public void setNextRoomType(RoomType nextRoomType) {
        this.nextRoomType = nextRoomType;
    }

    public Boolean getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(boolean disabled) {
        this.isDisabled = disabled;
        if (disabled) {
            isAvailable = false; // If the room type is disabled, it cannot be available
        }
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean available) {
        if (!isDisabled) {
            this.isAvailable = available;
        } else {
            this.isAvailable = false; // Ensures that a disabled room cannot be set as available
        }
    }
}