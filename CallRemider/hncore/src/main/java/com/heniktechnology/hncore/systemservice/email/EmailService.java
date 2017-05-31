package com.heniktechnology.hncore.systemservice.email;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import com.heniktechnology.hncore.utility.HNCoreConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static android.R.attr.id;

/**
 * Created by NikhilNagpure on 20-01-2017.
 */

public class EmailService extends IntentService {
    public static String EMAIL = "email";
    private String SEND_MULTIPLE = "android.intent.action.SEND_MULTIPLE";
    private String SEND = "android.intent.action.SEND";
    private String EMAIL_INTENT = "android.intent.extra.EMAIL";
    private String SUBJECT = "android.intent.extra.SUBJECT";
    private String TEXT = "android.intent.extra.TEXT";
    private String PLAIN_TEXT = "plain/text";
    private Email email;
    private File reportFiles;
    private Uri exceptionFileUri;
    private File reportFile;
    private FileChannel sourceChannel;
    private FileChannel destinationChannel;

    public EmailService(String name, @NonNull Email email) {
        super("Email_Service");
        this.email = email;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        {
            if (email == null) {
                email = (Email) intent.getSerializableExtra(EMAIL);
            }
            ArrayList temporaryFiles = new ArrayList();
            if (intent != null) {
                try {
                    ArrayList exception = new ArrayList();
                    Intent emailIntent;
                    //with or without attachment
                    if (email.getAttachmnetfilePaths() != null && email.getAttachmnetfilePaths().length != 0) {
                        emailIntent = new Intent(SEND_MULTIPLE);
                    } else {
                        emailIntent = new Intent(SEND);
                    }
                    emailIntent.putExtra(EMAIL_INTENT, email.getToPerson());
                    emailIntent.putExtra(SUBJECT, email.getMailSubject());
                    emailIntent.putExtra(TEXT, HNCoreConstants.NEXT_LINE_STRING + email.getMailBody());
                    emailIntent.setType(PLAIN_TEXT);
/*                    if(emailIntent.resolveActivity(getPackageManager()) != null)
                    {



                    }*/


                    PackageManager packageManager = this.getPackageManager();
                    List matches = packageManager.queryIntentActivities(emailIntent, 0);
                    ResolveInfo best = null;
                    Iterator var15 = matches.iterator();

                    while (true) {
                        ResolveInfo notificationManager;
                        do {
                            if (!var15.hasNext()) {
                                if (best != null) {
                                    emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                                }
                                this.exceptionFileUri = FileProvider.getUriForFile(this, this.getPackageName(), this.reportFile);
                                exception.add(this.exceptionFileUri);
                                if (email.getFilePath() != null) {
                                    String[] var17 = email.getAttachmnetfilePaths();
                                    int var16 = email.getAttachmnetfilePaths().length;

                                    for (int var31 = 0; var31 < var16; ++var31) {
                                        String var29 = var17[var31];
                                        if (var29 != null) {
                                            File attachmentFile;
                                            Uri attachmentFileUri;
                                            if (this.isFileInRootDir(var29)) {
                                                attachmentFile = this.getFileStoredInInternalStorage(var29);
                                                temporaryFiles.add(attachmentFile);
                                                if (attachmentFile != null && attachmentFile.exists()) {
                                                    attachmentFileUri = FileProvider.getUriForFile(this, this.getPackageName(), attachmentFile);
                                                    exception.add(attachmentFileUri);
                                                }
                                            } else {
                                                attachmentFile = new File(var29);
                                                if (attachmentFile != null && attachmentFile.exists()) {
                                                    attachmentFileUri = Uri.fromFile(attachmentFile);
                                                    exception.add(attachmentFileUri);
                                                }
                                            }
                                        }
                                    }
                                }

                                if (exception.size() > 1) {
                                    emailIntent.putParcelableArrayListExtra("android.intent.extra.STREAM", exception);
                                } else {
                                    emailIntent.putExtra("android.intent.extra.STREAM", this.exceptionFileUri);
                                }

                                emailIntent.setFlags(268435456);
                                this.getApplicationContext().startActivity(emailIntent);
                                NotificationManager var30 = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);//"notification");
                                var30.cancel(id);
                                Thread.sleep(60000L);
                                return;
                            }

                            notificationManager = (ResolveInfo) var15.next();
                        }
                        while (!notificationManager.activityInfo.packageName.endsWith(".gm") && !notificationManager.activityInfo.name.toLowerCase(Locale.US).contains("gmail"));

                        best = notificationManager;
                    }
                } catch (InterruptedException var26) {
                   /* if(GenericExceptionHandler.sharedInstance(this).isLogEnable) {
                        debug(var26, new String[0]);
                    }*/
                } catch (Exception var27) {
                    /*if(GenericExceptionHandler.sharedInstance(this).isLogEnable) {
                        debug(var27, new String[0]);
                    }*/
                } finally {
                    this.reportFile.delete();
                    Iterator var22 = temporaryFiles.iterator();

                    while (true) {
                        if (!var22.hasNext()) {
                            ;
                        } else {
                            File deleteFile = (File) var22.next();
                            if (deleteFile != null) {
                                /*if(GenericExceptionHandler.sharedInstance(this).isLogEnable) {
                                    MobiculeLogger.debug(new String[]{"Deleting File :", deleteFile.getName()});
                                }

                                deleteFile.delete();*/
                            }
                        }
                    }
                }
            }

        }
    }

    private File getFileStoredInInternalStorage(String internalFilePath) {
        File temporaryFile = null;
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {
            File exception = new File(internalFilePath);
            if (exception.exists()) {
                String fileName = exception.getName();
                String reportFilePath = this.getApplicationContext().getFilesDir().getAbsolutePath() + File.separator;
                File reportFile = new File(reportFilePath, "/Exception");
                temporaryFile = new File(reportFile, fileName);
                inputStream = new FileInputStream(exception);
                outputStream = new FileOutputStream(temporaryFile);
                this.sourceChannel = inputStream.getChannel();
                this.destinationChannel = outputStream.getChannel();
                this.destinationChannel.transferFrom(this.sourceChannel, 0L, this.sourceChannel.size());
            }
        } catch (Exception var17) {
            /*if(GenericExceptionHandler.sharedInstance(this).isLogEnable) {
                debug(var17, new String[0]);
            }*/
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }

                if (this.sourceChannel != null && this.destinationChannel != null) {
                    this.sourceChannel.close();
                    this.destinationChannel.close();
                }
            } catch (Exception var16) {
                /*if(GenericExceptionHandler.sharedInstance(this).isLogEnable) {
                    debug(var16, new String[0]);
                }*/
            }

        }

        return temporaryFile;
    }

    private boolean isFileInRootDir(String attachFilePath) {
        return attachFilePath.contains("/data/data");
    }
}
