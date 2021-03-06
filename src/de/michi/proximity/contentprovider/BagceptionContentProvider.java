package de.michi.proximity.contentprovider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class BagceptionContentProvider extends ContentProvider {
	
	static final String PROVIDER_NAME = "de.michi.proximity.contentprovider.Items";
	static final String URL = "content://" + PROVIDER_NAME + "/items";
	
	public static final Uri CONTENT_URI = Uri.parse(URL);
	public static final String _ID = "_id";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "descritption";
	public static final String VISIBILITY = "visibility";
	public static final String TAG_ID = "tag_id";
	
	private static HashMap<String, String> ITEMS_PROJECTION_MAP;
	
	public static final int ITEMS = 1;
	public static final int ITEM_ID = 2;
	
	public static UriMatcher uriMatcher;
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "items", ITEMS);
		uriMatcher.addURI(PROVIDER_NAME, "items/#", ITEM_ID);
	}
	
	// Database specific constants 
	private SQLiteDatabase db;
	static final String DATABASE_NAME = "Items";
	static final String ITEM_TABLE_NAME = "items";
	static final int DATABASE_VERSION = 1;
	public static final String CREATE_DB_TABLE = 
			"CREATE TABLE " + ITEM_TABLE_NAME + 
			" (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			" name TEXT NOT NULL, " +
			" description TEXT NOT NULL, " +
			" visibility TEXT NOT NULL, " +
			" tag_id ID NOT NULL);";

	
	public class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context){
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_DB_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE_NAME);
			onCreate(db);
		}

	}
	
	@Override
	public boolean onCreate() {

		Context context = getContext();
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		
		// Create a writable database
		db = dbHelper.getWritableDatabase();
		return (db == null)? false:true;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {

		// Add new item record
		long rowID = db.insert(ITEM_TABLE_NAME, "", values);
		
		// If record is added successfully
		if(rowID > 0) {
			Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(_uri, null);
			return uri;
		}
	throw new SQLException("Failed to add a record into " + uri);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(ITEM_TABLE_NAME);
		
		switch (uriMatcher.match(uri)) {
		case ITEMS: qb.setProjectionMap(ITEMS_PROJECTION_MAP);
			break;
		case ITEM_ID: qb.appendWhere(_ID + "=" + uri.getPathSegments().get(1));
			break;
		default: throw new IllegalArgumentException("Unknown URI " + uri);
		}
		if(sortOrder == null || sortOrder == ""){
			
			//By default sort on item names
			sortOrder = NAME;
		}
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		
		// Register to watch a content URI for changes
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
	
		int count = 0;
		
		switch(uriMatcher.match(uri)) {
		case ITEMS: count = db.delete(ITEM_TABLE_NAME, selection, selectionArgs);
			break;
		case ITEM_ID: String id = uri.getPathSegments().get(1);
			count = db.delete(ITEM_TABLE_NAME, _ID + " = " + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
			break;
		default: throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

		int count = 0;
		
		switch (uriMatcher.match(uri)){
		case ITEMS: count = db.update(ITEM_TABLE_NAME, values, selection, selectionArgs);
			break;
		case ITEM_ID: count = db.update(ITEM_TABLE_NAME, values, _ID + " = " + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
			break;
		default: throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
	
	@Override
	public String getType(Uri uri) {

		return null;
	}

}
