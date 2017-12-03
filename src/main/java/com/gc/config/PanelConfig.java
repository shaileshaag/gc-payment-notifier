package com.gc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.client.RestTemplate;

import com.gc.component.DatePickerPanel;
import com.gc.component.MainFrame;
import com.gc.component.MemberFileLoaderImpl;
import com.gc.component.NotificationPanel;
import com.gc.service.EmailNotificationsSender;
import com.gc.service.MemberDetailsReader;
import com.gc.service.NotificationsLoader;
import com.gc.service.PaymentDetailsReader;
import com.gc.service.SmsNotificationsSender;
import com.gc.vo.EmailNotificationProperties;
import com.gc.vo.SmsNotificationProperties;

@Configuration
public class PanelConfig {

	@Autowired
	private JavaMailSenderImpl javaMailSender;
	
	@Bean(initMethod = "init")
	public MemberFileLoaderImpl memberDetailsFilePanel() {
		return new MemberFileLoaderImpl("Member Details File  ", "Load");
	}

	@Bean(initMethod = "init")
	public MemberFileLoaderImpl memberPaymentsFilePanel() {
		return new MemberFileLoaderImpl("Member Payment File", "Load");
	}

	@Bean(initMethod = "init")
	public DatePickerPanel fromDatePickerPanel() {
		return new DatePickerPanel("dd-MMM-yyyy", "Notifications from");
	}

	@Bean(initMethod = "init")
	public NotificationPanel notificationPanel() {
		return new NotificationPanel("Send Notifications", emailNotificationSender(), smsNotificationSender());
	}

	@Bean
	public NotificationsLoader notificationsLoader() {
		return new NotificationsLoader(memberDetailsFilePanel(), memberPaymentsFilePanel(), fromDatePickerPanel(),
				notificationPanel(), memberDetailsReader(), paymentDetailsReader());
	}

	@Bean
	public EmailNotificationsSender emailNotificationSender() {
		return new EmailNotificationsSender(emailNotificationProperties(), javaMailSender);
	}

	@Bean
	public SmsNotificationsSender smsNotificationSender() {
		return new SmsNotificationsSender(smsNotificationProperties(), smsRestTemplate());
	}

	@Bean
	public MemberDetailsReader memberDetailsReader() {
		return new MemberDetailsReader();
	}

	@Bean
	public PaymentDetailsReader paymentDetailsReader() {
		return new PaymentDetailsReader();
	}

	@Bean(initMethod = "init")
	public MainFrame mainFrame() {
		return new MainFrame(memberDetailsFilePanel(), memberPaymentsFilePanel(), fromDatePickerPanel(),
				notificationPanel(), notificationsLoader());
	}

	@Bean
	@ConfigurationProperties(prefix = "payment.mail")
	public EmailNotificationProperties emailNotificationProperties() {
		return new EmailNotificationProperties();
	}

	@Bean
	@ConfigurationProperties(prefix = "payment.sms")
	public SmsNotificationProperties smsNotificationProperties() {
		return new SmsNotificationProperties();
	}

	@Bean
	public RestTemplate smsRestTemplate() {
		return new RestTemplate();
	}

}
