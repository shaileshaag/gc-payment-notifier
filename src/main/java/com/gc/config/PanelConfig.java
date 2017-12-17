package com.gc.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.client.RestTemplate;

import com.gc.component.MainFrame;
import com.gc.component.common.DatePickerPanel;
import com.gc.component.common.FileLoaderImpl;
import com.gc.component.common.NotificationPanel;
import com.gc.component.common.NotificationTab;
import com.gc.component.payment.PaymentNotificationTab;
import com.gc.component.payment.PaymentNotificationTableColCheckboxDecider;
import com.gc.component.pending.PendingNotificationTab;
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

	@Value("${default.member.file-path:}")
	private String defaultMemberDetailsFilePath;

	@Bean(initMethod = "init")
	public FileLoaderImpl memberDetailsFilePanel() {
		return new FileLoaderImpl("Member Details File  ", "Load", defaultMemberDetailsFilePath);
	}

	@Bean(initMethod = "init")
	public FileLoaderImpl memberPaymentsFilePanel() {
		return new FileLoaderImpl("Member Payment File", "Load");
	}

	@Bean(initMethod = "init")
	public DatePickerPanel fromDatePickerPanel() {
		return new DatePickerPanel("dd-MMM-yyyy", "Notifications from");
	}

	@Bean(initMethod = "init")
	public NotificationPanel paymentNotificationPanel() {
		return new NotificationPanel("Send Notifications", emailNotificationSender(), smsNotificationSender(),
				new PaymentNotificationTableColCheckboxDecider());
	}

	@Bean
	public NotificationsLoader notificationsLoader() {
		return new NotificationsLoader(memberDetailsFilePanel(), memberPaymentsFilePanel(), fromDatePickerPanel(),
				paymentNotificationPanel(), memberDetailsReader(), paymentDetailsReader());
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
		List<NotificationTab> notificationTabs = new ArrayList<>();
		notificationTabs.add(paymentNotificationTab());
		notificationTabs.add(pendingNotificationTab());
		return new MainFrame(memberDetailsFilePanel(), notificationTabs);
	}

	@Bean(initMethod = "init")
	public NotificationTab paymentNotificationTab() {
		return new PaymentNotificationTab(memberPaymentsFilePanel(), fromDatePickerPanel(), paymentNotificationPanel(),
				notificationsLoader());
	}

	@Bean(initMethod = "init")
	public NotificationTab pendingNotificationTab() {
		return new PendingNotificationTab();
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
