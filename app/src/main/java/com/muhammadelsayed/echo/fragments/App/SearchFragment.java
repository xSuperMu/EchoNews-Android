package com.muhammadelsayed.echo.fragments.App;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.muhammadelsayed.echo.R;
import com.muhammadelsayed.echo.adapters.NewsAdapter;
import com.muhammadelsayed.echo.echo_utils.Utils;
import com.muhammadelsayed.echo.model.Article;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";
    private NewsAdapter mSearchNewsAdapter;
    private RecyclerView mSearchRecycler;
    private TextView mNoResults;
    private SearchView mSearchView;
    private List<Article> mSearchArticleList = new ArrayList<>();

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment searchFragmentInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.wtf(TAG, "onCreate() has been instantiated");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.wtf(TAG, "onCreateView() has been instantiated");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        mSearchRecycler = rootView.findViewById(R.id.search_recycler);
        LinearLayoutManager mLinearLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mSearchRecycler.setLayoutManager(mLinearLayoutManager);
        mSearchRecycler.setDrawingCacheEnabled(true);
        mSearchRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mSearchRecycler.setItemViewCacheSize(100);
        mNoResults = rootView.findViewById(R.id.no_results);
        mSearchView = rootView.findViewById(R.id.search_view);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.wtf("onQueryTextChange", "called");
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                loadSearchData(mSearchView.getQuery().toString());
                return false;
            }
        });
        return rootView;
    }

    private void loadSearchData(String query) {
        Log.wtf(TAG, "loadSearchData() has been instantiated");
        showData();
        mSearchView.clearFocus();
        if (!TextUtils.isEmpty(query)) {
            if (mSearchNewsAdapter != null)
                mSearchNewsAdapter = null;
            Map<String, Object> options = new HashMap<>();
            options.put("q", query);
            options.put("order-by", "newest");
            options.put("show-tags", "contributor");
            options.put("show-fields", "thumbnail,showInRelatedContent,shortUrl");
            options.put("page", 1);
            options.put("page-size", 20);
            options.put("api-key", "c8133e91-2b02-42b7-9cc8-88ca8d73998a");
            Utils.getSearchResults(options, new Utils.retrofitCallback() {
                @Override
                public void onSuccess(List<Article> Article) {
                    Log.wtf(TAG, "onSuccess: SEARCH ARTICLES = " + Article);
                    if (Article.size() > 0) {
                        mSearchArticleList = Article;
                        mSearchNewsAdapter = new NewsAdapter(getContext(), mSearchArticleList);
                        mSearchRecycler.setAdapter(mSearchNewsAdapter);
                    } else {
                        showEmptyResults();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.wtf(TAG, "onSuccess: SEARCH ARTICLES Failure -> " + t.getMessage());
                }
            });
        } else {
            showEmptyResults();
        }
        mSearchRecycler.setAdapter(mSearchNewsAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.wtf(TAG, "onPause() has been instantiated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.wtf(TAG, "onStart() has been instantiated");
        if (mSearchNewsAdapter != null)
            mSearchRecycler.setAdapter(mSearchNewsAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.wtf(TAG, "onStop() has been instantiated");
    }

    private void showEmptyResults() {
        Log.wtf(TAG, "showEmptyResults() has been instantiated");
        mSearchRecycler.setVisibility(View.GONE);
        mNoResults.setVisibility(View.VISIBLE);
    }

    private void showData() {
        Log.wtf(TAG, "showData() has been instantiated");
        mSearchRecycler.setVisibility(View.VISIBLE);
        mNoResults.setVisibility(View.GONE);
    }

}
