package com.bcu.movie.Filter;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class HtmlFilter {
    public static String filterHtml(String html) {
        return Jsoup.clean(html, Safelist.relaxed());
    }
}
