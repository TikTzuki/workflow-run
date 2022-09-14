// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.context.Context;
import org.apache.commons.mail.SimpleEmail;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.delegate.Expression;

public class MailActivityBehavior extends AbstractBpmnActivityBehavior
{
    protected static final BpmnBehaviorLogger LOG;
    protected Expression to;
    protected Expression from;
    protected Expression cc;
    protected Expression bcc;
    protected Expression subject;
    protected Expression text;
    protected Expression html;
    protected Expression charset;
    
    @Override
    public void execute(final ActivityExecution execution) {
        final String toStr = this.getStringFromField(this.to, execution);
        final String fromStr = this.getStringFromField(this.from, execution);
        final String ccStr = this.getStringFromField(this.cc, execution);
        final String bccStr = this.getStringFromField(this.bcc, execution);
        final String subjectStr = this.getStringFromField(this.subject, execution);
        final String textStr = this.getStringFromField(this.text, execution);
        final String htmlStr = this.getStringFromField(this.html, execution);
        final String charSetStr = this.getStringFromField(this.charset, execution);
        final Email email = this.createEmail(textStr, htmlStr);
        this.addTo(email, toStr);
        this.setFrom(email, fromStr);
        this.addCc(email, ccStr);
        this.addBcc(email, bccStr);
        this.setSubject(email, subjectStr);
        this.setMailServerProperties(email);
        this.setCharset(email, charSetStr);
        try {
            email.send();
        }
        catch (EmailException e) {
            throw MailActivityBehavior.LOG.sendingEmailException(toStr, (Throwable)e);
        }
        this.leave(execution);
    }
    
    protected Email createEmail(final String text, final String html) {
        if (html != null) {
            return (Email)this.createHtmlEmail(text, html);
        }
        if (text != null) {
            return (Email)this.createTextOnlyEmail(text);
        }
        throw MailActivityBehavior.LOG.emailFormatException();
    }
    
    protected HtmlEmail createHtmlEmail(final String text, final String html) {
        final HtmlEmail email = new HtmlEmail();
        try {
            email.setHtmlMsg(html);
            if (text != null) {
                email.setTextMsg(text);
            }
            return email;
        }
        catch (EmailException e) {
            throw MailActivityBehavior.LOG.emailCreationException("HTML", (Throwable)e);
        }
    }
    
    protected SimpleEmail createTextOnlyEmail(final String text) {
        final SimpleEmail email = new SimpleEmail();
        try {
            email.setMsg(text);
            return email;
        }
        catch (EmailException e) {
            throw MailActivityBehavior.LOG.emailCreationException("text-only", (Throwable)e);
        }
    }
    
    protected void addTo(final Email email, final String to) {
        final String[] tos = this.splitAndTrim(to);
        if (tos != null) {
            for (final String t : tos) {
                try {
                    email.addTo(t);
                }
                catch (EmailException e) {
                    throw MailActivityBehavior.LOG.addRecipientException(t, (Throwable)e);
                }
            }
            return;
        }
        throw MailActivityBehavior.LOG.missingRecipientsException();
    }
    
    protected void setFrom(final Email email, final String from) {
        String fromAddress = null;
        if (from != null) {
            fromAddress = from;
        }
        else {
            fromAddress = Context.getProcessEngineConfiguration().getMailServerDefaultFrom();
        }
        try {
            email.setFrom(fromAddress);
        }
        catch (EmailException e) {
            throw MailActivityBehavior.LOG.addSenderException(from, (Throwable)e);
        }
    }
    
    protected void addCc(final Email email, final String cc) {
        final String[] ccs = this.splitAndTrim(cc);
        if (ccs != null) {
            for (final String c : ccs) {
                try {
                    email.addCc(c);
                }
                catch (EmailException e) {
                    throw MailActivityBehavior.LOG.addCcException(c, (Throwable)e);
                }
            }
        }
    }
    
    protected void addBcc(final Email email, final String bcc) {
        final String[] bccs = this.splitAndTrim(bcc);
        if (bccs != null) {
            for (final String b : bccs) {
                try {
                    email.addBcc(b);
                }
                catch (EmailException e) {
                    throw MailActivityBehavior.LOG.addBccException(b, (Throwable)e);
                }
            }
        }
    }
    
    protected void setSubject(final Email email, final String subject) {
        email.setSubject((subject != null) ? subject : "");
    }
    
    protected void setMailServerProperties(final Email email) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        final String host = processEngineConfiguration.getMailServerHost();
        EnsureUtil.ensureNotNull("Could not send email: no SMTP host is configured", "host", host);
        email.setHostName(host);
        final int port = processEngineConfiguration.getMailServerPort();
        email.setSmtpPort(port);
        email.setTLS(processEngineConfiguration.getMailServerUseTLS());
        final String user = processEngineConfiguration.getMailServerUsername();
        final String password = processEngineConfiguration.getMailServerPassword();
        if (user != null && password != null) {
            email.setAuthentication(user, password);
        }
    }
    
    protected void setCharset(final Email email, final String charSetStr) {
        if (this.charset != null) {
            email.setCharset(charSetStr);
        }
    }
    
    protected String[] splitAndTrim(final String str) {
        if (str != null) {
            final String[] splittedStrings = str.split(",");
            for (int i = 0; i < splittedStrings.length; ++i) {
                splittedStrings[i] = splittedStrings[i].trim();
            }
            return splittedStrings;
        }
        return null;
    }
    
    protected String getStringFromField(final Expression expression, final DelegateExecution execution) {
        if (expression != null) {
            final Object value = expression.getValue(execution);
            if (value != null) {
                return value.toString();
            }
        }
        return null;
    }
    
    static {
        LOG = ProcessEngineLogger.BPMN_BEHAVIOR_LOGGER;
    }
}
