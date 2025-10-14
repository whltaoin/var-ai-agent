package cn.varin.varinimagesearchmcp.entity;

import lombok.Data;
import java.util.List;

@Data
public class PexelsResponse {
    private int totalResults;
    private int page;
    private int perPage;
    private List<PexelsImage> photos;
    private String nextPage;
}
