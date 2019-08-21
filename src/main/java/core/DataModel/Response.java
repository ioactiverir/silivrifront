package core.DataModel;

public class Response {
    private String respType; // Video, Audio, Gift, etc.
    private String respMediaLink; // Audio, Video, etc.
    private String respText;   // message
    private String respCharacterName; // who want to show response?
    private String respMessage;
    private String respId;

    public String getQuezzRes() {
        return quezzRes;
    }

    public String getRespId() {
        return respId;
    }

    public void setRespId(String respId) {
        this.respId = respId;
    }

    public void setQuezzRes(String quezzRes) {
        this.quezzRes = quezzRes;
    }

    private String quezzRes;

    public int getRespTime() {
        return respTime;
    }

    public void setRespTime(int respTime) {
        this.respTime = respTime;
    }

    private int respTime;
    public String getRespMessage() {
        return respMessage;
    }

    public void setRespMessage(String respMessage) {
        this.respMessage = respMessage;
    }

    public String getRespType() {
        return respType;
    }

    public void setRespType(String respType) {
        this.respType = respType;
    }

    public String getRespMediaLink() {
        return respMediaLink;
    }

    public void setRespMediaLink(String respMediaLink) {
        this.respMediaLink = respMediaLink;
    }

    public String getRespText() {
        return respText;
    }

    public void setRespText(String respText) {
        this.respText = respText;
    }

    public String getRespCharacterName() {
        return respCharacterName;
    }

    public void setRespCharacterName(String respCharacterName) {
        this.respCharacterName = respCharacterName;
    }
}
