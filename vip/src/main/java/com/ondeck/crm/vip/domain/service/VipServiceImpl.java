package com.ondeck.crm.vip.domain.service;

import com.ondeck.crm.vip.domain.entity.InvitationStatus;
import com.ondeck.crm.vip.domain.entity.VipRecord;
import com.ondeck.crm.vip.domain.exceptions.ServiceException;
import com.ondeck.crm.vip.domain.repository.VipRecordRepository;
import com.ondeck.crm.vip.dto.VipRecordDetailsDto;
import com.ondeck.crm.vip.dto.VipRecordDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service("vipService")
@Slf4j
public class VipServiceImpl implements VipService {

    /* email address validation regex */
    public static final String REGEX_MATCHER_VALID_EMAIL = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private static final String DEFAULT_INVITATION_EMAIL_SUBJECT = "Your Vip Invitation";
    private static final String DEFAULT_EMAIL_FROM = "solomonmail88@gmail.com";
    private static final String INVITATION_EMAIL_TEMPLATE_NAME = "vip-invitation-email";
    private VipRecordRepository vipRecordRepository;
    private MessagingService messagingService;


    public VipServiceImpl(VipRecordRepository vipRecordRepository, MessagingService messagingService) {
        this.vipRecordRepository = vipRecordRepository;
        this.messagingService = messagingService;
    }

    @Override
    public VipRecordDetailsDto createVipRecord(VipRecordDto vipRecordDto) throws ServiceException {

        /* validate */
        if (vipRecordDto == null) {
            throw new ServiceException( "invalid request", ServiceException.INVALID_CREATE_VIP_REQUEST );
        }

        /*check if the required fields are present , exit with error in case any one of them is missing*/
        if (!StringUtils.hasText( vipRecordDto.getName() )) {
            throw new ServiceException( "your request is missing with some required fields please check your request and try again",
                    ServiceException.INVALID_CREATE_VIP_REQUEST );
        }

        /* validate email address format , exit with error if its not valid */
        validateEmail( vipRecordDto.getEmail() );

        /* find existing vip with the same email , if exist then exit with error */
        var existingVip = vipRecordRepository.findVipRecordByEmail( vipRecordDto.getEmail() );
        if (existingVip != null) {
            throw new ServiceException( "vip with the same email " + vipRecordDto.getEmail() + " already exists", ServiceException.DUPLICATE_EMAIL );
        }


        /* create a VipRecord and add it to the Database */
        var vipRecord = vipRecordRepository.save(
                VipRecord.builder()
                        .createdBy( vipRecordDto.getCreatedBy() )
                        .name( vipRecordDto.getName() )
                        .email( vipRecordDto.getEmail() )
                        .invitationStatus( InvitationStatus.PENDING )
                        .build() );

        try {

            /* try to send invitation email */
            sendSignupInvitationEmail( vipRecord );

            /*if invitation is ok , change its Inv..status and inv.. sentDate then update the vipRecord , */
            vipRecord.setInvitationStatus( InvitationStatus.PENDING_CONFIRM );
            vipRecord.setInvitationSentDate( LocalDateTime.now() );

            /* update */
            vipRecord = vipRecordRepository.save( vipRecord );

        } catch (Exception ex) {
            /* if sending email failed , then do nothing , the email portion will be tried latter by batch job */
            log.warn( "VipServiceImpl : email invitation failed for vipRecord email " + vipRecord.getEmail(), ex );
        }

        /* finally return the created vip record details */
        return buildVipDetailDTO( vipRecord );
    }

    @Override
    public VipRecordDetailsDto confirmInvitation(UUID vidRecordId, InvitationStatus invitationStatus) {

        /* find existing vip by its id , exit with error if doesn't exit  */
        var existingVip = vipRecordRepository.findVipRecordByVipRecordId(vidRecordId);
        if (existingVip == null) {
            throw new ServiceException( "vip record doesn't exist ", ServiceException.INVALID_VIP_RECORD );
        }

        if(existingVip.getInvitationStatus() != InvitationStatus.PENDING_CONFIRM) {
            throw new ServiceException( "invalid vip record status", ServiceException.INVALID_VIP_RECORD );
        }

        /* if record status is ok , then update record with the invitation status */
        existingVip.setInvitationStatus( invitationStatus );
        existingVip.setInvitationConfirmDate( LocalDateTime.now() );

        vipRecordRepository.save( existingVip );

        return buildVipDetailDTO( existingVip );
    }


    public List<VipRecordDetailsDto> findAll() {

        List<VipRecordDetailsDto> vipRecordDetailsDtoList = new ArrayList<>();

        List<VipRecord> vipRecordList = vipRecordRepository.findAll();

        if(vipRecordList != null) {

            vipRecordDetailsDtoList = vipRecordList.stream().map( vip -> buildVipDetailDTO( vip ) )
                    .sorted(Comparator.comparing(VipRecordDetailsDto::getUpdatedDate))
                    .collect( Collectors.toList());
        }

        return vipRecordDetailsDtoList;
    }


    /* validates email format , if the current email is not valid it throws ServiceException */
    private void validateEmail(String email) {

        if (!StringUtils.hasText( email )) {
            throw new ServiceException( "invalid email address " + email, ServiceException.INVALID_EMAIL_FORMAT );
        }

        final boolean match = (Pattern.compile( REGEX_MATCHER_VALID_EMAIL, Pattern.CASE_INSENSITIVE ).matcher( email ).matches());
        if (!match) {
            throw new ServiceException( "invalid email address " + email, ServiceException.INVALID_EMAIL_FORMAT );
        }
    }

    @Override
    public void sendSignupInvitationEmail(VipRecord vipRecord) throws Exception {

        /* validates the emails */
        validateEmail( vipRecord.getEmail() );

        Map<String, Object> templateParam = new HashMap<>();
        templateParam.put( "recipientName", vipRecord.getName() );
        templateParam.put( "vipRecordId", vipRecord.getVipRecordId());

        messagingService.sendEmail( DEFAULT_EMAIL_FROM, vipRecord.getEmail(), DEFAULT_INVITATION_EMAIL_SUBJECT, INVITATION_EMAIL_TEMPLATE_NAME, templateParam );
    }

    private VipRecordDetailsDto buildVipDetailDTO(VipRecord vipRecord) {

        return VipRecordDetailsDto
                .builder().vipRecordId( vipRecord.getVipRecordId() )
                .name( vipRecord.getName() )
                .email( vipRecord.getEmail() )
                .invitationSentDate( vipRecord.getInvitationSentDate() )
                .createdBy( vipRecord.getCreatedBy() )
                .createdDate( vipRecord.getCreatedDate() )
                .updatedBy( vipRecord.getUpdatedBy() )
                .updatedDate( vipRecord.getUpdatedDate() )
                .invitationStatus( vipRecord.getInvitationStatus() )
                .invitationConfirmDate( vipRecord.getInvitationConfirmDate() )
                .invitationSentDate( vipRecord.getInvitationSentDate() )
                .build();
    }

}
