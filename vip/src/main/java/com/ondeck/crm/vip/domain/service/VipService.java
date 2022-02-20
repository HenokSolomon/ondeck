package com.ondeck.crm.vip.domain.service;

import com.ondeck.crm.vip.domain.entity.InvitationStatus;
import com.ondeck.crm.vip.domain.entity.VipRecord;
import com.ondeck.crm.vip.domain.exceptions.ServiceException;
import com.ondeck.crm.vip.dto.VipRecordDetailsDto;
import com.ondeck.crm.vip.dto.VipRecordDto;

import java.util.List;
import java.util.UUID;

public interface VipService {

    VipRecordDetailsDto createVipRecord(VipRecordDto vipRecordDto) throws ServiceException;

    VipRecordDetailsDto confirmInvitation(UUID vidRecordId, InvitationStatus invitationStatus);

    void sendSignupInvitationEmail(VipRecord vipRecord) throws Exception;

    List<VipRecordDetailsDto> findAll();
}
