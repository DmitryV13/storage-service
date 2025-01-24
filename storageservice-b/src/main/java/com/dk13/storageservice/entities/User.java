package com.dk13.storageservice.entities;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@NamedEntityGraph(
        name = "u-authorities",
        attributeNodes = {
                @NamedAttributeNode("authorities"),
                @NamedAttributeNode("userReservation")
        }
)
@Table(name = "_user")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(
            columnDefinition = "VARCHAR(50)",
            nullable = false,
            unique = true
    )
    private String login;
    
    @Column(columnDefinition = "VARCHAR(60)")
    private String passwordHash;
    
    @Column(columnDefinition = "VARCHAR(50)")
    private String firstName;
    
    @Column(columnDefinition = "VARCHAR(50)")
    private String lastName;
    
    @Column(columnDefinition = "VARCHAR(191)")
    private String email;
    
    @Column(columnDefinition = "VARCHAR(256)")
    private String imageUrl;
    
    private Boolean activated;
    
    @Column(columnDefinition = "VARCHAR(10)")
    private String langKey;
    
    @Column(
            columnDefinition = "VARCHAR(36)",
            unique = true
    )
    private String activationKey;
    
    @Column(columnDefinition = "VARCHAR(20)")
    private String resetKey;
    
    @Column(columnDefinition = "VARCHAR(50)")
    private String createdBy;
    
    private Date createdDate;
    
    private Date resetDate;
    
    @Column(columnDefinition = "VARCHAR(50)")
    private String lastModifiedBy;
    
    private Date lastModifiedDate;
    
    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    foreignKey = @ForeignKey(name = "fk_user_id"),
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "authority_name",
                    foreignKey = @ForeignKey(name = "fk_authority_name"),
                    referencedColumnName = "name"
            )
    )
    private Set<Authority> authorities = new HashSet<>();
    
    @OneToMany(mappedBy = "user")
    private Set<StorageFile> storageFiles;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserReservation userReservation;
    
    public User(){
    }
    
    private User(builder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.passwordHash = builder.passwordHash;
        this.login = builder.login;
        this.activated = builder.activated;
        this.activationKey = builder.activationKey;
    }
    
    public Long getId() {
        return id;
    }
    
    public String getPassword() {
        return passwordHash;
    }
    
    public String getUsername() {
        return login;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public Boolean getActivated() {
        return activated;
    }
    
    public String getActivationKey() {
        return activationKey;
    }
    
    public Set<Authority> getAuthorities() {
        return authorities;
    }
    
    public List<String> getAuthoritiesList() {
        return authorities.stream()
                .map(Authority::getName)
                .toList();
    }
    
    public String getEmail() {
        return email;
    }
    
    public UserReservation getUserReservation() {
        return userReservation;
    }
    
    public Set<StorageFile> getStorageFiles() {
        return storageFiles;
    }
    
    public void setActivated(Boolean activated) {
        this.activated = activated;
    }
    
    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }
    
    public void setUserReservation(UserReservation userReservation) {
        this.userReservation = userReservation;
    }
    
    public static class builder{
        private String firstName;
        private String lastName;
        private String email;
        private String login;
        private String passwordHash;
        private String imageUrl;
        private Boolean activated;
        private String langKey;
        private String activationKey;
        private String resetKey;
        private String createdBy;
        private Date createdDate;
        private Date resetDate;
        private String lastModifiedBy;
        private Date lastModifiedDate;
        
        public builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }
        
        public builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
        
        public builder email(String email) {
            this.email = email;
            return this;
        }
        
        public builder login(String login) {
            this.login = login;
            return this;
        }
        
        public builder password(String password) {
            this.passwordHash = password;
            return this;
        }
        
        public builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }
        
        public builder activated(Boolean activated) {
            this.activated = activated;
            return this;
        }
        
        public builder langKey(String langKey) {
            this.langKey = langKey;
            return this;
        }
        
        public builder activationKey(String activationKey) {
            this.activationKey = activationKey;
            return this;
        }
        
        public builder resetKey(String resetKey) {
            this.resetKey = resetKey;
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
        
        public builder resetDate(Date resetDate) {
            this.resetDate = resetDate;
            return this;
        }
        
        public builder lastModifiedBy(String lastModifiedBy) {
            this.lastModifiedBy = lastModifiedBy;
            return this;
        }
        
        public builder lastModifiedDate(Date lastModifiedDate) {
            this.lastModifiedDate = lastModifiedDate;
            return this;
        }
        
        public User build() {
            return new User(this);
        }
    }
}
