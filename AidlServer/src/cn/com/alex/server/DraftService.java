package cn.com.alex.server;

import java.util.UUID;

import cn.com.alex.server.domain.Person;
import cn.com.alex.server.inter.IDraftTools;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class DraftService extends Service {
	
	private IDraftBinder mBinder;
	private PropertyEditor mEditor;
	
	@Override
	public void onCreate() {
		mBinder = new IDraftBinder();
		mEditor = PropertyEditor.getInstance(getApplicationContext());
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	class IDraftBinder extends IDraftTools.Stub {

		@Override
		public String getDraft(String draftId) throws RemoteException {
			return (String)mEditor.getValue(draftId);
		}

		@Override
		public void setDraft(String draftText) throws RemoteException {
			mEditor.setValue(draftText);
		}

		@Override
		public void setPerson(Person person) throws RemoteException {
			mEditor.setPersonInfo(person);
		}
	}
}
