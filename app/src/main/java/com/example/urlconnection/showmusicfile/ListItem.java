package com.example.urlconnection.showmusicfile;

import java.io.File;

/**
 * Created by auk on 2017/05/24.
 */

public class ListItem {
  private long id = 0;
  private String title = null;
  private String path = null;
  private File file = null;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    this.file = file;
  }
}
