package com.sonveer.cab.sharing.entity.response;

import com.sonveer.cab.sharing.entity.request.User;

import java.util.List;

public class SelectResponse extends ResponseMessage{
    List<SelectedVehicleInfo> selectedVehicleInfoList;

    public SelectResponse(String message,
                          List<SelectedVehicleInfo> selectedVehicleInfoList) {
        super(message);
        this.selectedVehicleInfoList = selectedVehicleInfoList;
    }

    public List<SelectedVehicleInfo> getSelectedVehicleInfoList() {
        return selectedVehicleInfoList;
    }

    public void setSelectedVehicleInfoList(List<SelectedVehicleInfo> selectedVehicleInfoList) {
        this.selectedVehicleInfoList = selectedVehicleInfoList;
    }
}
