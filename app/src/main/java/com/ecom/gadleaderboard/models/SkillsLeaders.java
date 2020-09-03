package com.ecom.gadleaderboard.models;

import com.google.gson.annotations.SerializedName;

public class SkillsLeaders {

    private String name;
    private String country;
    private String score;
    @SerializedName("badgeUrl")
    private String imageUrl;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
