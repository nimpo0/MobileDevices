package com.example.lab3.data.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScheduleResponse {

    @SerializedName("userSchedule")
    @Expose
    private List<UserSchedule> userSchedule;

    @SerializedName("appointmentDetails")
    @Expose
    private List<AppointmentDetail> appointmentDetails;

    public List<UserSchedule> getUserSchedule() {
        return userSchedule;
    }

    public void setUserSchedule(List<UserSchedule> userSchedule) {
        this.userSchedule = userSchedule;
    }

    public List<AppointmentDetail> getAppointmentDetails() {
        return appointmentDetails;
    }

    public void setAppointmentDetails(List<AppointmentDetail> appointmentDetails) {
        this.appointmentDetails = appointmentDetails;
    }
}