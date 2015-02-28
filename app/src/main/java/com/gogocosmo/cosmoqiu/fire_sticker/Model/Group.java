package com.gogocosmo.cosmoqiu.fire_sticker.Model;

import java.util.UUID;

/**
 * Created by Citrixer on 2/27/15.
 */
public class Group {

    private int groupId;
    private String uuid;
    private String groupName;

    public Group() {
    }

    public Group(String groupName) {
        this.groupName = groupName;
        this.uuid = UUID.randomUUID().toString();
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}

