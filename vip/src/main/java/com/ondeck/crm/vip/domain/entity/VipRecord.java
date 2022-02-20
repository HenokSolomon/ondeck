package com.ondeck.crm.vip.domain.entity;

import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "vip_record")
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VipRecord {

    @Id
    @Column(name = "vip_record_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID vipRecordId;

    private String name;
    private String email;

    @Column(name = "invitation_status")
    @Enumerated(STRING)
    private InvitationStatus invitationStatus;

    @Column(name = "invitation_sent_date")
    private LocalDateTime invitationSentDate;

    @Column(name = "invitation_confirm_date")
    private LocalDateTime invitationConfirmDate;

    private String invitationConfirmRemark;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;


    @PrePersist
    private void setAuditInfo() {

        if (!StringUtils.hasText( createdBy )) {
            this.createdBy = "system";//fixme (current user)
        }

        if (!StringUtils.hasText( updatedBy )) {
            this.updatedBy = "system";//fixme (current user)
        }

        if (createdDate == null) {
            this.createdDate = LocalDateTime.now();
        }

        if (updatedDate == null) {
            this.updatedDate = LocalDateTime.now();
        }
    }
}
