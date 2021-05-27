package com.example.usersmanagement.Model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    @SerializedName("id")
   // @ColumnInfo(name = "id")
    private int id;

    @SerializedName("name")
    //@ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "firstname")
    private String firstname;

    @ColumnInfo(name = "lastname")
    private String lastname;

    @SerializedName("username")
    //@ColumnInfo(name = "username")
    private String username;

    @SerializedName("email")
   // @ColumnInfo(name = "email")
    private String email;

    @SerializedName("phone")
    //@ColumnInfo(name = "phone")
    private String phone;

    @SerializedName("website")
    //@ColumnInfo(name = "website")
    private String website;

    @SerializedName("company")
    @ColumnInfo(name = "company")
    private Company company;

    @SerializedName("address")
    @ColumnInfo(name = "address")
    private Address address;

    public User(int id, String name, String username, String firstname, String lastname, String email, String phone, String website, Company company, Address address) {
        this.id = id;
        this.name = name;
        if(name != null) {
            String[] nameArray = name.split(" ");
            if(nameArray.length >= 2) {
                this.lastname = nameArray[1];
            } else {
                this.lastname = "";
            }
            this.firstname = nameArray[0];
        }
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.website = website;
        this.company = company;
        this.address = address;
    }

    public int getId() {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", userName='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", website='" + website + '\'' +
                ", company=" + company +
                ", address=" + address +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }

    public static class Company {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Company(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Company{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

   public static class Address {
        private String street;
        private String suite;
        private String city;
        private String zipcode;
        private Geo geo;

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getSuite() {
            return suite;
        }

        public void setSuite(String suite) {
            this.suite = suite;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }

        public Geo getGeo() {
            return geo;
        }

        public void setGeo(Geo geo) {
            this.geo = geo;
        }

        @Override
        public String toString() {
            return "Address{" +
                    "street='" + street + '\'' +
                    ", suite='" + suite + '\'' +
                    ", city='" + city + '\'' +
                    ", zipcode='" + zipcode + '\'' +
                    ", geo=" + geo +
                    '}';
        }
    }

    public static class Geo {
        private double lat;
        private double lng;

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

        @Override
        public String toString() {
            return "Geo{" +
                    "lat=" + lat +
                    ", lng=" + lng +
                    '}';
        }
    }

    public static class SortByName implements Comparator<User> {

        @Override
        public int compare(User user1, User user2) {
            if(user1 != null && user2 != null && !TextUtils.isEmpty(user1.getFirstname()) && !TextUtils.isEmpty(user2.getFirstname())) {
                return user1.getFirstname().compareTo(user2.getFirstname());
            }
            return -1;

        }

    }
}
