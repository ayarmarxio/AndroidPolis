package com.example.polis.Models;

import android.location.Location;

import com.google.firebase.database.IgnoreExtraProperties;
import java.util.Date;
import com.google.firebase.firestore.ServerTimestamp;

@IgnoreExtraProperties
public class Report {

    private String incidentName;
    private Date timeStamp;
    private Location locationPoint;
    private String registryId;
    private String userId;
    private String detailText;

    public Report(String incidentName, Date timeStamp, Location locationPoint, String registryId, String userId, String detailText){

        this.incidentName = incidentName;
        this.timeStamp = timeStamp;
        this.locationPoint = locationPoint;
        this.registryId = registryId;
        this.userId = userId;
        this.detailText = detailText;
    }

    public Report () {
    }

    public String getIncidentName(){ return incidentName;}
    public void setIncidentName(String incidentName) {this.incidentName = incidentName;}

    public Date getTimeStamp() { return timeStamp;}
    public void setTimeStamp(Date timeStamp){this.timeStamp = timeStamp;}

    public Location getLocationPoint(){ return locationPoint;}
    public void setLocationPoint(Location location) {this.locationPoint = location;}

    public String getRegistryId() { return registryId;}
    public void setRegistryId(String registryId){this.registryId = registryId;}

    public String getUserId() { return userId;}
    public void setUserId(String userId){this.userId = userId;}

    public String getDetailText() { return detailText;}
    public void setDetailText(String detailText){this.detailText = detailText;}




}
