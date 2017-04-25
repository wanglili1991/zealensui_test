package com.zealens.face.user;

/**
 * Created on 2017/3/7
 * in BlaBla by Kyle
 */

public class UserInfo implements Comparable {
    public String nickName;
    public String headImageUrl;
    public String unionId;

    public UserInfo(String nickName, String headImageUrl, String unionId) {
        this.nickName = nickName;
        this.headImageUrl = headImageUrl;
        this.unionId = unionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInfo userInfo = (UserInfo) o;

        if (nickName != null ? !nickName.equals(userInfo.nickName) : userInfo.nickName != null)
            return false;
        if (headImageUrl != null ? !headImageUrl.equals(userInfo.headImageUrl) : userInfo.headImageUrl != null)
            return false;
        return unionId != null ? unionId.equals(userInfo.unionId) : userInfo.unionId == null;

    }

    @Override
    public int hashCode() {
        int result = nickName != null ? nickName.hashCode() : 0;
        result = 31 * result + (headImageUrl != null ? headImageUrl.hashCode() : 0);
        result = 31 * result + (unionId != null ? unionId.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Object o) {
        if (this.equals(o)) return 0;
        return this.unionId.compareTo(((UserInfo) o).unionId);
    }
}
