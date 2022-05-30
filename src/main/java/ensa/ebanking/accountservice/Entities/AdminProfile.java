package ensa.ebanking.accountservice.Entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AdminProfile extends Profile {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private String username;

    private String phone;

    public AdminProfile(String username,String phone) {
        super(username,username,"");
        this.username = username;
        this.phone=phone;
    }

    public AdminProfile() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
