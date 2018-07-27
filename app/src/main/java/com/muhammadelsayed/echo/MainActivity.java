/*
 *  Copyright [2017] [Muhammad Elsayed]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.muhammadelsayed.echo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.muhammadelsayed.echo.Fragments.App.HomeFragment;
import com.muhammadelsayed.echo.Fragments.App.SearchFragment;
import com.muhammadelsayed.echo.Fragments.App.SettingsFragment;
import com.muhammadelsayed.echo.Fragments.App.ShortcutsFragment;
import com.muhammadelsayed.echo.Fragments.App.SourcesFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";
  private static final String TAG_FRAGMENT_HOME = "tag_frag_home";
  private static final String TAG_FRAGMENT_SOURCES = "tag_frag_sources";
  private static final String TAG_FRAGMENT_SHORTCUTS = "tag_frag_shortcuts";
  private static final String TAG_FRAGMENT_SEARCH = "tag_frag_search";
  private static final String TAG_FRAGMENT_SETTINGS = "tag_frag_settings";
  private static final int INT_FRAGMENTS_COUNT = 5;
  private static final int INT_FRAGMENT_HOME_POS = 0;
  private static final int INT_FRAGMENT_SOURCES_POS = 1;
  private static final int INT_FRAGMENT_SHORTCUTS_POS = 2;
  private static final int INT_FRAGMENT_SEARCH_POS = 3;
  private static final int INT_FRAGMENT_SETTINGS_POS = 4;
  public BottomNavigationView mBottomNavigation;
  private List<Fragment> mFragmentsList = new ArrayList<>(INT_FRAGMENTS_COUNT);

  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
      new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
          switch (item.getItemId()) {
            case R.id.navigation_home:
              switchFragment(INT_FRAGMENT_HOME_POS, TAG_FRAGMENT_HOME);
              return true;
            case R.id.navigation_sources:
              switchFragment(INT_FRAGMENT_SOURCES_POS, TAG_FRAGMENT_SOURCES);
              return true;
            case R.id.navigation_shortcuts:
              switchFragment(INT_FRAGMENT_SHORTCUTS_POS, TAG_FRAGMENT_SHORTCUTS);
              return true;
            case R.id.navigation_search:
              switchFragment(INT_FRAGMENT_SEARCH_POS, TAG_FRAGMENT_SEARCH);
              return true;
            case R.id.navigation_settings:
              switchFragment(INT_FRAGMENT_SETTINGS_POS, TAG_FRAGMENT_SETTINGS);
              return true;
          }
          return false;
        }
      };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Log.d(TAG, "onCreate: started !!");

    mBottomNavigation = findViewById(R.id.bottom_navigation_view);
    mBottomNavigation.getMenu().getItem(0).setChecked(true);
    mBottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    disableShiftMode(mBottomNavigation);
    buildFragmentsList();
    // Set the 0th Fragment to be displayed by default.
    switchFragment(0, TAG_FRAGMENT_HOME);
  }

  /**
   * Use this method to create a new instance of any of the fragments using the fragments' factory
   * methods.
   *
   * <p>Then, the method puts the created fragments in the "mFragmentsList"
   */
  private void buildFragmentsList() {
    Log.wtf(TAG, "buildFragmentsList() has been instantiated");

    HomeFragment homeFragment = HomeFragment.homeFragmentInstance();
    SourcesFragment sourcesFragment = SourcesFragment.sourcesFragmentInstance();
    ShortcutsFragment shortcutsFragment = ShortcutsFragment.shortcutsFragmentInstance();
    SearchFragment searchFragment = SearchFragment.searchFragmentInstance();
    SettingsFragment settingsFragment = SettingsFragment.settingsFragmentInstance();

    mFragmentsList.add(homeFragment);
    mFragmentsList.add(sourcesFragment);
    mFragmentsList.add(shortcutsFragment);
    mFragmentsList.add(searchFragment);
    mFragmentsList.add(settingsFragment);
  }

  /**
   * This generic method is used to handle switching between fragments.
   *
   * @param pos The position of fragment at the "mFragmentList".
   * @param tag The tag name for the fragment.
   */
  private void switchFragment(int pos, String tag) {
    Log.wtf(TAG, "switchFragment() has been instantiated");

    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.frame_fragment_holder, mFragmentsList.get(pos), tag)
        .commit();
  }

  /**
   * This method will force the BottomNavigationView to show both the icon and the label of each
   * element in the BottomNavigationView, not only the highlighted element
   *
   * <p>I got this method from STACKOVERFLOW.com and here's the link see <a
   * href="https://stackoverflow.com/questions/41352934/force-showing-icon-and-title-in-bottomnavigationview-support-android/41374515"</a>
   *
   * @param view is the BottomNavigationView object on which the force showing will be applied
   */
  @SuppressLint("RestrictedApi")
  public static void disableShiftMode(BottomNavigationView view) {
    Log.wtf(TAG, "disableShiftMode() has been instantiated");

    BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
    try {
      Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
      shiftingMode.setAccessible(true);
      shiftingMode.setBoolean(menuView, false);
      shiftingMode.setAccessible(false);
      for (int i = 0; i < menuView.getChildCount(); i++) {
        BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
        item.setShiftingMode(false);
        // set once again checked value, so view will be updated
        item.setChecked(item.getItemData().isChecked());
      }
    } catch (NoSuchFieldException e) {
      // Timber.e(e, "Unable to get shift mode field");
    } catch (IllegalAccessException e) {
      // Timber.e(e, "Unable to change value of shift mode");
    }
  }
}
