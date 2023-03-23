/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import client.components.BoardCtrl;
import client.components.BoardJoinCtrl;
import client.components.CardCtrl;
import client.components.CardListCtrl;
import client.scenes.*;
import client.utils.ClientUtils;
import client.utils.ComponentFactory;
import client.utils.ExceptionHandler;
import client.utils.ServerUtils;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

import java.util.prefs.Preferences;

public class MyModule implements Module {

    @Override
    public void configure(Binder binder) {
        // Scenes
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(ServerConnectCtrl.class).in(Scopes.SINGLETON);
        binder.bind(BoardSettingsCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AddCardCtrl.class).in(Scopes.SINGLETON);
        binder.bind(MainViewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AddListCtrl.class).in(Scopes.SINGLETON);
        binder.bind(CreateBoardCtrl.class).in(Scopes.SINGLETON);
        binder.bind(JoinBoardsCtrl.class).in(Scopes.SINGLETON);
        binder.bind(ListSettingsCtrl.class).in(Scopes.SINGLETON);

        // Components
        binder.bind(CardCtrl.class);
        binder.bind(CardListCtrl.class);
        binder.bind(BoardCtrl.class);
        binder.bind(BoardJoinCtrl.class);

        // Utils
        binder.bind(ServerUtils.class).in(Scopes.SINGLETON);
        binder.bind(Preferences.class).toInstance(Preferences.userRoot());
        binder.bind(ClientUtils.class).in(Scopes.SINGLETON);
        binder.bind(ComponentFactory.class).in(Scopes.SINGLETON);
        binder.bind(ExceptionHandler.class).in(Scopes.SINGLETON);
    }
}