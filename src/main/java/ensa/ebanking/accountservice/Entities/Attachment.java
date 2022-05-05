package ensa.ebanking.accountservice.Entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Attachment implements Serializable {

    @Id
    private Long id;
    private String attachmentUrl;
    private String description;

    @ManyToOne
    private Profile profile;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Attachment() {
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }
}
