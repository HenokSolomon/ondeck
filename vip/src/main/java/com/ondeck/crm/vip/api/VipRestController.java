package com.ondeck.crm.vip.api;

import com.ondeck.crm.vip.api.model.CreateVipRequest;
import com.ondeck.crm.vip.domain.entity.InvitationStatus;
import com.ondeck.crm.vip.domain.service.VipService;
import com.ondeck.crm.vip.dto.VipRecordDetailsDto;
import com.ondeck.crm.vip.dto.VipRecordDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping(value = {"/vip"})
@RestController
@Slf4j
public class VipRestController {

    private final VipService vipService;

    public VipRestController(VipService vipService) {
        this.vipService = vipService;
    }

    @PostMapping("/create")
    @ResponseBody
    public VipRecordDetailsDto create(@RequestBody CreateVipRequest request) {

        var dto = VipRecordDto.builder().email( request.getEmail() ).name( request.getName() ).build();

        return vipService.createVipRecord( dto );

    }

    @GetMapping("/accept/{vipRecordId}")
    @ResponseBody
    public VipRecordDetailsDto accept(@PathVariable("vipRecordId") UUID vipRecordId) {

        return vipService.confirmInvitation( vipRecordId, InvitationStatus.ACCEPTED );
    }

    @GetMapping("/reject/{vipRecordId}")
    @ResponseBody
    public VipRecordDetailsDto reject(@PathVariable("vipRecordId") UUID vipRecordId) {

        return vipService.confirmInvitation( vipRecordId, InvitationStatus.REJECTED );
    }


    @GetMapping("/findAll")
    @ResponseBody
    public List<VipRecordDetailsDto> findAll() {
        return vipService.findAll();
    }

}
