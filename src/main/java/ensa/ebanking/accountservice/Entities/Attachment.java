package ensa.ebanking.accountservice.Entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Attachment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String attachmentUrl;
    private String description;

    @ManyToOne
    private AgentProfile agentProfile;

    public AgentProfile getAgentProfile() {
        return agentProfile;
    }

    public void setAgentProfile(AgentProfile agentProfile) {
        this.agentProfile = agentProfile;
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
