
package kz.optimabank.optima24.model.gson.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InterfaceView {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("CategoryId")
    @Expose
    private Integer categoryId;
    @SerializedName("CategoryName")
    @Expose
    private String categoryName;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Image")
    @Expose
    private String image;
    @SerializedName("InterfaceApi")
    @Expose
    private String interfaceApi;
    @SerializedName("InterfaceForms")
    @Expose
    private String interfaceForms;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("InterfaceApiData")
    @Expose
    private List<InterfaceApiData> interfaceApiData = null;
    @SerializedName("InterfaceFormsData")
    @Expose
    private List<InterfaceFormsData> interfaceFormsData = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getInterfaceApi() {
        return interfaceApi;
    }

    public void setInterfaceApi(String interfaceApi) {
        this.interfaceApi = interfaceApi;
    }

    public String getInterfaceForms() {
        return interfaceForms;
    }

    public void setInterfaceForms(String interfaceForms) {
        this.interfaceForms = interfaceForms;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public List<InterfaceApiData> getInterfaceApiData() {
        return interfaceApiData;
    }

    public void setInterfaceApiData(List<InterfaceApiData> interfaceApiData) {
        this.interfaceApiData = interfaceApiData;
    }

    public List<InterfaceFormsData> getInterfaceFormsData() {
        return interfaceFormsData;
    }

    public void setInterfaceFormsData(List<InterfaceFormsData> interfaceFormsData) {
        this.interfaceFormsData = interfaceFormsData;
    }

    public InterfaceView(String title, String image, String interfaceApi, String interfaceForms, Boolean isActive) {
        this.title = title;
        this.image = image;
        this.interfaceApi = interfaceApi;
        this.interfaceForms = interfaceForms;
        this.isActive = isActive;
    }

    public InterfaceView() {
    }
}
