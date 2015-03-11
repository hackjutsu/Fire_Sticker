package com.gogocosmo.cosmoqiu.fire_sticker.Model;

import java.util.UUID;

public class Group {

    private int mGroupId;
    private String mUuid;
    private String mGroupName;

    public Group() {
    }

    public Group(String GroupName) {
        this.mGroupName = GroupName;
        this.mUuid = UUID.randomUUID().toString();
    }

    public int getGroupId() {
        return mGroupId;
    }

    public void setGroupId(int GroupId) {
        this.mGroupId = GroupId;
    }

    public String getGroupName() {
        return mGroupName;
    }

    public void setGroupName(String GroupName) {
        this.mGroupName = GroupName;
    }

    public String getUuid() {
        return mUuid;
    }

    public void setUuid(String Uuid) {
        this.mUuid = Uuid;
    }
}

