package hucare.avstudy.mmp.model.entity;

/**
 * List entity
 *
 * @author huzeliang
 * @version 1.0 2017-11-6 18:12:50
 * @see ***
 * @since ***
 */
public class HotItemBean {

    private String id;
    private String title;
    private String intro;
    private String icon_url;
    private String browser;
    private String uuid;
    private String vuid;
    private String comment_num;
    private String type;
    private String display_style;
    private String url;
    private int is_video;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getVuid() {
        return vuid;
    }

    public void setVuid(String vuid) {
        this.vuid = vuid;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDisplay_style() {
        return display_style;
    }

    public void setDisplay_style(String display_style) {
        this.display_style = display_style;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIs_video() {
        return is_video;
    }

    public void setIs_video(int is_video) {
        this.is_video = is_video;
    }
}
