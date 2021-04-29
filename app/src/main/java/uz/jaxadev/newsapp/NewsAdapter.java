package uz.jaxadev.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.abduqodirov.newsapp.model.News;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_news, parent, false);
        }

        com.abduqodirov.newsapp.model.News news = getItem(position);

        TextView newsTitleView = listItemView.findViewById(R.id.news_title_text);
        TextView newsSectionView = listItemView.findViewById(R.id.news_section_text);
        TextView newsDate = listItemView.findViewById(R.id.news_date_text);
        TextView newsAuthor = listItemView.findViewById(R.id.news_author_text);

        newsTitleView.setText(news.getTitle());
        newsSectionView.setText(news.getSection());
        newsDate.setText(news.getDate());
        newsAuthor.setText(news.getAuthor());

        return listItemView;
    }
}
