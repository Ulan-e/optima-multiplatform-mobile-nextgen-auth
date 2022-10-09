
package kz.optimabank.optima24.model.gson.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InterfaceFormsData {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("ItemId")
    @Expose
    private Integer itemId;
    @SerializedName("FormTypeId")
    @Expose
    private Integer formTypeId;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Key")
    @Expose
    private String key;
    @SerializedName("Mask")
    @Expose
    private String mask;
    @SerializedName("MinLength")
    @Expose
    private Integer minLength;
    @SerializedName("MaxLength")
    @Expose
    private Integer maxLength;
    @SerializedName("TextColor")
    @Expose
    private String textColor;
    @SerializedName("FormColor")
    @Expose
    private String formColor;
    @SerializedName("FormName")
    @Expose
    private String formName;

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

    public Integer getFormTypeId() {
        return formTypeId;
    }

    public void setFormTypeId(Integer formTypeId) {
        this.formTypeId = formTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getFormColor() {
        return formColor;
    }

    public void setFormColor(String formColor) {
        this.formColor = formColor;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

}
