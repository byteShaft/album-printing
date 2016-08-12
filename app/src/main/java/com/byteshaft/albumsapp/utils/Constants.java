package com.byteshaft.albumsapp.utils;

public class Constants {

    private static String API_BASE = "http://188.166.145.145:8000/api/";
    private static String API_BASE_USER = API_BASE + "user/";
    public static String ENDPOINT_REGISTER = API_BASE + "register";
    public static String ENDPOINT_LOGIN = API_BASE + "login";
    public static String ENDPOINT_ACTIVATE = API_BASE + "activate";
    public static String ENDPOINT_ME = API_BASE + "me";
    public static String ENDPOINT_ALBUMS = API_BASE_USER + "albums";
    public static String CONFIG_KEY_FIRST_RUN = "first_run";

    public static String getPhotosEndpointForAlbum(int albumID) {
        return String.format("%s/%s/photos", ENDPOINT_ALBUMS, albumID);
    }
}
