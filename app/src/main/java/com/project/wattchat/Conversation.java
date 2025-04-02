package com.project.wattchat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import crypt.Crypt;
import phonebook.OneComment;
import phonebook.DiscussArrayAdapter;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


public class Conversation extends Activity {
    static ListView conversationList;
    static DiscussArrayAdapter adapter;
    static String phoneNumber, personName;
    EditText message;
    ImageButton sentMessage;
    public Context mContext;
    static Crypt cryptObject=new Crypt();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);
        mContext=this;
        sentMessage=(ImageButton) findViewById(R.id.sentMessage);
        message=(EditText) findViewById(R.id.message);
        conversationList=(ListView) findViewById(R.id.conversationList);
        Bundle phoneData=getIntent().getExtras();
        phoneNumber=phoneData.getString("phoneNumber");
        personName=phoneData.getString("personName");
        this.setTitle(personName);
        /*telefon numarası önünde "+9" varsa, kaldırılır.*/
        if (phoneNumber.substring(0,2).equals("+9"))
            phoneNumber=phoneNumber.substring(2);
        adapter=new DiscussArrayAdapter(getApplicationContext(), R.layout.listitem_discuss);
        /* gelen kutusuna erişim Uri'si */
        Uri inboxUri=Uri.parse("content://sms/");
        /* alınacak olan bilgiler: mesajı gönderen, mesaj içeriği, tarih ve mesajın gelen bir mesaj mı yoksa giden bir
        *mesaj mı olduğunu belirten ip bilgisi */
        String[] columns = new String[] {"address", "body","date", "type"};
        /* şifreli mesajlar veritabanından çekilir ve deşifreleme işleminin ardından ekranda listelenir.*/
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(inboxUri, columns, "address like '%"+"phoneNumber"  + "%'", null, "date ASC");
        while (c.moveToNext()) {
            String body = c.getString(c.getColumnIndexOrThrow("body"));
            String date = c.getString(c.getColumnIndexOrThrow("date"));
            String type = c.getString(c.getColumnIndexOrThrow("type"));
            if (body.trim().length()!=0) {
                try {
                    String decryptedText = cryptObject.decrypt(body);
                    if (decryptedText.trim().length()!=0) {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd, MMM HH:mm");
                        date=formatter.format(new Date(Long.parseLong(date)));
                        /*mesaj giden bir mesaj ise farklı bir tasarımda, görüntülenmesini sağlar. Bu tasarım baloncuk tasarımıdır.*/
                        if (type.contains("1"))
                            adapter.add(new OneComment(true, decryptedText+"\n"+date));
                        else
                            adapter.add(new OneComment(false, decryptedText+"\n"+date));
                    }
                } catch (Exception e) {
                }
            }
        }
        conversationList.setAdapter(adapter);
        conversationList.setSelection(adapter.getCount()-1);
        sentMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    /*Mesaj gönderilmeden önce şifreleme işlemi gerçekleştirilir.*/
                    String encryptedText = cryptObject.encrypt(message.getText().toString());
                    /*mesaj gönderilir.*/
                    smsManager.sendTextMessage(phoneNumber, null, encryptedText, null, null);
                    SimpleDateFormat formatter = new SimpleDateFormat("dd, MMM HH:mm");
                    String date = formatter.format(Calendar.getInstance().getTime());
                    /*gönderilen mesaj ekrandaki listeye eklenir.*/
                    adapter.add(new OneComment(false, message.getText().toString().trim()+"\n"+date));
                    conversationList.setAdapter(adapter);
                    conversationList.setSelection(adapter.getCount()-1);
                    message.setText("");
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "SMS faild, please try again later!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    /*bu metod yeni bir mesaj geldiğinde tetiklenir. Gelen mesaj önceden deşifre edilir ardından ekranda listelenir.*/
    public static void updateAdapter(String message, String type, String phoneNo) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd, MMM HH:mm");
        String date = formatter.format(Calendar.getInstance().getTime());
        String decryptedText = cryptObject.decrypt(message);
        if (phoneNo.contains(phoneNumber)) {
            if (type.contains("1"))
                adapter.add(new OneComment(true, decryptedText+"\n"+date));
            else
                adapter.add(new OneComment(false, decryptedText+"\n"+date));
            conversationList.setAdapter(adapter);
            conversationList.setSelection(adapter.getCount()-1);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.conversation_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.call:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+phoneNumber));
                startActivity(callIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
