package com.xkyss.weixin.tp.constant;

/**
 * 路径常量
 *
 * @author xkyii
 * Created on 2021/07/28.
 */
public interface TpPathConsts {

    String GET_TOKEN = "/cgi-bin/gettoken?corpid=%s&corpsecret=%s";
    String BATCH_REPLACE_PARTY = "/cgi-bin/batch/replaceparty";
    String BATCH_REPLACE_USER = "/cgi-bin/batch/replaceuser";

    interface Department {
        String DEPARTMENT_CREATE = "/cgi-bin/department/create";
        String DEPARTMENT_UPDATE = "/cgi-bin/department/update";
        String DEPARTMENT_DELETE = "/cgi-bin/department/delete?id=%d";
        String DEPARTMENT_LIST = "/cgi-bin/department/list";
    }

    interface Media {
        String MEDIA_GET = "/cgi-bin/media/get?media_id=%s";
        String MEDIA_UPLOAD = "/cgi-bin/media/upload?type=%s";
        String IMG_UPLOAD = "/cgi-bin/media/uploadimg";
        String JSSDK_MEDIA_GET = "/cgi-bin/media/get/jssdk";
    }

    interface User {
        String USER_AUTHENTICATE = "/cgi-bin/user/authsucc?userid=";
        String USER_CREATE = "/cgi-bin/user/create";
        String USER_UPDATE = "/cgi-bin/user/update";
        String USER_DELETE = "/cgi-bin/user/delete?userid=%s";
        String USER_BATCH_DELETE = "/cgi-bin/user/batchdelete";
        String USER_GET = "/cgi-bin/user/get?userid=%s";
        String USER_LIST = "/cgi-bin/user/list?department_id=%d&fetch_child=%d";
        String USER_SIMPLE_LIST = "/cgi-bin/user/simplelist?department_id=%d&fetch_child=%d";
        String BATCH_INVITE = "/cgi-bin/batch/invite";
        String USER_CONVERT_TO_OPENID = "/cgi-bin/user/convert_to_openid";
        String USER_CONVERT_TO_USERID = "/cgi-bin/user/convert_to_userid";
        String GET_USER_ID = "/cgi-bin/user/getuserid";
        String GET_EXTERNAL_CONTACT = "/cgi-bin/crm/get_external_contact?external_userid=";
        String GET_JOIN_QR_CODE = "/cgi-bin/corp/get_join_qrcode?size_type=";
        String GET_USER_INFO = "/cgi-bin/user/getuserinfo?code=%s";
        String GET_USER_DETAIL = "/cgi-bin/user/getuserdetail";
    }
}
