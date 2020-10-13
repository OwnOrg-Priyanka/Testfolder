package com.sendmail;

	import com.apigee.flow.execution.ExecutionContext;
	import com.apigee.flow.execution.ExecutionResult;
	import com.apigee.flow.execution.spi.Execution;
	import com.apigee.flow.message.MessageContext;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
//import java.net.URL;
	import java.util.*;
	import java.util.Properties;
	import javax.mail.Address;
	import javax.mail.Authenticator;
	import javax.mail.Message;
	import javax.mail.SendFailedException;
	import javax.mail.MessagingException;
	import javax.mail.internet.AddressException;
	import javax.mail.PasswordAuthentication;
	import javax.mail.Session;
	import javax.mail.Transport;
	import javax.mail.internet.InternetAddress;
	import javax.mail.internet.MimeMessage;

	  class MailResponse{
		  
		  String mailId;
		  String mailStatus;
		  String errorResponse;
	  }
	public class SendMailBySite  implements Execution {
	  private final Map<String, String> parameters;
	  
	  public SendMailBySite(Map<String, String> parameters) {
	    this.parameters = parameters;
	  }

	  public static void main(String[] args){
		  ArrayList<String> emailTo = new ArrayList<String>();
		  ArrayList<String> txt = new ArrayList<String>();
		  ArrayList<String> emailCc = new ArrayList<String>();
		  ArrayList<String> emailBcc = new ArrayList<String>();
		  MessageContext messageContext = null;
		  String test = "['puja0','puja1']";
		  String Host =  "test";
		  List<String> testcc = new ArrayList<String>();
		  testcc.add("[]");
		  txt.add("puja.kheta@vodafone.com");
		  emailTo = txt;
		   String emailFrom = "puja.khetan@vodfone.com";
		      String emailSubject = "Subject";
		     String emailBody = "Content";
		       String ContentType = "text/plain";
		       String flags =  "!";
		       String headers = "stanadrdemail";
		       try {
		   sendMail(messageContext, emailFrom, emailTo, emailCc, emailBcc, emailSubject, emailBody, ContentType, flags, headers, Host);
		   System.out.println("debugpoint1");
		       }catch(Exception e) {
		    	   
		       }
	  }
	  public static List<String> getList(String emailId , MessageContext messageContext ){
		  if (emailId.contains("[]")) {
				return new ArrayList<String>();	  
		} 
		  emailId = emailId.substring(1,(emailId.length()-1));
		  emailId = emailId.replace("\"", "");
		  String str[] = emailId.split(",");
		  List<String> emailIdList = new ArrayList<String>();
		  emailIdList = Arrays.asList(str);
		  System.out.println(emailId+" "+emailIdList);
		  return emailIdList;
	  }
	  public ExecutionResult execute(MessageContext messageContext, ExecutionContext executionContext) {
		  
	    String emailFrom = null,emailTo = null, emailBody = null;
	    MailResponse mresponse = new MailResponse() ;
	    String javaFailed = "false";
	    java.net.URL classUrl = this.getClass().getResource("com.sun.mail.util.TraceInputStream");
	    messageContext.setVariable("classVar", classUrl);
	    try {
			List<String> emailToList = new ArrayList<String>();
			List<String> emailCcList = new ArrayList<String>();
			List<String> emailBccList = new ArrayList<String>();
			emailFrom = (String) messageContext.getVariable("From");
			emailTo = (String) messageContext.getVariable("To");
			emailToList = getList(emailTo, messageContext);
			String Host = (String) messageContext.getVariable("host");
			String Port = (String) messageContext.getVariable("port");
			String emailCc = (String) messageContext.getVariable("Cc");
			emailCcList = getList(emailCc,messageContext);
			String emailBcc = (String) messageContext.getVariable("Bcc");
			emailBccList = getList(emailBcc, messageContext);
			String emailSubject = (String) messageContext.getVariable("Subject");
			emailBody = (String) messageContext.getVariable("Content");
			String ContentType = (String) messageContext.getVariable("ContentType");
			String flags = (String) messageContext.getVariable("Flags");
			String headers = (String) messageContext.getVariable("Headers");
			mresponse = sendMail(messageContext, emailFrom, emailToList, emailCcList, emailBccList, emailSubject, emailBody, ContentType, flags, headers, Host, Port);
				messageContext.setVariable("JAVA_Success", String.valueOf(emailFrom) + emailTo);
				messageContext.setVariable("traceflags", flags);
				messageContext.setVariable("traceheaders", headers);
				messageContext.setVariable("Result", "Success");
				messageContext.setVariable("javaFailed", javaFailed);
				messageContext.setVariable("emailMsgId", mresponse.mailId);
			
			
			return ExecutionResult.SUCCESS;
	    }catch (NullPointerException ne) {
		      messageContext.setVariable("errorJSON", "NullPOinterException");
		      messageContext.setVariable("javaFailed", "true");
		      return ExecutionResult.ABORT;
		    } 
	    catch (Exception e) {
		      messageContext.setVariable("javaFailed", "true");
		  
	       String errorJSON = messageContext.getVariable("errorJSON");
	      if(errorJSON == null || errorJSON.length()== 0){
	    	  messageContext.setVariable("errorJSON", "Unknown_error");
	    	  
	      }
	      StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			try {
				InetAddress localhost = InetAddress.getLocalHost();
				messageContext.setVariable("hostAdd", localhost);
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace(pw);
			String sStackTrce = sw.toString();
			messageContext.setVariable("sStackTrce", sStackTrce); 
	      return ExecutionResult.ABORT;
	    } 
	  }
	  

	  public static MailResponse sendMail(MessageContext messageContext, String emailFrom, List<String> emailTo, List<String> emailCc, List<String> emailBcc, String emailSubject, String emailBody, String ContentType, String flags, String headers, String Host, String Port) throws MessagingException,SendFailedException,AddressException{
	    String host = Host;
	    String port = Port;
	    Properties properties = System.getProperties();
	    properties.setProperty("mail.smtp.host", host);
	    properties.put("mail.smtp.auth", "true");
	    properties.put("mail.smtp.port", port);
	    properties.put("mail.smtp.starttls.enable", "false");
	    
	    MailResponse mResponse = new MailResponse();
	    
	    try {
	      Session session2 = Session.getInstance(properties, 
	          new Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	              return new PasswordAuthentication(
	                  "test@vodafone.com", "xyz");
	            }
	          });
	      MimeMessage message = new MimeMessage(session2);
	      message.setFrom((Address)new InternetAddress(emailFrom));
	      for(String mailTo: emailTo){
	    	  message.addRecipient(Message.RecipientType.TO, (Address)new InternetAddress(mailTo));
	      }
	      if (!(emailCc.isEmpty())){
	      for(String mailTo: emailCc){
	    	  message.addRecipient(Message.RecipientType.CC, (Address)new InternetAddress(mailTo));
	      }
	      }
	      if (!(emailBcc.isEmpty())){
	      for(String mailTo: emailBcc){
	    	  System.out.println(mailTo);
	    	  message.addRecipient(Message.RecipientType.BCC, (Address)new InternetAddress(mailTo));
	      }}
	      message.setSubject(emailSubject);
	      if (ContentType.contains("text/plain")){
	    	  message.setContent(emailBody, "text/plain");  
	      } else if (ContentType.contains("text/html")) {
	    	  message.setContent(emailBody, "text/html");
	      }
	      if (flags != null && flags.length() > 0) {
              char[] flgs = flags.toCharArray();
              for (char ch : flgs) {
                              switch (ch) {
                              case '!':
                                              message.addHeader("X-Priority", "1");
                                              message.addHeader("Importance", "high");
                                              break;
                              case '?':
                                              message.addHeader("X-Priority", "5");
                                              message.addHeader("Importance", "low");
                                              break;
                              case 'f':
                                              message.addHeader("X-Message-Flag", "Follow up");
                                              break;
                              }
              }
	      }
	      
	      if (headers != null && headers.length() > 0) {
              String[] hdrs = headers.split(",");
              for (String h : hdrs)
                              message.addHeaderLine(h);
	      }


	      Transport.send((Message)message);
	      System.out.println("Mail successfully sent");
	      String messageid = message.getMessageID();
	      mResponse.mailId = messageid;
	      System.out.println("messageid:" + messageid);
	      
	    } catch (SendFailedException e) {
	    	messageContext.setVariable("errorJSON", "SendFailedException");
	    	throw e;
	    } 
	    catch (AddressException e) {
	    	messageContext.setVariable("errorJSON", "AddressException");
	    	throw e;
	    } 
	    catch (MessagingException me) {
	    	messageContext.setVariable("errorJSON", "MessagingException");
	    	throw me;
	    } 
	    catch (Exception mex) {
	    	messageContext.setVariable("errorJSON", "Exception");
	      throw mex;
	    }
		return mResponse; 
	  }
	}

