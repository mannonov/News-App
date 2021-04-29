package uz.jaxadev.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class MyLoader extends AsyncTaskLoader<List<News>> {

    private String url;

    public MyLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (url == null || url.isEmpty()) {
            return null;
        }

        List<News> fetchedNews = QueryUtils.fetchNews(url);
        return fetchedNews;
    }
}
