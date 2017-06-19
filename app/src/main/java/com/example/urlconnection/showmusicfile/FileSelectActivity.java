package com.example.urlconnection.showmusicfile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class FileSelectActivity extends AppCompatActivity {
  private ListView listView;
  private File[] files;
  private String path;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_file_select);

    showList(Environment.getExternalStorageDirectory()
        + "/" + Environment.DIRECTORY_MUSIC + "/");
  }

  private String getPath() {
    return this.path;
  }

  private void setPath(String path) {
    this.path = path;
  }

  //
  private void showList(String filePath) {
    setPath(filePath);
    files = new File(getPath()).listFiles();
    if (files != null) {
      ArrayList<ListItem> data = new ArrayList<>();
      ArrayList<ListItem> directory = new ArrayList<>();
      ArrayList<ListItem> file = new ArrayList<>();
      for (File f : files) {
        ListItem listItem = new ListItem();
        listItem.setId((new Random()).nextLong());
        listItem.setTitle(f.getName());
        listItem.setFile(f);
        listItem.setPath(f.getPath());
        if (f.isDirectory()) {
          directory.add(listItem);
        }
        if (f.getName().endsWith(".mp3")) {
          file.add(listItem);
        }
      }
      // ディレクトリ、ファイルの順にする
      // TODO 書き直し
      for (ListItem item : directory) {
        data.add(item);
      }
      for (ListItem item : file) {
        data.add(item);
      }

      // ListItem配列とレイアウトを関連付け
      ListAdapter adapter = new ListAdapter(this, data, R.layout.list_item);
      listView = (ListView) findViewById(R.id.songList);
      listView.setAdapter(adapter);

      // リスト項目がクリックされた時の処理を定義
      listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
          ListItem li = (ListItem) ((ListView) adapterView).getAdapter().getItem(position);
          if (li.getFile().isDirectory()) {
            showList(li.getPath());
          }
          if (li.getFile().isFile() && li.getFile().getName().endsWith(".mp3")) {
            // PlayMusicActivityに選択したファイルを渡す
            Intent intent = new Intent(getApplication(),
                com.example.urlconnection.showmusicfile.PlayMusicActivity.class);
            intent.putExtra("musicFile", li.getFile());
            startActivity(intent);
          }
        }
      });
    }
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      File file = new File(getPath());
      if (!file.getParent().equals("/")) {
        showList(file.getParent());
      }
      return false;
    } else {
      return super.onKeyDown(keyCode, keyEvent);
    }
  }
}
