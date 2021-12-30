package com.xkyss.weixin.tp.property;

/**
 * 政务相关配置信息
 * 政务微信/我的单位
 *
 * @author xkyii
 * @createdAt  on 2021/07/28.
 */
public class TpProperties {

    /**
     * 第三方应用url
     */
    private String serverUrl;

    /**
     * 企业ID
     */
    private String corpId;

    /**
     * 通讯录
     */
    private TpContactProperties contact;

    /**
     * 应用管理
     */
    private TpAppManagerProperties appManager;

    /**
     * 默认应用(当前为: 省厅新平台)
     */
    private TpAppProperties mainApp;

    public TpProperties() {
        contact = new TpContactProperties();
        mainApp = new TpAppProperties();
    }

    public TpProperties(String serverUrl, String corpId, TpContactProperties contact, TpAppManagerProperties appManager, TpAppProperties stxpt) {
        this.serverUrl = serverUrl;
        this.corpId = corpId;
        this.contact = contact;
        this.appManager = appManager;
        this.mainApp = stxpt;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public TpContactProperties getContact() {
        return contact;
    }

    public void setContact(TpContactProperties contact) {
        this.contact = contact;
    }

    public TpAppProperties getMainApp() {
        return mainApp;
    }

    public void setMainApp(TpAppProperties mainApp) {
        this.mainApp = mainApp;
    }

    public static final Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private String serverUrl;
        private String corpId;
        private TpContactProperties contact;
        private TpAppManagerProperties appManager;
        private TpAppProperties mainApp;

        private Builder() {
        }

        public static Builder aTpProperties() {
            return new Builder();
        }

        public Builder serverUrl(String serverUrl) {
            this.serverUrl = serverUrl;
            return this;
        }

        public Builder corpId(String corpId) {
            this.corpId = corpId;
            return this;
        }

        public Builder contact(TpContactProperties contact) {
            this.contact = contact;
            return this;
        }

        public Builder appManager(TpAppManagerProperties appManager) {
            this.appManager = appManager;
            return this;
        }

        public Builder mainApp(TpAppProperties mainApp) {
            this.mainApp = mainApp;
            return this;
        }

        public TpProperties build() {
            TpProperties tpProperties = new TpProperties(serverUrl, corpId, contact, appManager, null);
            tpProperties.setMainApp(mainApp);
            return tpProperties;
        }
    }
}
