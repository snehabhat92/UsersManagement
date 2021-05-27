package com.example.usersmanagement.Model;
import android.os.Parcel;
import android.os.Parcelable;

public class UserParcel implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public UserParcel createFromParcel(Parcel in) {
            return new UserParcel(in);
        }

        public UserParcel[] newArray(int size) {
            return new UserParcel[size];
        }

    };

    private int id;
    private String name;
    private String firstName;
    private String lastName;
    private String userName;
    private String company;
    private String phone;
    private String email;
    private String website;
    private String address;
    private double lat;
    private double lng;


    public UserParcel(int id, String name, String firstName, String lastName, String userName, String phone, String email, String website, String company, String address, double lat, double lng) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.company = company;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public UserParcel(Parcel in){
        this.id = in.readInt();
        this.name = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.userName = in.readString();
        this.phone =  in.readString();
        this.email =  in.readString();
        this.website =  in.readString();
        this.company =  in.readString();
        this.address = in.readString();
        this.lat = in.readDouble();
        this.lng = in.readDouble();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.userName);
        dest.writeString(this.phone);
        dest.writeString(this.email);
        dest.writeString(this.website);
        dest.writeString(this.company);
        dest.writeString(this.address);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);

    }


}
