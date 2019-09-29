package com.ljy.crawler.link;

public interface LinkFilter {
    public boolean accept(String url);
}