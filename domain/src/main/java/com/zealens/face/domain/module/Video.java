package com.zealens.face.domain.module;

import com.zealens.face.base.Rule;

/**
 * Created on 2017/3/25
 * in BlaBla by Kyle
 */

public class Video {
    public String path;
    public long timeLast;
    public Score score;

    @Rule.VideoTag
    public int tag;

    @Rule.VideoDivider
    public int dividerType;

    public Video() {
    }

    public Video(@Rule.VideoDivider int dividerType) {
        this.dividerType = dividerType;
    }

    public Video(String path, long timeLast, Score score, int tag, int dividerType) {
        this.path = path;
        this.timeLast = timeLast;
        this.score = score;
        this.tag = tag;
        this.dividerType = dividerType;
    }

    public Video(Video v) {
        this.path = v.path;
        this.timeLast = v.timeLast;
        this.score = v.score;
        this.tag = v.tag;
        this.dividerType = v.dividerType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Video video = (Video) o;

        if (timeLast != video.timeLast) return false;
        if (tag != video.tag) return false;
        if (dividerType != video.dividerType) return false;
        if (path != null ? !path.equals(video.path) : video.path != null) return false;
        return score != null ? score.equals(video.score) : video.score == null;
    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + (int) (timeLast ^ (timeLast >>> 32));
        result = 31 * result + (score != null ? score.hashCode() : 0);
        result = 31 * result + tag;
        result = 31 * result + dividerType;
        return result;
    }
}
