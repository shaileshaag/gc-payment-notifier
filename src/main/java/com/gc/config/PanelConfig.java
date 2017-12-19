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
import com.gc.component.pending.PendingNotificationTableColCheckboxDecider;
import com.gc.service.EmailNotificationsSender;
import com.gc.service.MemberDetailsReader;
import com.gc.service.SmsNotificationsSender;
import com.gc.service.payment.PaymentDetailsReader;
import com.gc.service.payment.PaymentNotificationsLoader;
import com.gc.service.pending.PendingDetailsReader;
import com.gc.service.pending.PendingNotificationsLoader;
import com.gc.util.GcEmailSender;
import com.gc.util.GcSmsSender;
import com.gc.vo.EmailNotificationProperties;
import com.gc.vo.SmsNotificationProperties;
import com.gc.vo.payment.PaymentNotification;
import com.gc.vo.pending.PendingNotification;

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

	@Bean
	public GcEmailSender gcEmailSender() {
		return new GcEmailSender(javaMailSender);
	}

	@Bean
	public GcSmsSender gcSmsSender() {
		return new GcSmsSender(smsRestTemplate(), paymentSmsNotificationProperties());
	}

	@Bean
	public RestTemplate smsRestTemplate() {
		return new RestTemplate();
	}

	@Bean
	public MemberDetailsReader memberDetailsReader() {
		return new MemberDetailsReader();
	}

	@Bean
	public EmailNotificationsSender paymentEmailNotificationSender() {
		return new EmailNotificationsSender(paymentEmailNotificationProperties(), gcEmailSender());
	}

	@Bean
	@ConfigurationProperties(prefix = "payment.mail")
	public EmailNotificationProperties paymentEmailNotificationProperties() {
		return new EmailNotificationProperties();
	}

	@Bean
	public SmsNotificationsSender paymentSmsNotificationSender() {
		return new SmsNotificationsSender(paymentSmsNotificationProperties(), gcSmsSender());
	}

	@Bean
	@ConfigurationProperties(prefix = "payment.sms")
	public SmsNotificationProperties paymentSmsNotificationProperties() {
		return new SmsNotificationProperties();
	}

	@Bean
	public PaymentDetailsReader paymentDetailsReader() {
		return new PaymentDetailsReader();
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
		return new NotificationPanel("Send Payment Notifications", paymentEmailNotificationSender(),
				paymentSmsNotificationSender(), new PaymentNotificationTableColCheckboxDecider(),
				PaymentNotification.HEADERS);
	}

	@Bean
	public PaymentNotificationsLoader paymentNotificationsLoader() {
		return new PaymentNotificationsLoader(memberDetailsFilePanel(), memberPaymentsFilePanel(),
				fromDatePickerPanel(), paymentNotificationPanel(), memberDetailsReader(), paymentDetailsReader());
	}

	@Bean(initMethod = "init")
	public NotificationTab paymentNotificationTab() {
		return new PaymentNotificationTab(memberPaymentsFilePanel(), fromDatePickerPanel(), paymentNotificationPanel(),
				paymentNotificationsLoader());
	}

	@Bean(initMethod = "init")
	public FileLoaderImpl memberPendingFilePanel() {
		return new FileLoaderImpl("Member Pending File", "Load");
	}

	@Bean(initMethod = "init")
	public DatePickerPanel pendingDatePickerPanel() {
		return new DatePickerPanel("dd-MMM-yyyy", "Pending payments since");
	}

	@Bean
	public EmailNotificationsSender pendingEmailNotificationSender() {
		return new EmailNotificationsSender(pendingEmailNotificationProperties(), gcEmailSender());
	}

	@Bean
	@ConfigurationProperties(prefix = "pending.mail")
	public EmailNotificationProperties pendingEmailNotificationProperties() {
		return new EmailNotificationProperties();
	}

	@Bean
	public SmsNotificationsSender pendingSmsNotificationSender() {
		return new SmsNotificationsSender(pendingSmsNotificationProperties(), gcSmsSender());
	}

	@Bean
	@ConfigurationProperties(prefix = "pending.sms")
	public SmsNotificationProperties pendingSmsNotificationProperties() {
		return new SmsNotificationProperties();
	}

	@Bean(initMethod = "init")
	public NotificationPanel pendingNotificationPanel() {
		return new NotificationPanel("Send Pending Notifications", pendingEmailNotificationSender(),
				pendingSmsNotificationSender(), new PendingNotificationTableColCheckboxDecider(),
				PendingNotification.HEADERS);
	}

	@Bean
	public PendingDetailsReader pendingDetailsReader() {
		return new PendingDetailsReader();
	}

	@Bean
	public PendingNotificationsLoader pendingNotificationsLoader() {
		return new PendingNotificationsLoader(memberDetailsFilePanel(), memberPendingFilePanel(),
				pendingDatePickerPanel(), pendingNotificationPanel(), memberDetailsReader(), pendingDetailsReader());
	}

	@Bean(initMethod = "init")
	public NotificationTab pendingNotificationTab() {
		return new PendingNotificationTab(memberPendingFilePanel(), pendingDatePickerPanel(),
				pendingNotificationPanel(), pendingNotificationsLoader());
	}

	@Bean(initMethod = "init")
	public MainFrame mainFrame() {
		List<NotificationTab> notificationTabs = new ArrayList<>();
		notificationTabs.add(paymentNotificationTab());
		notificationTabs.add(pendingNotificationTab());
		return new MainFrame(memberDetailsFilePanel(), notificationTabs);
	}

}
