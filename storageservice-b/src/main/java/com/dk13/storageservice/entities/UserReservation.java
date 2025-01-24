package com.dk13.storageservice.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class UserReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long totalSize;
    
    private Long usedSize;
    
    private Boolean activated;
    
    @Column(columnDefinition = "VARCHAR(50)")
    private String createdBy;
    
    private Date createdDate;
    
    @OneToOne
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(name = "fk_user_reservation")
    )
    private User user;
    
    public UserReservation() {
    }
    
    public UserReservation(builder builder) {
        this.activated = builder.activated;
        this.createdBy = builder.createdBy;
        this.createdDate = builder.createdDate;
        this.totalSize = builder.totalSize;
        this.usedSize = builder.usedSize;
        this.user = builder.user;
    }
    
    public Boolean getActivated() {
        return activated;
    }
    
    public Long getTotalSize() {
        return totalSize;
    }
    
    public Long getUsedSize() {
        return usedSize;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public void setActivated(Boolean activated) {
        this.activated = activated;
    }
    
    public void setUsedSize(Long usedSize) {
        this.usedSize = usedSize;
    }
    
    public void addUsedSize(Long size) {
        this.usedSize += size;
    }
    
    public static class builder{
        private Long totalSize;
        private Long usedSize;
        private Boolean activated;
        private String createdBy;
        private Date createdDate;
        private User user;
        
        public builder totalSize(Long totalSize) {
            this.totalSize = totalSize;
            return this;
        }
        
        public builder usedSize(Long usedSize) {
            this.usedSize = usedSize;
            return this;
        }
        
        public builder activated(Boolean activated) {
            this.activated = activated;
            return this;
        }
        
        public builder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }
        
        public builder createdDate(Date createdDate) {
            this.createdDate = createdDate;
            return this;
        }
        
        public builder user(User user) {
            this.user = user;
            return this;
        }
        
        public UserReservation build(){
            return new UserReservation(this);
        }
    }
}
