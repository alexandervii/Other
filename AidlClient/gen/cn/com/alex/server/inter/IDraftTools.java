/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: F:\\Workspace\\AidlClient\\src\\cn\\com\\alex\\server\\inter\\IDraftTools.aidl
 */
package cn.com.alex.server.inter;
public interface IDraftTools extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements cn.com.alex.server.inter.IDraftTools
{
private static final java.lang.String DESCRIPTOR = "cn.com.alex.server.inter.IDraftTools";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an cn.com.alex.server.inter.IDraftTools interface,
 * generating a proxy if needed.
 */
public static cn.com.alex.server.inter.IDraftTools asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof cn.com.alex.server.inter.IDraftTools))) {
return ((cn.com.alex.server.inter.IDraftTools)iin);
}
return new cn.com.alex.server.inter.IDraftTools.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getDraft:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.getDraft(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_setDraft:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.setDraft(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setPerson:
{
data.enforceInterface(DESCRIPTOR);
cn.com.alex.server.domain.Person _arg0;
if ((0!=data.readInt())) {
_arg0 = cn.com.alex.server.domain.Person.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.setPerson(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements cn.com.alex.server.inter.IDraftTools
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
/** 根据编号获取内容*/
@Override public java.lang.String getDraft(java.lang.String draftId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(draftId);
mRemote.transact(Stub.TRANSACTION_getDraft, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/** 设置内容*/
@Override public void setDraft(java.lang.String draftText) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(draftText);
mRemote.transact(Stub.TRANSACTION_setDraft, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/** 设置人信息*/
@Override public void setPerson(cn.com.alex.server.domain.Person person) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((person!=null)) {
_data.writeInt(1);
person.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_setPerson, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_getDraft = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_setDraft = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_setPerson = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
/** 根据编号获取内容*/
public java.lang.String getDraft(java.lang.String draftId) throws android.os.RemoteException;
/** 设置内容*/
public void setDraft(java.lang.String draftText) throws android.os.RemoteException;
/** 设置人信息*/
public void setPerson(cn.com.alex.server.domain.Person person) throws android.os.RemoteException;
}
