package com.ondeck.crm.vip.dto;

import com.ondeck.crm.vip.domain.entity.InvitationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VipRecordDetailsDto {

    private UUID vipRecordId;
    private String name;
    private String email;
    private InvitationStatus invitationStatus;
    private LocalDateTime invitationSentDate;
    private LocalDateTime invitationConfirmDate;
    private String invitationConfirmRemark;
    private String createdBy;
    private LocalDateTime createdDate;
    private String updatedBy;
    private LocalDateTime updatedDate;
}
