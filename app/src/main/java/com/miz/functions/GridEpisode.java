/*
 * Copyright (C) 2014 Michell Bak
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

package com.miz.functions;

import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.miz.mizuu.R;

import java.io.File;

import static com.miz.functions.PreferenceKeys.TVSHOWS_EPISODE_ORDER;

public class GridEpisode implements Comparable<GridEpisode> {

	private Context mContext;
	private File mCover;
	private String mSubtitleText, mTitle, mFilepath, mAirDate;
	private int mSeason, mEpisode;
	private boolean mWatched;

	public GridEpisode(Context context, String title, String filepath, int season, int episode, boolean watched, File cover, String airdate) {
		mContext = context;
		mTitle = title;
		mFilepath = filepath;
		mSeason = season;
		mEpisode = episode;
		mWatched = watched;
		mCover = cover;
		mAirDate = airdate;

		// Subtitle text
		StringBuilder sb = new StringBuilder();
		sb.append(mContext.getString(R.string.showEpisode)).append(" ").append(getEpisode());
		if (!hasWatched()) {
			sb.append(" ").append(mContext.getString(R.string.unwatched));
		}

		mSubtitleText = sb.toString();
	}
	
	public String getTitle() {
		if (TextUtils.isEmpty(mTitle)) {
			String temp = mFilepath.contains("<MiZ>") ? mFilepath.split("<MiZ>")[0] : mFilepath;
			File fileName = new File(temp);
			int pointPosition=fileName.getName().lastIndexOf(".");
			return pointPosition == -1 ? fileName.getName() : fileName.getName().substring(0, pointPosition);
		} else {
			return mTitle;
		}
	}

	public int getEpisode() {
		return mEpisode;
	}

	public int getSeason() {
		return mSeason;
	}

	public String getSeasonZeroIndex() {
		return MizLib.addIndexZero(mSeason);
	}

	public String getSubtitleText() {
		return mSubtitleText;
	}

	public boolean hasWatched() {
		return mWatched;
	}
	
	public File getCover() {
		return mCover;
	}
	
	public String getAirDate() {
		return mAirDate;
	}

	@Override
	public int compareTo(GridEpisode another) {
		String defaultOrder = mContext.getString(R.string.oldestFirst);
		boolean oldestFirst = PreferenceManager.getDefaultSharedPreferences(mContext).getString(TVSHOWS_EPISODE_ORDER, defaultOrder).equals(defaultOrder);
		int multiplier = oldestFirst ? 1 : -1;

		// Regular sorting
		if (getEpisode() < another.getEpisode())
			return -1 * multiplier;
		if (getEpisode() > another.getEpisode())
			return multiplier;
		return 0;
	}
}