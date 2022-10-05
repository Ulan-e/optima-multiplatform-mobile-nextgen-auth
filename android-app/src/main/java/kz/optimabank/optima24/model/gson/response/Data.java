
package kz.optimabank.optima24.model.gson.response;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("InterfaceView")
    @Expose
    private ArrayList<InterfaceView> interfaceView = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<InterfaceView> getInterfaceView() {
        return interfaceView;
    }

    public void setInterfaceView(ArrayList<InterfaceView> interfaceView) {
        this.interfaceView = interfaceView;
    }

}
