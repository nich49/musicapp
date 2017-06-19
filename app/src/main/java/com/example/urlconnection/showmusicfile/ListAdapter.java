package com.example.urlconnection.showmusicfile;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by auk on 2017/05/24.
 */

public class ListAdapter extends BaseAdapter {
  private Context context = null;
  private ArrayList<ListItem> list = null;
  private int resource = 0;

  public ListAdapter(Context context,
                     ArrayList<ListItem> list, int resource) {
    this.context = context;
    this.list = list;
    this.resource = resource;
  }

  // アイテムの個数
  public int getCount() {
    return this.list.size();
  }

  // 指定されたアイテムを取得
  public Object getItem(int position) {
    return this.list.get(position);
  }

  // 指定された項目を識別するためのid値を取得
  public long getItemId(int position) {
    return list.get(position).getId();
  }

  // リスト項目を取得するためのViewを取得
  public View getView(int position, View convertView, ViewGroup parent) {
    Activity activity = (Activity) this.context;
    ListItem item = (ListItem) getItem(position);
    if (convertView == null) {
      convertView = activity.getLayoutInflater()
          .inflate(resource, null);
    }
    ((TextView) convertView.findViewById(R.id.title))
        .setText(item.getTitle());
    ((TextView) convertView.findViewById(R.id.path))
        .setText(item.getPath());
    return convertView;
  }
}
