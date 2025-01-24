package com.dk13.storageservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.List;

@Entity
public class Authority {
    @Id
    @Column(columnDefinition = "VARCHAR(50)")
    private String name;
    
    @ManyToMany(mappedBy = "authorities")
    private List<User> users;
    
    public Authority() {
    }
    
    public Authority(builder builder) {
        this.name = builder.name;
    }
    
    public String getName() {
        return name;
    }
    
    public static class builder{
        private String name;
        
        public builder name(String name){
            this.name = name;
            return this;
        }
        
        public Authority build(){
            return new Authority(this);
        }
    }
}
