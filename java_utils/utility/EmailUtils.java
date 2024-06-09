package com.ngwisefood.app.utility;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * Created by andy on 2/3/20.
 */
public class EmailUtils {

    private static Session session;

    public static Session getEmailSession() {
        if (session != null) {
            return session;
        }
        final String fromEmail = "moneymanager365@gmail.com"; //requires valid gmail id
        final String password = "Pkoxyz861016"; // correct password for gmail id

        //System.out.println("TLSEmail Start");
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        Session session = Session.getInstance(props, auth);

        return session;
    }

    private static Session getDefaultEmailSession(){

        // Assuming you are sending email from localhost
        String host = "localhost";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        // Get the default Session object.
        Session session = Session.getInstance(properties);

        return session;
    }

    public static boolean sendEmail(String toEmail, String subject, String body) {
        try {
            return EmailUtils.sendEmail(getEmailSession(), toEmail, subject, body);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean sendEmailHtml(String toEmail, String subject, String body) {
        try {
            return EmailUtils.sendEmailHtml(getEmailSession(), toEmail, subject, body);
//            return EmailUtils.sendEmailHtml(getEmailSession(), toEmail, subject, body);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Utility method to send simple HTML email
     *
     * @param session
     * @param toEmail
     * @param subject
     * @param body
     */
    private static boolean sendEmail(Session session, String toEmail, String subject, String body) {
        try {
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("viewcoco@gmail.com", "ViewCoco"));

            msg.setReplyTo(InternetAddress.parse("viewcoco@gmail.com", false));

            msg.setSubject(subject, "UTF-8");

            msg.setText(body, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            //System.out.println("Message is ready");

            Transport.send(msg);

            //System.out.println("EMail Sent Successfully!!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Utility method to send simple HTML email
     * @param session
     * @param toEmail
     * @param subject
     * @param body
     */
    public static boolean sendEmailHtml(Session session, String toEmail, String subject, String body){
        try
        {
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("viewcoco@gmail.com", "ViewCoco"));

            msg.setReplyTo(InternetAddress.parse("viewcoco@gmail.com", false));

            msg.setSubject(subject, "UTF-8");

            //msg.setText(body, "UTF-8");
            msg.setContent(body, "text/html; charset=utf-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            //System.out.println("Message is ready");
            Transport.send(msg);

            //System.out.println("EMail Sent Successfully!!");

            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getResetPasswordEmailBody() {
        return "\n" +
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "  <head>\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                "    <title>Set up a new password for Money Manager 365</title>\n" +
                "    <!-- \n" +
                "    The style block is collapsed on page load to save you some scrolling.\n" +
                "    Postmark automatically inlines all CSS properties for maximum email client \n" +
                "    compatibility. You can just update styles here, and Postmark does the rest.\n" +
                "    -->\n" +
                "    <style type=\"text/css\" rel=\"stylesheet\" media=\"all\">\n" +
                "    /* Base ------------------------------ */\n" +
                "    \n" +
                "    *:not(br):not(tr):not(html) {\n" +
                "      font-family: Arial, 'Helvetica Neue', Helvetica, sans-serif;\n" +
                "      box-sizing: border-box;\n" +
                "    }\n" +
                "    \n" +
                "    body {\n" +
                "      width: 100% !important;\n" +
                "      height: 100%;\n" +
                "      margin: 0;\n" +
                "      line-height: 1.4;\n" +
                "      background-color: #F2F4F6;\n" +
                "      color: #74787E;\n" +
                "      -webkit-text-size-adjust: none;\n" +
                "    }\n" +
                "    \n" +
                "    p,\n" +
                "    ul,\n" +
                "    ol,\n" +
                "    blockquote {\n" +
                "      line-height: 1.4;\n" +
                "      text-align: left;\n" +
                "    }\n" +
                "    \n" +
                "    a {\n" +
                "      color: #3869D4;\n" +
                "    }\n" +
                "    \n" +
                "    a img {\n" +
                "      border: none;\n" +
                "    }\n" +
                "    /* Layout ------------------------------ */\n" +
                "    \n" +
                "    .email-wrapper {\n" +
                "      width: 100%;\n" +
                "      margin: 0;\n" +
                "      padding: 0;\n" +
                "      -premailer-width: 100%;\n" +
                "      -premailer-cellpadding: 0;\n" +
                "      -premailer-cellspacing: 0;\n" +
                "      background-color: #F2F4F6;\n" +
                "    }\n" +
                "    \n" +
                "    .email-content {\n" +
                "      width: 100%;\n" +
                "      margin: 0;\n" +
                "      padding: 0;\n" +
                "      -premailer-width: 100%;\n" +
                "      -premailer-cellpadding: 0;\n" +
                "      -premailer-cellspacing: 0;\n" +
                "    }\n" +
                "    /* Masthead ----------------------- */\n" +
                "    \n" +
                "    .email-masthead {\n" +
                "      padding: 25px 0;\n" +
                "      text-align: center;\n" +
                "    }\n" +
                "    \n" +
                "    .email-masthead_logo {\n" +
                "      width: 94px;\n" +
                "    }\n" +
                "    \n" +
                "    .email-masthead_name {\n" +
                "      font-size: 16px;\n" +
                "      font-weight: bold;\n" +
                "      color: #bbbfc3;\n" +
                "      text-decoration: none;\n" +
                "      text-shadow: 0 1px 0 white;\n" +
                "    }\n" +
                "    .title_name {\n" +
                "      font-size: 16px;\n" +
                "      font-weight: bold;\n" +
                "      color: #555555;\n" +
                "      text-decoration: none;\n" +
                "      text-shadow: 0 1px 0 white;\n" +
                "    }\n" +
                "    /* Body ------------------------------ */\n" +
                "    \n" +
                "    .email-body {\n" +
                "      width: 100%;\n" +
                "      margin: 0;\n" +
                "      padding: 0;\n" +
                "      -premailer-width: 100%;\n" +
                "      -premailer-cellpadding: 0;\n" +
                "      -premailer-cellspacing: 0;\n" +
                "      border-top: 1px solid #EDEFF2;\n" +
                "      border-bottom: 1px solid #EDEFF2;\n" +
                "      background-color: #FFFFFF;\n" +
                "    }\n" +
                "    \n" +
                "    .email-body_inner {\n" +
                "      width: 570px;\n" +
                "      margin: 0 auto;\n" +
                "      padding: 0;\n" +
                "      -premailer-width: 570px;\n" +
                "      -premailer-cellpadding: 0;\n" +
                "      -premailer-cellspacing: 0;\n" +
                "      background-color: #FFFFFF;\n" +
                "    }\n" +
                "    \n" +
                "    .email-footer {\n" +
                "      width: 570px;\n" +
                "      margin: 0 auto;\n" +
                "      padding: 0;\n" +
                "      -premailer-width: 570px;\n" +
                "      -premailer-cellpadding: 0;\n" +
                "      -premailer-cellspacing: 0;\n" +
                "      text-align: center;\n" +
                "    }\n" +
                "    \n" +
                "    .email-footer p {\n" +
                "      color: #AEAEAE;\n" +
                "    }\n" +
                "    \n" +
                "    .body-action {\n" +
                "      width: 100%;\n" +
                "      margin: 30px auto;\n" +
                "      padding: 0;\n" +
                "      -premailer-width: 100%;\n" +
                "      -premailer-cellpadding: 0;\n" +
                "      -premailer-cellspacing: 0;\n" +
                "      text-align: center;\n" +
                "    }\n" +
                "    \n" +
                "    .body-sub {\n" +
                "      margin-top: 25px;\n" +
                "      padding-top: 25px;\n" +
                "      border-top: 1px solid #EDEFF2;\n" +
                "    }\n" +
                "    \n" +
                "    .content-cell {\n" +
                "      padding: 35px;\n" +
                "    }\n" +
                "    \n" +
                "    .preheader {\n" +
                "      display: none !important;\n" +
                "    }\n" +
                "    /* Attribute list ------------------------------ */\n" +
                "    \n" +
                "    .attributes {\n" +
                "      margin: 0 0 21px;\n" +
                "    }\n" +
                "    \n" +
                "    .attributes_content {\n" +
                "      background-color: #EDEFF2;\n" +
                "      padding: 16px;\n" +
                "    }\n" +
                "    \n" +
                "    .attributes_item {\n" +
                "      padding: 0;\n" +
                "    }\n" +
                "    /* Related Items ------------------------------ */\n" +
                "    \n" +
                "    .related {\n" +
                "      width: 100%;\n" +
                "      margin: 0;\n" +
                "      padding: 25px 0 0 0;\n" +
                "      -premailer-width: 100%;\n" +
                "      -premailer-cellpadding: 0;\n" +
                "      -premailer-cellspacing: 0;\n" +
                "    }\n" +
                "    \n" +
                "    .related_item {\n" +
                "      padding: 10px 0;\n" +
                "      color: #74787E;\n" +
                "      font-size: 15px;\n" +
                "      line-height: 18px;\n" +
                "    }\n" +
                "    \n" +
                "    .related_item-title {\n" +
                "      display: block;\n" +
                "      margin: .5em 0 0;\n" +
                "    }\n" +
                "    \n" +
                "    .related_item-thumb {\n" +
                "      display: block;\n" +
                "      padding-bottom: 10px;\n" +
                "    }\n" +
                "    \n" +
                "    .related_heading {\n" +
                "      border-top: 1px solid #EDEFF2;\n" +
                "      text-align: center;\n" +
                "      padding: 25px 0 10px;\n" +
                "    }\n" +
                "    /* Discount Code ------------------------------ */\n" +
                "    \n" +
                "    .discount {\n" +
                "      width: 100%;\n" +
                "      margin: 0;\n" +
                "      padding: 24px;\n" +
                "      -premailer-width: 100%;\n" +
                "      -premailer-cellpadding: 0;\n" +
                "      -premailer-cellspacing: 0;\n" +
                "      background-color: #EDEFF2;\n" +
                "      border: 2px dashed #9BA2AB;\n" +
                "    }\n" +
                "    \n" +
                "    .discount_heading {\n" +
                "      text-align: center;\n" +
                "    }\n" +
                "    \n" +
                "    .discount_body {\n" +
                "      text-align: center;\n" +
                "      font-size: 15px;\n" +
                "    }\n" +
                "    /* Social Icons ------------------------------ */\n" +
                "    \n" +
                "    .social {\n" +
                "      width: auto;\n" +
                "    }\n" +
                "    \n" +
                "    .social td {\n" +
                "      padding: 0;\n" +
                "      width: auto;\n" +
                "    }\n" +
                "    \n" +
                "    .social_icon {\n" +
                "      height: 20px;\n" +
                "      margin: 0 8px 10px 8px;\n" +
                "      padding: 0;\n" +
                "    }\n" +
                "    /* Data table ------------------------------ */\n" +
                "    \n" +
                "    .purchase {\n" +
                "      width: 100%;\n" +
                "      margin: 0;\n" +
                "      padding: 35px 0;\n" +
                "      -premailer-width: 100%;\n" +
                "      -premailer-cellpadding: 0;\n" +
                "      -premailer-cellspacing: 0;\n" +
                "    }\n" +
                "    \n" +
                "    .purchase_content {\n" +
                "      width: 100%;\n" +
                "      margin: 0;\n" +
                "      padding: 25px 0 0 0;\n" +
                "      -premailer-width: 100%;\n" +
                "      -premailer-cellpadding: 0;\n" +
                "      -premailer-cellspacing: 0;\n" +
                "    }\n" +
                "    \n" +
                "    .purchase_item {\n" +
                "      padding: 10px 0;\n" +
                "      color: #74787E;\n" +
                "      font-size: 15px;\n" +
                "      line-height: 18px;\n" +
                "    }\n" +
                "    \n" +
                "    .purchase_heading {\n" +
                "      padding-bottom: 8px;\n" +
                "      border-bottom: 1px solid #EDEFF2;\n" +
                "    }\n" +
                "    \n" +
                "    .purchase_heading p {\n" +
                "      margin: 0;\n" +
                "      color: #9BA2AB;\n" +
                "      font-size: 12px;\n" +
                "    }\n" +
                "    \n" +
                "    .purchase_footer {\n" +
                "      padding-top: 15px;\n" +
                "      border-top: 1px solid #EDEFF2;\n" +
                "    }\n" +
                "    \n" +
                "    .purchase_total {\n" +
                "      margin: 0;\n" +
                "      text-align: right;\n" +
                "      font-weight: bold;\n" +
                "      color: #2F3133;\n" +
                "    }\n" +
                "    \n" +
                "    .purchase_total--label {\n" +
                "      padding: 0 15px 0 0;\n" +
                "    }\n" +
                "    /* Utilities ------------------------------ */\n" +
                "    \n" +
                "    .align-right {\n" +
                "      text-align: right;\n" +
                "    }\n" +
                "    \n" +
                "    .align-left {\n" +
                "      text-align: left;\n" +
                "    }\n" +
                "    \n" +
                "    .align-center {\n" +
                "      text-align: center;\n" +
                "    }\n" +
                "    /*Media Queries ------------------------------ */\n" +
                "    \n" +
                "    @media only screen and (max-width: 600px) {\n" +
                "      .email-body_inner,\n" +
                "      .email-footer {\n" +
                "        width: 100% !important;\n" +
                "      }\n" +
                "    }\n" +
                "    \n" +
                "    @media only screen and (max-width: 500px) {\n" +
                "      .button {\n" +
                "        width: 100% !important;\n" +
                "      }\n" +
                "    }\n" +
                "    /* Buttons ------------------------------ */\n" +
                "    \n" +
                "    .button {\n" +
                "      background-color: #3869D4;\n" +
                "      border-top: 10px solid #3869D4;\n" +
                "      border-right: 18px solid #3869D4;\n" +
                "      border-bottom: 10px solid #3869D4;\n" +
                "      border-left: 18px solid #3869D4;\n" +
                "      display: inline-block;\n" +
                "      color: #FFF;\n" +
                "      text-decoration: none;\n" +
                "      border-radius: 3px;\n" +
                "      box-shadow: 0 2px 3px rgba(0, 0, 0, 0.16);\n" +
                "      -webkit-text-size-adjust: none;\n" +
                "    }\n" +
                "    \n" +
                "    .button--green {\n" +
                "      background-color: #22BC66;\n" +
                "      border-top: 10px solid #22BC66;\n" +
                "      border-right: 18px solid #22BC66;\n" +
                "      border-bottom: 10px solid #22BC66;\n" +
                "      border-left: 18px solid #22BC66;\n" +
                "    }\n" +
                "    \n" +
                "    .button--red {\n" +
                "      background-color: #FF6136;\n" +
                "      border-top: 10px solid #FF6136;\n" +
                "      border-right: 18px solid #FF6136;\n" +
                "      border-bottom: 10px solid #FF6136;\n" +
                "      border-left: 18px solid #FF6136;\n" +
                "    }\n" +
                "    /* Type ------------------------------ */\n" +
                "    \n" +
                "    h1 {\n" +
                "      margin-top: 0;\n" +
                "      color: #2F3133;\n" +
                "      font-size: 19px;\n" +
                "      font-weight: bold;\n" +
                "      text-align: left;\n" +
                "    }\n" +
                "    \n" +
                "    h2 {\n" +
                "      margin-top: 0;\n" +
                "      color: #2F3133;\n" +
                "      font-size: 16px;\n" +
                "      font-weight: bold;\n" +
                "      text-align: left;\n" +
                "    }\n" +
                "    \n" +
                "    h3 {\n" +
                "      margin-top: 0;\n" +
                "      color: #2F3133;\n" +
                "      font-size: 14px;\n" +
                "      font-weight: bold;\n" +
                "      text-align: left;\n" +
                "    }\n" +
                "    \n" +
                "    p {\n" +
                "      margin-top: 0;\n" +
                "      color: #74787E;\n" +
                "      font-size: 16px;\n" +
                "      line-height: 1.5em;\n" +
                "      text-align: left;\n" +
                "    }\n" +
                "    \n" +
                "    p.sub {\n" +
                "      font-size: 12px;\n" +
                "    }\n" +
                "    \n" +
                "    p.center {\n" +
                "      text-align: center;\n" +
                "    }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <span class=\"preheader\">Use this link to reset your password. The link is only valid for 24 hours.</span>\n" +
                "    <table class=\"email-wrapper\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "      <tr>\n" +
                "        <td align=\"center\">\n" +
                "          <table class=\"email-content\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "            <tr>\n" +
                "              <td class=\"email-masthead\">\n" +
                "                <a href=\"https://www.moneymanager365.com\" class=\"title_name\">\n" +
                "        Money Manager 365\n" +
                "      </a>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "            <!-- Email Body -->\n" +
                "            <tr>\n" +
                "              <td class=\"email-body\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "                <table class=\"email-body_inner\" align=\"center\" width=\"570\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "                  <!-- Body content -->\n" +
                "                  <tr>\n" +
                "                    <td class=\"content-cell\">\n" +
                "                      <h1>Hi Dear,</h1>\n" +
                "                      <p>You recently requested to reset your password for your Money Manager 365 account. Use the button below to reset it. <strong>This password reset is only valid for the next 24 hours.</strong></p>\n" +
                "                      <!-- Action -->\n" +
                "                      <table class=\"body-action\" align=\"center\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "                        <tr>\n" +
                "                          <td align=\"center\">\n" +
                "                            <!-- Border based button\n" +
                "                       https://litmus.com/blog/a-guide-to-bulletproof-buttons-in-email-design -->\n" +
                "                            <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                              <tr>\n" +
                "                                <td align=\"center\">\n" +
                "                                  <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                    <tr>\n" +
                "                                      <td>\n" +
                "                                        <a href=\"{{action_url}}\" class=\"button button--green\" target=\"_blank\">Reset your password</a>\n" +
                "                                      <br>(Note : You must open the link from your mobile phone.)" +
                "                                      </td>\n" +
                "                                    </tr>\n" +
                "                                  </table>\n" +
                "                                </td>\n" +
                "                              </tr>\n" +
                "                            </table>\n" +
                "                          </td>\n" +
                "                        </tr>\n" +
                "                      </table>\n" +
                "                      <p>For security, this request was received from a {{platform}} device. If you did not request a password reset, please ignore this email or <a href=\"{{support_url}}\">contact support</a> if you have questions.</p>\n" +
                "                      <p>Thanks,\n" +
                "                        <br>The Money Manager 365 Team</p>\n" +
                "                      <!-- Sub copy -->\n" +
                "                      <table class=\"body-sub\">\n" +
                "                        <tr>\n" +
                "                          <td>\n" +
                "                            <p class=\"sub\">If you’re having trouble with the button above, copy and paste the URL below into your web browser. (You must be done from your mobile phone)</p>\n" +
                "                            <p class=\"sub\">{{action_url}}</p>\n" +
                "                          </td>\n" +
                "                        </tr>\n" +
                "                      </table>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </table>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "              <td>\n" +
                "                <table class=\"email-footer\" align=\"center\" width=\"570\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "                  <tr>\n" +
                "                    <td class=\"content-cell\" align=\"center\">\n" +
                "                      <p class=\"sub align-center\">&copy; 2016 Money Manager 365. All rights reserved.</p>\n" +
                "                      <p class=\"sub align-center\">\n" +
                "                        <!--[Company Name, LLC]\n" +
                "                        <br>1234 Street Rd.\n" +
                "                        <br>Suite 1234\n" +
                "                        -->" +
                "                      </p>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </table>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "          </table>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </table>\n" +
                "  </body>\n" +
                "</html>";
    }

    public static String getEmailVerifyBody(){
        return "<!DOCTYPE html\n" +
                "  PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "\n" +
                "<head>\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                "  <title>ViewCoco</title>\n" +
                "  <!-- \n" +
                "    The style block is collapsed on page load to save you some scrolling.\n" +
                "    Postmark automatically inlines all CSS properties for maximum email client \n" +
                "    compatibility. You can just update styles here, and Postmark does the rest.\n" +
                "    -->\n" +
                "  <style type=\"text/css\" rel=\"stylesheet\" media=\"all\">\n" +
                "    /* Base ------------------------------ */\n" +
                "\n" +
                "    *:not(br):not(tr):not(html) {\n" +
                "      font-family: Arial, 'Helvetica Neue', Helvetica, sans-serif;\n" +
                "      box-sizing: border-box;\n" +
                "    }\n" +
                "\n" +
                "    body {\n" +
                "      width: 100% !important;\n" +
                "      height: 100%;\n" +
                "      margin: 0;\n" +
                "      line-height: 1.4;\n" +
                "      background-color: #F2F4F6;\n" +
                "      color: #74787E;\n" +
                "      -webkit-text-size-adjust: none;\n" +
                "    }\n" +
                "\n" +
                "    p,\n" +
                "    ul,\n" +
                "    ol,\n" +
                "    blockquote {\n" +
                "      line-height: 1.4;\n" +
                "      text-align: left;\n" +
                "    }\n" +
                "\n" +
                "    a {\n" +
                "      color: #3869D4;\n" +
                "    }\n" +
                "\n" +
                "    a img {\n" +
                "      border: none;\n" +
                "    }\n" +
                "\n" +
                "    /* Layout ------------------------------ */\n" +
                "\n" +
                "    .email-wrapper {\n" +
                "      width: 100%;\n" +
                "      margin: 0;\n" +
                "      padding: 0;\n" +
                "      -premailer-width: 100%;\n" +
                "      -premailer-cellpadding: 0;\n" +
                "      -premailer-cellspacing: 0;\n" +
                "      background-color: #F2F4F6;\n" +
                "    }\n" +
                "\n" +
                "    .email-content {\n" +
                "      width: 100%;\n" +
                "      margin: 0;\n" +
                "      padding: 0;\n" +
                "      -premailer-width: 100%;\n" +
                "      -premailer-cellpadding: 0;\n" +
                "      -premailer-cellspacing: 0;\n" +
                "    }\n" +
                "\n" +
                "    /* Masthead ----------------------- */\n" +
                "\n" +
                "    .email-masthead {\n" +
                "      padding: 25px 0;\n" +
                "      text-align: center;\n" +
                "    }\n" +
                "\n" +
                "    .email-masthead_logo {\n" +
                "      width: 94px;\n" +
                "    }\n" +
                "\n" +
                "    .email-masthead_name {\n" +
                "      font-size: 16px;\n" +
                "      font-weight: bold;\n" +
                "      color: #bbbfc3;\n" +
                "      text-decoration: none;\n" +
                "      text-shadow: 0 1px 0 white;\n" +
                "    }\n" +
                "\n" +
                "    .title_name {\n" +
                "      font-size: 16px;\n" +
                "      font-weight: bold;\n" +
                "      color: #555555;\n" +
                "      text-decoration: none;\n" +
                "      text-shadow: 0 1px 0 white;\n" +
                "    }\n" +
                "\n" +
                "    /* Body ------------------------------ */\n" +
                "\n" +
                "    .email-body {\n" +
                "      width: 100%;\n" +
                "      margin: 0;\n" +
                "      padding: 0;\n" +
                "      -premailer-width: 100%;\n" +
                "      -premailer-cellpadding: 0;\n" +
                "      -premailer-cellspacing: 0;\n" +
                "      border-top: 1px solid #EDEFF2;\n" +
                "      border-bottom: 1px solid #EDEFF2;\n" +
                "      background-color: #FFFFFF;\n" +
                "    }\n" +
                "\n" +
                "    .email-body_inner {\n" +
                "      width: 570px;\n" +
                "      margin: 0 auto;\n" +
                "      padding: 0;\n" +
                "      -premailer-width: 570px;\n" +
                "      -premailer-cellpadding: 0;\n" +
                "      -premailer-cellspacing: 0;\n" +
                "      background-color: #FFFFFF;\n" +
                "    }\n" +
                "\n" +
                "    .email-footer {\n" +
                "      width: 570px;\n" +
                "      margin: 0 auto;\n" +
                "      padding: 0;\n" +
                "      -premailer-width: 570px;\n" +
                "      -premailer-cellpadding: 0;\n" +
                "      -premailer-cellspacing: 0;\n" +
                "      text-align: center;\n" +
                "    }\n" +
                "\n" +
                "    .email-footer p {\n" +
                "      color: #AEAEAE;\n" +
                "    }\n" +
                "\n" +
                "    .body-action {\n" +
                "      width: 100%;\n" +
                "      margin: 30px auto;\n" +
                "      padding: 0;\n" +
                "      -premailer-width: 100%;\n" +
                "      -premailer-cellpadding: 0;\n" +
                "      -premailer-cellspacing: 0;\n" +
                "      text-align: center;\n" +
                "    }\n" +
                "\n" +
                "    .body-sub {\n" +
                "      margin-top: 25px;\n" +
                "      padding-top: 25px;\n" +
                "      border-top: 1px solid #EDEFF2;\n" +
                "    }\n" +
                "\n" +
                "    .content-cell {\n" +
                "      padding: 35px;\n" +
                "    }\n" +
                "\n" +
                "    .preheader {\n" +
                "      display: none !important;\n" +
                "    }\n" +
                "\n" +
                "    /* Attribute list ------------------------------ */\n" +
                "\n" +
                "    .attributes {\n" +
                "      margin: 0 0 21px;\n" +
                "    }\n" +
                "\n" +
                "    .attributes_content {\n" +
                "      background-color: #EDEFF2;\n" +
                "      padding: 16px;\n" +
                "    }\n" +
                "\n" +
                "    .attributes_item {\n" +
                "      padding: 0;\n" +
                "    }\n" +
                "\n" +
                "    /* Related Items ------------------------------ */\n" +
                "\n" +
                "    .related {\n" +
                "      width: 100%;\n" +
                "      margin: 0;\n" +
                "      padding: 25px 0 0 0;\n" +
                "      -premailer-width: 100%;\n" +
                "      -premailer-cellpadding: 0;\n" +
                "      -premailer-cellspacing: 0;\n" +
                "    }\n" +
                "\n" +
                "    .related_item {\n" +
                "      padding: 10px 0;\n" +
                "      color: #74787E;\n" +
                "      font-size: 15px;\n" +
                "      line-height: 18px;\n" +
                "    }\n" +
                "\n" +
                "    .related_item-title {\n" +
                "      display: block;\n" +
                "      margin: .5em 0 0;\n" +
                "    }\n" +
                "\n" +
                "    .related_item-thumb {\n" +
                "      display: block;\n" +
                "      padding-bottom: 10px;\n" +
                "    }\n" +
                "\n" +
                "    .related_heading {\n" +
                "      border-top: 1px solid #EDEFF2;\n" +
                "      text-align: center;\n" +
                "      padding: 25px 0 10px;\n" +
                "    }\n" +
                "\n" +
                "    /* Discount Code ------------------------------ */\n" +
                "\n" +
                "    .discount {\n" +
                "      width: 100%;\n" +
                "      margin: 0;\n" +
                "      padding: 24px;\n" +
                "      -premailer-width: 100%;\n" +
                "      -premailer-cellpadding: 0;\n" +
                "      -premailer-cellspacing: 0;\n" +
                "      background-color: #EDEFF2;\n" +
                "      border: 2px dashed #9BA2AB;\n" +
                "    }\n" +
                "\n" +
                "    .discount_heading {\n" +
                "      text-align: center;\n" +
                "    }\n" +
                "\n" +
                "    .discount_body {\n" +
                "      text-align: center;\n" +
                "      font-size: 15px;\n" +
                "    }\n" +
                "\n" +
                "    /* Social Icons ------------------------------ */\n" +
                "\n" +
                "    .social {\n" +
                "      width: auto;\n" +
                "    }\n" +
                "\n" +
                "    .social td {\n" +
                "      padding: 0;\n" +
                "      width: auto;\n" +
                "    }\n" +
                "\n" +
                "    .social_icon {\n" +
                "      height: 20px;\n" +
                "      margin: 0 8px 10px 8px;\n" +
                "      padding: 0;\n" +
                "    }\n" +
                "\n" +
                "    /* Data table ------------------------------ */\n" +
                "\n" +
                "    .purchase {\n" +
                "      width: 100%;\n" +
                "      margin: 0;\n" +
                "      padding: 35px 0;\n" +
                "      -premailer-width: 100%;\n" +
                "      -premailer-cellpadding: 0;\n" +
                "      -premailer-cellspacing: 0;\n" +
                "    }\n" +
                "\n" +
                "    .purchase_content {\n" +
                "      width: 100%;\n" +
                "      margin: 0;\n" +
                "      padding: 25px 0 0 0;\n" +
                "      -premailer-width: 100%;\n" +
                "      -premailer-cellpadding: 0;\n" +
                "      -premailer-cellspacing: 0;\n" +
                "    }\n" +
                "\n" +
                "    .purchase_item {\n" +
                "      padding: 10px 0;\n" +
                "      color: #74787E;\n" +
                "      font-size: 15px;\n" +
                "      line-height: 18px;\n" +
                "    }\n" +
                "\n" +
                "    .purchase_heading {\n" +
                "      padding-bottom: 8px;\n" +
                "      border-bottom: 1px solid #EDEFF2;\n" +
                "    }\n" +
                "\n" +
                "    .purchase_heading p {\n" +
                "      margin: 0;\n" +
                "      color: #9BA2AB;\n" +
                "      font-size: 12px;\n" +
                "    }\n" +
                "\n" +
                "    .purchase_footer {\n" +
                "      padding-top: 15px;\n" +
                "      border-top: 1px solid #EDEFF2;\n" +
                "    }\n" +
                "\n" +
                "    .purchase_total {\n" +
                "      margin: 0;\n" +
                "      text-align: right;\n" +
                "      font-weight: bold;\n" +
                "      color: #2F3133;\n" +
                "    }\n" +
                "\n" +
                "    .purchase_total--label {\n" +
                "      padding: 0 15px 0 0;\n" +
                "    }\n" +
                "\n" +
                "    /* Utilities ------------------------------ */\n" +
                "\n" +
                "    .align-right {\n" +
                "      text-align: right;\n" +
                "    }\n" +
                "\n" +
                "    .align-left {\n" +
                "      text-align: left;\n" +
                "    }\n" +
                "\n" +
                "    .align-center {\n" +
                "      text-align: center;\n" +
                "    }\n" +
                "\n" +
                "    /*Media Queries ------------------------------ */\n" +
                "\n" +
                "    @media only screen and (max-width: 600px) {\n" +
                "\n" +
                "      .email-body_inner,\n" +
                "      .email-footer {\n" +
                "        width: 100% !important;\n" +
                "      }\n" +
                "    }\n" +
                "\n" +
                "    @media only screen and (max-width: 500px) {\n" +
                "      .button {\n" +
                "        width: 100% !important;\n" +
                "      }\n" +
                "    }\n" +
                "\n" +
                "    /* Buttons ------------------------------ */\n" +
                "\n" +
                "    .button {\n" +
                "      background-color: #3869D4;\n" +
                "      border-top: 10px solid #3869D4;\n" +
                "      border-right: 18px solid #3869D4;\n" +
                "      border-bottom: 10px solid #3869D4;\n" +
                "      border-left: 18px solid #3869D4;\n" +
                "      display: inline-block;\n" +
                "      color: #FFF;\n" +
                "      text-decoration: none;\n" +
                "      border-radius: 3px;\n" +
                "      box-shadow: 0 2px 3px rgba(0, 0, 0, 0.16);\n" +
                "      -webkit-text-size-adjust: none;\n" +
                "    }\n" +
                "\n" +
                "    .button--green {\n" +
                "      background-color: #22BC66;\n" +
                "      border-top: 10px solid #22BC66;\n" +
                "      border-right: 18px solid #22BC66;\n" +
                "      border-bottom: 10px solid #22BC66;\n" +
                "      border-left: 18px solid #22BC66;\n" +
                "    }\n" +
                "\n" +
                "    .button--red {\n" +
                "      background-color: #FF6136;\n" +
                "      border-top: 10px solid #FF6136;\n" +
                "      border-right: 18px solid #FF6136;\n" +
                "      border-bottom: 10px solid #FF6136;\n" +
                "      border-left: 18px solid #FF6136;\n" +
                "    }\n" +
                "\n" +
                "    /* Type ------------------------------ */\n" +
                "\n" +
                "    h1 {\n" +
                "      margin-top: 0;\n" +
                "      color: #2F3133;\n" +
                "      font-size: 19px;\n" +
                "      font-weight: bold;\n" +
                "      text-align: left;\n" +
                "    }\n" +
                "\n" +
                "    h2 {\n" +
                "      margin-top: 0;\n" +
                "      color: #2F3133;\n" +
                "      font-size: 16px;\n" +
                "      font-weight: bold;\n" +
                "      text-align: left;\n" +
                "    }\n" +
                "\n" +
                "    h3 {\n" +
                "      margin-top: 0;\n" +
                "      color: #2F3133;\n" +
                "      font-size: 14px;\n" +
                "      font-weight: bold;\n" +
                "      text-align: left;\n" +
                "    }\n" +
                "\n" +
                "    p {\n" +
                "      margin-top: 0;\n" +
                "      color: #74787E;\n" +
                "      font-size: 16px;\n" +
                "      line-height: 1.5em;\n" +
                "      text-align: left;\n" +
                "    }\n" +
                "\n" +
                "    p.sub {\n" +
                "      font-size: 12px;\n" +
                "    }\n" +
                "\n" +
                "    p.center {\n" +
                "      text-align: center;\n" +
                "    }\n" +
                "  </style>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "  <span class=\"preheader\">Use this link to reset your password. The link is only valid for 24 hours.</span>\n" +
                "  <table class=\"email-wrapper\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "    <tr>\n" +
                "      <td align=\"center\">\n" +
                "        <table class=\"email-content\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "          <tr>\n" +
                "            <td class=\"email-masthead\">\n" +
                "              <a href=\"http://www.viewcoco.com\" class=\"title_name\">\n" +
                "                ViewCoco\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "          <!-- Email Body -->\n" +
                "          <tr>\n" +
                "            <td class=\"email-body\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "              <table class=\"email-body_inner\" align=\"center\" width=\"570\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "                <!-- Body content -->\n" +
                "                <tr>\n" +
                "                  <td class=\"content-cell\">\n" +
                "                    <h1>Hi Dear,</h1>\n" +
                "                    <p>Congratulation, you've successfully registered the account, click the link below to verify your email.\n" +
                "                    <!-- Action -->\n" +
                "                    <table class=\"body-action\" align=\"center\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "                      <tr>\n" +
                "                        <td align=\"center\">\n" +
                "                          <!-- Border based button\n" +
                "                       https://litmus.com/blog/a-guide-to-bulletproof-buttons-in-email-design -->\n" +
                "                          <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                            <tr>\n" +
                "                              <td align=\"center\">\n" +
                "                                <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                  <tr>\n" +
                "                                    <td>\n" +
                "                                      <a href=\"{{action_url}}\" class=\"button button--green\" target=\"_blank\">Email Verify</a>\n" +
                "                                    </td>\n" +
                "                                  </tr>\n" +
                "                                </table>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </table>\n" +
                "                        </td>\n" +
                "                      </tr>\n" +
                "                    </table>\n" +
                "                    \n" +
                "                    <p>Thanks,\n" +
                "                      <br>ViewCoco\n" +
                "                    </p>\n" +
                "                    <!-- Sub copy -->\n" +
                "                    <table class=\"body-sub\">\n" +
                "                      <tr>\n" +
                "                        <td>\n" +
                "                          <p class=\"sub\">If you’re having trouble with the button above, copy and paste the URL below\n" +
                "                            into your web browser.</p>\n" +
                "                          <p class=\"sub\">{{action_url}}</p>\n" +
                "                        </td>\n" +
                "                      </tr>\n" +
                "                    </table>\n" +
                "                  </td>\n" +
                "                </tr>\n" +
                "              </table>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "          <tr>\n" +
                "            <td>\n" +
                "              <table class=\"email-footer\" align=\"center\" width=\"570\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "                <tr>\n" +
                "                  <td class=\"content-cell\" align=\"center\">\n" +
                "                    <p class=\"sub align-center\">&copy; 2021 ViewCoco. All rights reserved.</p>\n" +
                "                    <!--                      \n" +
                "                        <p class=\"sub align-center\">\n" +
                "                            [Company Name, LLC]\n" +
                "                            <br>1234 Street Rd.\n" +
                "                            <br>Suite 1234\n" +
                "                        </p>\n" +
                "                      -->\n" +
                "                  </td>\n" +
                "                </tr>\n" +
                "              </table>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </table>\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </table>\n" +
                "</body>\n" +
                "\n" +
                "</html>\n" +
                "\n";
    }
}
