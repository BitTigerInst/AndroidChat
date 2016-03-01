package dropbox;

import android.content.Context;
import android.os.AsyncTask;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;

/**
 * Task to share a file from Dropbox by creating a Shared Link
 */
public class SharedLinkTask extends AsyncTask<FileMetadata, Void, String> {
    private final Context mContext;
    private final DbxClientV2 mDbxClient;
    private final Callback mCallback;
    private Exception mException;

    SharedLinkTask(Context context, DbxClientV2 dbxClient, Callback callback) {
        mContext = context;
        mDbxClient = dbxClient;
        mCallback = callback;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (mException != null) {
            mCallback.onError(mException);
        } else {
            mCallback.onCreatedSharedLink(result);
        }
    }

    @Override
    protected String doInBackground(FileMetadata... params) {
        FileMetadata metadata = params[0];
        try {
            return mDbxClient.sharing.createSharedLink(metadata.getPathLower()).getUrl();
        } catch (DbxException e) {
            mException = e;
        }

        return null;
    }

    public interface Callback {
        void onCreatedSharedLink(String result);

        void onError(Exception e);
    }
}
