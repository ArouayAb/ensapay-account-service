package ensa.ebanking.accountservice.Entities;


import javax.persistence.*;
import java.util.Date;

@Entity
public class Sms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String phone;
    private String messageText;
    private Date expirationDate;
    @OneToOne
    private User user;

    public Sms(){}

    public Sms(String phone, String messageText,User user,Date expirationDate) {
        this.phone = phone;
        this.messageText = messageText;
        this.user=user;
        this.expirationDate=expirationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
