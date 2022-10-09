
package kz.optimabank.optima24.model.gson.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InterfaceApiData {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("ItemId")
    @Expose
    private Integer itemId;
    @SerializedName("Count")
    @Expose
    private Integer count;
    @SerializedName("Macros")
    @Expose
    private String macros;
    @SerializedName("IpAddress")
    @Expose
    private String ipAddress;
    @SerializedName("HttpMethod")
    @Expose
    private String httpMethod;
    @SerializedName("RequestBody")
    @Expose
    private String requestBody;
    @SerializedName("ResponseBody")
    @Expose
    private String responseBody;
    @SerializedName("IsAutoRequest")
    @Expose
    private Boolean isAutoRequest;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getMacros() {
        return macros;
    }

    public void setMacros(String macros) {
        this.macros = macros;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public Boolean getIsAutoRequest() {
        return isAutoRequest;
    }

    public void setIsAutoRequest(Boolean isAutoRequest) {
        this.isAutoRequest = isAutoRequest;
    }

}
