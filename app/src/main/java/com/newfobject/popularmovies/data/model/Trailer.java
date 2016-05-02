package com.newfobject.popularmovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Trailer implements Parcelable {

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("iso_639_1")
    @Expose
    private String iso6391;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("site")
    @Expose
    private String site;
    @SerializedName("size")
    @Expose
    private int size;
    @SerializedName("type")
    @Expose
    private String type;


    public Trailer() {
    }

    private Trailer(Parcel in) {
        this.id = in.readString();
        this.iso6391 = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.site = in.readString();
        this.size = in.readInt();
        this.type = in.readString();
    }

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @return The iso6391
     */
    public String getIso6391() {
        return iso6391;
    }

    /**
     * @return The key
     */
    public String getKey() {
        return key;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @return The site
     */
    public String getSite() {
        return site;
    }

    /**
     * @return The size
     */
    public int getSize() {
        return size;
    }

    /**
     * @return The type
     */
    public String getType() {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.iso6391);
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.site);
        dest.writeInt(this.size);
        dest.writeString(this.type);
    }

    public static class Response {

        @SerializedName("id")
        @Expose
        private int id;
        @SerializedName("results")
        @Expose
        private List<Trailer> results = new ArrayList<>();

        /**
         * @return The id
         */
        public int getId() {
            return id;
        }

        /**
         * @return The results
         */
        public List<Trailer> getResults() {
            return results;
        }
    }
}
