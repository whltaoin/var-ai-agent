package cn.varin.varinimagesearchmcp.entity;

import lombok.Data;

@Data
public class PexelsImage {
    private int id;
    private int width;
    private int height;
    private String url;
    private String photographer;
    private String photographerUrl;
    private int photographerId;
    private String avgColor;
    private ImageSource src;
    private boolean liked;
    private String alt;
}
