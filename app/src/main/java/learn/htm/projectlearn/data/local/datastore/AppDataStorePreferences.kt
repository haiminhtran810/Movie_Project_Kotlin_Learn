package learn.htm.projectlearn.data.local.datastore

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore

class AppDataStorePreferences(context: Context) : DataStorePrefHelper {
    private val APP_DATA_STORE_NAME = "data_store_name"
    private val dataStore: DataStore<Preferences> =
        context.createDataStore(name = APP_DATA_STORE_NAME)

    companion object {

    }
}