package org.octabyte.zeem.API.Helper;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.List;

@Entity
public class ContactHolder {

    @Id private Long userId;
    private List<Long> contactList;
    private Long phone;  // Hold my phone

    public ContactHolder() {
    }

    public ContactHolder(Long userId, List<Long> contactList, Long myId) {
        this.userId = userId;
        this.contactList = contactList;
        this.phone = myId;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getContactList() {
        return contactList;
    }

    public void setContactList(List<Long> contactList) {
        this.contactList = contactList;
    }
}
