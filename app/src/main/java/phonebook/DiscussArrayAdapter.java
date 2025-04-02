package phonebook;

import java.util.ArrayList;
import java.util.List;

import com.project.wattchat.R;
import android.content.Context;
import android.database.DataSetObserver;
import android.drm.DrmRights;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DiscussArrayAdapter extends ArrayAdapter<OneComment> {
    private TextView messages;
    private List<OneComment>oneCommentList=new ArrayList<OneComment>();

    private LinearLayout wrapper;
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (observer != null) {
            super.unregisterDataSetObserver(observer);
        }
    }
    @Override
    public void add(OneComment object) {
        oneCommentList.add(object);
        super.add(object);
    }
    public DiscussArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }
    public int getCount() {
        return this.oneCommentList.size();
    }
    public  View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        if (row==null) {
            LayoutInflater inflater =(LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.listitem_discuss, parent,false);
        }
        wrapper=(LinearLayout) row.findViewById(R.id.wrapper);
        OneComment coment=getItem(position);
        messages=(TextView) row.findViewById(R.id.comment);
        messages.setText(coment.comment);
        messages.setBackgroundResource(coment.left ? R.drawable.bubble_yellow:R.drawable.bubble_green);
        wrapper.setGravity(coment.left ? Gravity.LEFT:Gravity.RIGHT);
        return row;
    }
    public Bitmap decodeToBitmap(byte[] decodeByte) {
        return BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.length);
    }
}
