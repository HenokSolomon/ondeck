package com.ondeck.crm.vip.domain.service;

import javax.mail.MessagingException;
import java.util.Map;

public interface MessagingService {
    /**
     * @param from
     * @param to
     * @param subject
     * @param templateName
     * @param templateModel
     * @throws MessagingException
     */
    void sendEmail(String from, String to, String subject, String templateName, Map<String, Object> templateModel) throws MessagingException;
}
