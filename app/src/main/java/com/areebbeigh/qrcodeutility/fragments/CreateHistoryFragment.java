/**
 * Copyright 2016 Areeb Beigh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.areebbeigh.qrcodeutility.fragments;

import com.areebbeigh.qrcodeutility.data.HistoryDBHandler;
import com.areebbeigh.qrcodeutility.util.HistoryObject;

import java.util.ArrayList;

public class CreateHistoryFragment extends BaseHistoryFragment {
    @Override
    public ArrayList<HistoryObject> getItems() {
        HistoryDBHandler dbHandler = new HistoryDBHandler(getActivity(), null, null, 0);
        ArrayList<HistoryObject> items = new ArrayList<>();

        for (String[] array : dbHandler.createDatabaseToList()) {
            items.add(new HistoryObject(array[0], array[1], Integer.parseInt(array[2])));
        }

        return items;
    }

}
