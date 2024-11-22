package com.example.bakelink.common.models;

import java.util.ArrayList;
import java.util.List;

public class VisionRequest {
    private List<Request> requests;

    public VisionRequest(String base64Image) {
        this.requests = new ArrayList<>();
        this.requests.add(new Request(base64Image));
    }

    public static class Request {
        private Image image;
        private List<Feature> features;

        public Request(String base64Image) {
            this.image = new Image(base64Image);
            this.features = new ArrayList<>();
            this.features.add(new Feature("IMAGE_PROPERTIES"));
        }
    }

    public static class Image {
        private String content;

        public Image(String content) {
            this.content = content;
        }
    }

    public static class Feature {
        private String type;

        public Feature(String type) {
            this.type = type;
        }
    }
}

