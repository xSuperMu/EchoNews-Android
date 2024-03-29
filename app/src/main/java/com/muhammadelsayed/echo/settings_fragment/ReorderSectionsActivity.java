package com.muhammadelsayed.echo.settings_fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.muhammadelsayed.echo.R;
import com.muhammadelsayed.echo.adapters.ItemTouchHelpers.OnSectionsListChangedListener;
import com.muhammadelsayed.echo.adapters.ItemTouchHelpers.OnStartDragListener;
import com.muhammadelsayed.echo.adapters.ItemTouchHelpers.SimpleItemTouchHelperCallback;
import com.muhammadelsayed.echo.adapters.SectionAdapter;
import com.muhammadelsayed.echo.model.Section;

import java.util.ArrayList;
import java.util.List;


public class ReorderSectionsActivity extends AppCompatActivity implements OnSectionsListChangedListener,
        OnStartDragListener {
    private static final String TAG = "ReorderSectionsActivity";

    private SectionAdapter mSectionAdapter;
    private RecyclerView mSectionsRecycler;
    private ItemTouchHelper mItemTouchHelper;
    private List<Section> mSections;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    public static List<Section> initSectionsList() {
        List<Section> sections = new ArrayList<>();

        sections.add(new Section(R.string.nav_art_design, "Art & Design"));
        sections.add(new Section(R.string.nav_books, "Books"));
        sections.add(new Section(R.string.nav_business, "Business"));
        sections.add(new Section(R.string.nav_culture, "Culture"));
        sections.add(new Section(R.string.nav_education, "Education"));
        sections.add(new Section(R.string.nav_environment, "Environment"));
        sections.add(new Section(R.string.nav_fashion, "Fashion"));
        sections.add(new Section(R.string.nav_film, "Film"));
        sections.add(new Section(R.string.nav_football, "Football"));
        sections.add(new Section(R.string.nav_law, "Law"));
        sections.add(new Section(R.string.nav_lifestyle, "Lifestyle"));
        sections.add(new Section(R.string.nav_media, "Media"));
        sections.add(new Section(R.string.nav_money, "Money"));
        sections.add(new Section(R.string.nav_music, "Music"));
        sections.add(new Section(R.string.nav_politics, "Politics"));
        sections.add(new Section(R.string.nav_science, "Science"));
        sections.add(new Section(R.string.nav_society, "Society"));
        sections.add(new Section(R.string.nav_sport, "Sport"));
        sections.add(new Section(R.string.nav_technology, "Technology"));
        sections.add(new Section(R.string.nav_travel, "Travel"));
        sections.add(new Section(R.string.nav_tv_radio, "Tv & Radio"));
        sections.add(new Section(R.string.nav_weather, "Weather"));

        return sections;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reorder_sections);

        mSharedPreferences = getSharedPreferences(getString(R.string.settings_preferences), Context.MODE_PRIVATE);


        mSectionsRecycler = findViewById(R.id.recycler);
        LinearLayoutManager mLinearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mSectionsRecycler.setLayoutManager(mLinearLayoutManager);
        mSectionsRecycler.setDrawingCacheEnabled(true);
        mSectionsRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mSectionsRecycler.setItemViewCacheSize(100);


        mSections = getSectionsList();

        mSectionAdapter = new SectionAdapter(this, mSections, this, this);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mSectionAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mSectionsRecycler);
        mSectionsRecycler.setAdapter(mSectionAdapter);
    }

    private void createListofSortedSectionIds() {
        List<String> sortedIds = new ArrayList<>();
        for (Section section : mSections) {
            sortedIds.add("" + section.getResId());
        }

        Gson gson = new Gson();
        String jsonListOfSortedSectionsIds = gson.toJson(sortedIds);

        mEditor = mSharedPreferences.edit();

        mEditor.putString("list_of_sorted_sections_ids", jsonListOfSortedSectionsIds).apply();
    }

    private List<Section> getSectionsList() {
        List<Section> sectionsList = initSectionsList();
        List<Section> sortedSectionsList = new ArrayList<>();

        String jsonListOfSortedSectionsIds = mSharedPreferences.getString("list_of_sorted_sections_ids", "");

        if (!jsonListOfSortedSectionsIds.isEmpty()) {

            Gson gson = new Gson();
            List<String> listOfSortedSectionsId = gson.fromJson(jsonListOfSortedSectionsIds, new TypeToken<List<String>>() {
            }.getType());
            Log.d(TAG, "getSectionsList: listOfSortedSectionsId = " + listOfSortedSectionsId);
            if (listOfSortedSectionsId != null && listOfSortedSectionsId.size() > 0) {

                for (String id : listOfSortedSectionsId) {
                    for (Section section : sectionsList) {
                        if (String.valueOf(section.getResId()).equals(id)) {
                            sortedSectionsList.add(section);
                            sectionsList.remove(section);
                            break;
                        }
                    }
                }

            }

            if (sectionsList.size() > 0) {
                sortedSectionsList.addAll(sectionsList);
            }

            return sortedSectionsList;

        } else {
            return sectionsList;
        }
    }

    @Override
    public void onNoteListChanged(List<Section> customers) {
        createListofSortedSectionIds();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
