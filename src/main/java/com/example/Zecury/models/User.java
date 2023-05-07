package com.example.Zecury.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.text.SimpleDateFormat;


import java.util.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_user")
public class User implements UserDetails {
    public static Random rand=new Random();
    @Id
//    @Column(name = "id")
    private String id=UUID.randomUUID().toString();
    //    @Column(name = "uId")
    private String uId = String.valueOf(rand.nextInt(1000000-99999+1)+99999);
    private String accNo = String.valueOf(rand.nextInt((100000000 - 9999999) + 1)+9999999);
    private String name;
    private String password;
    private Double balance =5000.0;
    private String address;
    private String email;
    private String mobile;
    private Date dateOfBirth;
    private Date dateCreated;
    private Date lastLoggedin;
    private String aadarno;
    private String panno;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();


    @OneToMany(mappedBy="user" , fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Transactions> transactions;

    public User(String name, String password, String address, String email, String mobile, Date dateOfBirth, Date dateCreated, Date lastLoggedin, String aadarno, String panno, Set<Role> roles) {
        this.name = name;
        this.password = password;
        this.address = address;
        this.email = email;
        this.mobile = mobile;
        this.dateOfBirth = dateOfBirth;
        this.dateCreated = dateCreated;
        this.lastLoggedin = lastLoggedin;
        this.aadarno = aadarno;
        this.panno = panno;
        this.roles = roles;
    }



    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return "user{" +
                "uid = " + uId +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", dateOfBirth=" + dateFormat.format(dateOfBirth) +
                ", dateCreated=" + dateFormat.format(dateCreated) +
                ", lastLoggedin=" + lastLoggedin +
                ", aadarno='" + aadarno + '\'' +
                ", panno='" + panno + '\'' +
                ", role='" +roles + '\'' +
//                ", transactions='" +transactions + '\'' +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(roles.toString()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return uId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
