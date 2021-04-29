package uz.jaxadev.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<List<News>> {

    private static String MY_URL = "https://content.guardianapis.com/search";

    private static String MY_KEY = "668c4fb8-e6d9-4d6f-94f8-044c81dd04dd";

    private NewsAdapter newsAdapter;
    private TextView listViewInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.news_list_view);
        listViewInfoTextView = findViewById(R.id.list_view_info_text);
        listView.setEmptyView(listViewInfoTextView);

        newsAdapter = new NewsAdapter(this,new ArrayList<News>());

        listView.setAdapter(newsAdapter);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager manager = getLoaderManager();
            manager.initLoader(0, null, this);

        } else {
            listViewInfoTextView.setText(R.string.no_internet);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                News clickedNews = newsAdapter.getItem(position);

                Uri newsLink = Uri.parse(clickedNews.getLink());

                Intent intent =  new Intent(Intent.ACTION_VIEW, newsLink);
                startActivity(intent);

            }
        });

    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {

        Uri uri = Uri.parse(MY_URL);

        Uri.Builder builder = uri.buildUpon();

        builder.appendQueryParameter("q", getString(R.string.query_key));
        builder.appendQueryParameter("api-key", MY_KEY);
        builder.appendQueryParameter("show-tags", "contributor");

        return new MyLoader(this, builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {

        listViewInfoTextView.setText(R.string.no_news);
        newsAdapter.clear();

        if (data != null && !data.isEmpty()) {
            newsAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        newsAdapter.clear();
    }
}