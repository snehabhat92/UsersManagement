package com.example.usersmanagement.Model;

import android.text.TextUtils;

import com.google.gson.Gson;

import androidx.room.TypeConverter;

public class Converters {
    @TypeConverter
    public static User.Company fromString(String companyName) {
        return !TextUtils.isEmpty(companyName) ? new User.Company(companyName) : null;
    }
    @TypeConverter
    public static String fromCompany(User.Company company) {
        return company != null ? company.getName() : "";
    }

    @TypeConverter
    public static User.Address fromAddressString(String companyName) {
        return new Gson().fromJson(companyName, User.Address.class);
    }
    @TypeConverter
    public static String fromAddress(User.Address address) {
        return new Gson().toJson(address);
    }

    @TypeConverter
    public static User.Geo fromGeoString(String geo) {
        return new Gson().fromJson(geo, User.Geo.class);
    }

    @TypeConverter
    public static String fromGeo(User.Geo geo) {
        return new Gson().toJson(geo);
    }
}
