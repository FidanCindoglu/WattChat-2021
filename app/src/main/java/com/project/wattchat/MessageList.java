package com.project.wattchat;

import java.util.ArrayList;
import java.util.List;
import crypt.Crypt;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.Telephony;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class MessageList extends Activity {

    SimpleCursorAdapter adapter;
    ListView messageList;
    List<String>persons;
    List<String>phones;
    Crypt cryptObject=new Crypt();
    ContentResolver cr;
    Cursor c;
    Uri inboxURI;
    String[] columns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageList=(ListView) findViewById(R.id.listView1);
        /*mesaj listesi boş ise bu duruma dair bir yazı görüntülenir.*/
        messageList.setEmptyView(findViewById(R.id.empty));
        /*gelen kutusuna erişim uri'si*/
        inboxURI=Uri.parse("content://sms/");
        /*alınacak olan bilgiler: id, mesajı gönderen, mesaj içeriği ve okunup okunmadı bilgisi*/
        columns=new String[] {"_id", "address", "body", "read"};
        cr=getContentResolver();
        /*bu metod sayesinde daha önceden mesajlaşılmış kişiler listelenir*/
        adapterLoad();
        messageList.setOnItemClickListener(new onItemClickListener() {
            /* liste üzerinde seçilen kişiile mesajlaşmak için conversation sayfasına yönlendirme yapılır.*/
            @Override
            public void onItemClick(AdapterView<?>arg0, View arg1, int position, long arg3) {
                try {
                    Bundle phoneData=new Bundle();
                    phoneData.putString("phoneNumber", phones.get(position)+"");
                    phoneData.putString("personName", persons.get(position)+"");
                    Intent intentObject=new Intent();
                    intentObject.setClass(MessageList.this, Conversation.class);
                    intentObject.putExtras(phoneData);
                    startActivity(intentObject);
                } catch (Exception e) {}
            }
        } );
    }
    /* bu metod veritabanından mesjları çekerek, şifreli mesajların ekranda görüntülenmesini sağlar*/
    public void adapterLoad() {
        persons=new ArrayList<String>();
        phones=new ArrayList<String>();
        messageList.setAdapter(null);
        c=cr.query(inboxURI, columns, null, null, null);
        while (c.moveToNext()) {
            try {
                String body=c.getString(c.getColumnIndexOrThrow("body"));
                String message=cryptObject.decrypt(body);
                if (message.length()!=0) {
                    String senderPhone=c.getString(c.getColumnIndexOrThrow("address"));
                    String senderName=getContactName(senderPhone);
                if (senderName.trim().length()==0 && !persons.contains(senderPhone))
                {
                    persons.add(senderPhone);
                    phones.add(senderPhone);
                }
                if (senderName.trim().length()!=0 && !persons.contains(senderName))
                {
                    persons.add(senderName);
                    phones.add(senderName);
                }
                }
            } catch (Exception e) {}
        }
        messageList.setAdapter(new ArrayAdapter<String>(MessageList.this, R.layout.list, R.id.name, persons));
        messageList.setFastScrollEnabled(true);
    }
    /* bu metod içerisine gelen telefon numarası bilgisine sahip olan kişinin adını döndürür.*/
    public String getContactName(final String phoneNumber) {
        Uri uri;
        String[] projection;
        Uri mBaseURi=Contacts.Phones.CONTENT_FILTER_URL;
        projection=new String[] {android.provider.Contacts.People.NAME};
        try {
            Class<?>c=Class.forName("android.provider.ContactsContract$PhoneLookup");
            mBaseURi=(Uri) c.getField("CONTENT_FILTER_URI").get(mBaseURi);
            projection=new String[] {"display_name"};
        } catch (Exception e ) {}
        uri=Uri.withAppendedPath(mBaseURi, Uri.encode(phoneNumber));
        Cursor cursor=this.getContentResolver().query(uri, projection, null, null, null);
        String contactName="";
        if (cursor.moveToFirst()) {
            contactName=cursor.getString(0);
        }
        cursor.close();
        cursor=null;
        return contactName;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*üst kısımda bulunan yenile, telefon rehberi ve bilgileri güncelle butonlarının işlevlerinin belirtildiği metot*/
        switch (item.getItemId()) {
            case R.id.refresh:
                adapterLoad();
                break;
            case R.id.contact:
                 Intent j= new Intent();
                 j.setClass(MessageList.this, TelephoneGuide.class);
                 startActivity(j);
                 break;
            case R.id.update:
                Intent k=new Intent();
                k.setClass(MessageList.this, UpdateUserInfo.class);
                startActivity(k);
                break;
                }
                return super.onOptionsItemSelected(item);
        }
        @Override
        protected void onResume() {
        adapterLoad();
        super.onResume();
        }
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
        /* üst kısımlardaki butonların tasarımını içeren menu.*/
            getMenuInflater().inflate(R.menu.messagelist_menu, menu);
            return true;
        }
    }
