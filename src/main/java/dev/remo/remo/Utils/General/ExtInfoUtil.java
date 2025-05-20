package dev.remo.remo.Utils.General;

import java.util.HashMap;
import java.util.Map;

import dev.remo.remo.Models.Users.User;

public class ExtInfoUtil {

    public static Map<String, String> buildExtInfo(User currentUser, String remark) {
        Map<String, String> extInfo = new HashMap<>();
        extInfo.put("updatedBy", currentUser.getEmail());
        extInfo.put("updatedAt", DateUtil.getCurrentDateTime());
        extInfo.put("remark", remark);
        return extInfo;
    }

}
