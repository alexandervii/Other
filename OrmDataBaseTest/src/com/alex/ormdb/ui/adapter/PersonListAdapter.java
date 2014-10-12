package com.alex.ormdb.ui.adapter;

import java.util.List;
import java.util.zip.Inflater;

import com.alex.ormdb.model.Person;
import com.alex.ormdb.model.Person.Gender;
import com.example.ormdatabasetest.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonListAdapter extends BaseAdapter {

    private List<Person> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    
    public PersonListAdapter(Context context, List<Person> list) {
        this.mData = list;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }
    
    @Override
    public int getCount() {
        if(mData != null && mData.size() > 0) {
            return mData.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.person_list_item, null);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //头像显示
        Person p = mData.get(position);
        
        holder.name = (TextView) convertView.findViewById(R.id.person_list_item_name_id);
        holder.gender = (TextView) convertView.findViewById(R.id.person_list_item_gender_id);
        holder.age = (TextView) convertView.findViewById(R.id.person_list_item_age_id);
        holder.address = (TextView) convertView.findViewById(R.id.person_list_item_addr_id);
        holder.description = (TextView) convertView.findViewById(R.id.person_list_item_desc_id);
        
        holder.name.setText(p.getName());
        holder.gender.setText(p.getGender().toString());
        holder.age.setText(p.getAge()+"");
        holder.address.setText(p.getAddress());
        holder.description.setText(p.getDescription());
        return convertView;
    }
    
    class ViewHolder {
        public ImageView portrait;
        public TextView name,gender,age,address,description;
    }

}
