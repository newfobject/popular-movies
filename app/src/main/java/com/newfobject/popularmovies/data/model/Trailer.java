
package com.newfobject.popularmovies.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Trailer {

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

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }


    /**
     * 
     * @return
     *     The iso6391
     */
    public String getIso6391() {
        return iso6391;
    }

    /**
     * 
     * @return
     *     The key
     */
    public String getKey() {
        return key;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @return
     *     The site
     */
    public String getSite() {
        return site;
    }

    /**
     * 
     * @return
     *     The size
     */
    public int getSize() {
        return size;
    }

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    public static class Response {

        @SerializedName("id")
        @Expose
        private int id;
        @SerializedName("results")
        @Expose
        private List<Trailer> results = new ArrayList<>();

        /**
         *
         * @return
         *     The id
         */
        public int getId() {
            return id;
        }

        /**
         *
         * @return
         *     The results
         */
        public List<Trailer> getResults() {
            return results;
        }
    }
}
