package Util;

import java.util.ArrayList;
import java.util.List;

public class GroupMsg {
    private String groupId;
    private String groupName;
    private String createrId;
    private List<String>groupUser = new ArrayList<>();

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public void setGroupUse(List<String> groupUser) {
        this.groupUser = groupUser;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getCreaterId() {
        return createrId;
    }

    public List<String> getGroupUse() {
        return groupUser;
    }
}
