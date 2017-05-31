package com.heniktechnology.hncore.systemservice.email;


import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.heniktechnology.hncore.utility.HNCoreConstants;

import java.io.Serializable;

/**
 * Created by NikhilNagpure on 20-01-2017.
 */

public class Email implements Serializable {

    private String mailSubject;
    private String mailBody;
    private String[] toPerson;
    private String filePath;
    private String[] attachmnetfilePaths;
    private int id = 0;

    private Email(Builder builder) {
        this.mailSubject = builder.mailSubject;
        this.mailBody = builder.mailBody;
        this.toPerson = builder.toPerson;
        this.filePath = builder.filePath;
        this.attachmnetfilePaths = builder.attachmnetfilePaths;
        this.id = builder.id;
    }


    public String getMailSubject() {
        return mailSubject;
    }

    public String getMailBody() {
        return mailBody;
    }

    public String[] getToPerson() {
        return toPerson;
    }

    public String getFilePath() {
        return filePath;
    }

    public String[] getAttachmnetfilePaths() {
        return attachmnetfilePaths;
    }

    public int getId() {
        return id;
    }

    public boolean sendEmail(Context context)
    {
        Intent emailIntentService = new Intent(context, EmailService.class);
        ComponentName componentName=context.startService(emailIntentService);
        //if(componentName.getClassName() == "Email_Service")
        {
            return true;
        }
        //return  false;
    }

   public static class Builder {

        String mailSubject = HNCoreConstants.EMPTY_STRING;
        String mailBody = HNCoreConstants.EMPTY_STRING;
        String[] toPerson;
        String filePath = HNCoreConstants.EMPTY_STRING;
        String[] attachmnetfilePaths;
        int id = 0;

        public Email.Builder setMailSubject(String mailSubject) {
            this.mailSubject = mailSubject;
            return this;
        }

        public Email.Builder setMailBody(String mailBody) {
            this.mailBody = mailBody;
            return this;
        }

        public Email.Builder setToPerson(String...toPerson) {
            this.toPerson = toPerson;
            return this;
        }

        public Email.Builder setFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Email.Builder setAttachmnetfilePaths(String[] attachmnetfilePaths) {
            this.attachmnetfilePaths = attachmnetfilePaths;
            return this;
        }

        public Email.Builder setId(int id) {
            this.id = id;
            return this;
        }
        public Email build()
        {
            return  new Email(this);
        }
    }
}
