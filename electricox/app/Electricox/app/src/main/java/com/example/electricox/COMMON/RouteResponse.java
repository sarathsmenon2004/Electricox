package com.example.electricox.COMMON;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RouteResponse {
    @SerializedName("routes")
    private List<Route> routes;

    public List<Route> getRoutes() { return routes; }

    public static class Route {
        @SerializedName("overview_polyline")
        private OverviewPolyline overviewPolyline;

        @SerializedName("legs")
        private List<Leg> legs;

        public OverviewPolyline getOverviewPolyline() { return overviewPolyline; }
        public List<Leg> getLegs() { return legs; }
    }

    public static class OverviewPolyline {
        @SerializedName("points")
        private String points;

        public String getPoints() { return points; }
    }

    public static class Leg {
        @SerializedName("distance")
        private TextValue distance;

        @SerializedName("duration")
        private TextValue duration;

        public TextValue getDistance() { return distance; }
        public TextValue getDuration() { return duration; }
    }

    public static class TextValue {
        @SerializedName("text")
        private String text;

        public String getText() { return text; }
    }
}
