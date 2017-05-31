
package com.heniktechnology.hncore.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.heniktechnology.hncore.utility.HNLoger.debug;


public class HNDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "NewEdelWeissEAAA.db";
    public static Context context;
    public static String ROW_ID = "_id";
    private static HNDatabaseHelper instance;
    private final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    private final String DROP_TABLE = "DROP TABLE IF EXISTS ";
    private final String TEXT_TYPE = " TEXT ";
    private final String INT_TYPE = " INTEGER ";
    private final String PRIMARY_KEY = " PRIMARY KEY ";
    private final String AUTO_INCREMENT = " AUTOINCREMENT ";
    private final String NOT_NULL = " NOT NULL ";
    private final String COMMA_SEP = " , ";
    private final String OPEN_BRACE = " ( ";
    private final String CLOSE_BRACE = " ); ";
    private String TAG = HNDatabaseHelper.class.getSimpleName();
    private SQLiteDatabase db = null;
    private String COLUMN_DATA = "data";
    private String COLUMN_NAME = "name";
    private String TABLE_SYNC_DETAIL = "sync_detail";
    private String COLUMN_ENTITY_NAME = "entity_name";
    private String COLUMN_LAST_SYNC_DATE = "last_sync_date";
    private String COLUMN_ID = "columnId";

    private String password;
    private Cursor cursor = null;
    private String selection;
    private List<String> result;
    private List<DataBaseRowIdAndObject> dataBaseRowIdAndObjects;
    private DataBaseRowIdAndObject dataBaseRowIdAndObject;
    private String[] selectionArgs;

    private HNDatabaseHelper(Context context, String password) {

        super(context, DATABASE_NAME, null, 1);
        this.context = context;
        this.password = password;
        debug("database Pasword is : " +password);
    }

    public static HNDatabaseHelper getInstance(Context context, String password) {
        if (instance == null) {
            instance = new HNDatabaseHelper(context, password);

        }

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    private SQLiteDatabase getWritableDatabase(String encryption_key) // TODO: need to check
    {
        //return this.getWritableDatabase(encryption_key);
        return this.getWritableDatabase();
    }

    private SQLiteDatabase getReadableDatabase(String encryption_key) {
        //return this.getReadableDatabase(encryption_key);
        return this.getReadableDatabase();
    }

    public static boolean closeDB(SQLiteDatabase db) {
        if (db != null) {
            //	db.close();
            return true;
        }
        return false;

    }

    public static boolean closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
            return true;
        }
        return false;

    }

    public boolean createTableForJSON(String tableName) {
        try {
            String createTableStatement = "";
            db = getWritableDatabase(password);
            createTableStatement = CREATE_TABLE + tableName + OPEN_BRACE + ROW_ID + INT_TYPE + PRIMARY_KEY + AUTO_INCREMENT
                    + COMMA_SEP + COLUMN_NAME + TEXT_TYPE + NOT_NULL + COMMA_SEP + COLUMN_DATA + TEXT_TYPE + NOT_NULL + CLOSE_BRACE;
            debug(TAG, "QUERY TABLE_TASK:" + createTableStatement);
            db.execSQL(createTableStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDB(db);
        }
        return false;
    }

    public boolean createTableForDynamicTable(String tableName, List<String> coloumnNames) {

        try {
            boolean isTableExist = isTableExist(tableName);
            if (!isTableExist) {
                String createTableStatement = "";
                StringBuilder dynamicColumn = new StringBuilder();
                debug("coloumnNames.size()  :"+coloumnNames.size());
                for (int i = 0; i < coloumnNames.size(); i++) {
                    String ColoumnName = coloumnNames.get(i);
                    //  if (i != coloumnNames.size())
                    {
                        dynamicColumn.append(COMMA_SEP + ColoumnName + TEXT_TYPE);
                    }

                }
                db = getWritableDatabase(password);
                createTableStatement = CREATE_TABLE + tableName + OPEN_BRACE + ROW_ID + INT_TYPE + PRIMARY_KEY + AUTO_INCREMENT
                        + dynamicColumn.toString() + CLOSE_BRACE;
                debug(TAG, "QUERY TABLE_TASK:" + createTableStatement);
                db.execSQL(createTableStatement);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDB(db);
        }
        return false;
    }

    public boolean isTableExist(String tableName) {
        db = getWritableDatabase(password);
        cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type ='table' AND name='" + tableName + "';", new String[]{});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }


    }


    public boolean createTableForSync() {
        try {
            String createTableStatement = "";

            db = getWritableDatabase(password);
            boolean isTableExist = isTableExist(TABLE_SYNC_DETAIL);
            if (!isTableExist) {
                createTableStatement = CREATE_TABLE + TABLE_SYNC_DETAIL + OPEN_BRACE + COLUMN_ID + INT_TYPE + PRIMARY_KEY
                        + AUTO_INCREMENT + COMMA_SEP + COLUMN_ENTITY_NAME + TEXT_TYPE + NOT_NULL + COMMA_SEP
                        + COLUMN_LAST_SYNC_DATE + TEXT_TYPE + NOT_NULL + CLOSE_BRACE;
                debug(TAG, "QUERY TABLE_TASK:" + createTableStatement);
                db.execSQL(createTableStatement);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDB(db);
        }
        return false;
    }


    public String insertJSON(String tableName, String columnName, String data) {
        String rowId = "-1";
        try {
            createTableForJSON(tableName);
            db = getWritableDatabase(password);
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_NAME, columnName);
            contentValues.put(COLUMN_DATA, data);
            rowId = (String.valueOf(db.insert(tableName, null, contentValues)));
            debug(TAG + " inserting data:-  " + data);
            return rowId;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDB(db);
        }


        return rowId;
    }

    public boolean deleteJSON(String tabaleName) {
        boolean deleteStatus = false;
        try {
            createTableForJSON(tabaleName);
            db = getWritableDatabase(password);
            cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type ='table' AND name='" + tabaleName + "';", new String[]{});
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                cursor = db.rawQuery("DELETE FROM " + tabaleName, new String[]{});
                cursor.moveToFirst();
                debug("deliting " + tabaleName);
                if (cursor.getCount() > 0)
                    deleteStatus = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDB(db);
        }
        return deleteStatus;
    }


    public boolean deleteJSON(String tabaleName, String dbRowId) {
        boolean deleteStatus = false;
        try {
            createTableForJSON(tabaleName);
            db = getWritableDatabase(password);
            int deleteCount = db.delete(tabaleName, ROW_ID + " =? ", new String[]{dbRowId});

            if (deleteCount > 0) {
                deleteStatus = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDB(db);
        }

        return deleteStatus;
    }

    public boolean updateJSONTable(String tableName, String dbRowId, String data) {
        int updateCount = 0;
        boolean updateStatus = false;
        try {
            createTableForJSON(tableName);
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_DATA, data);
            db = getWritableDatabase(password);
            updateCount = db.update(tableName, contentValues, ROW_ID + " =? ", new String[]{dbRowId});
            if (updateCount > 0)
                updateStatus = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeDB(db);
        }


        return updateStatus;
    }


    public List<String> getObject(String tableName, String columnName) {
        db = getReadableDatabase();
        result = new ArrayList<String>();
        selection = COLUMN_NAME + "=?";
        selectionArgs = new String[]{columnName};
        try {
            createTableForJSON(tableName);
            cursor = db.query(tableName, new String[]{COLUMN_DATA}, selection, selectionArgs, null, null, null);
            //  cursor = db.query(true,tableName, new String[]{COLUMN_DATA}, selection, selectionArgs, null, null, null,"");
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    result.add(cursor.getString(cursor.getColumnIndex(COLUMN_DATA)));
                    cursor.moveToNext();
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            closeDB(db);
        }
        return null;
    }

    public List<DataBaseRowIdAndObject> getObjectWithRowId(String tableName, String columnName) {
        db = getReadableDatabase();
        dataBaseRowIdAndObjects = new ArrayList<DataBaseRowIdAndObject>();
        selection = COLUMN_NAME + "=?";
        selectionArgs = new String[]{columnName};
        try {
            createTableForJSON(tableName);
            cursor = db.query(tableName, new String[]{COLUMN_DATA, ROW_ID}, selection, selectionArgs, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    dataBaseRowIdAndObject = new DataBaseRowIdAndObject();
                    dataBaseRowIdAndObject.setObject(cursor.getString(cursor.getColumnIndex(COLUMN_DATA)));
                    dataBaseRowIdAndObject.setRowid(cursor.getString(cursor.getColumnIndex(ROW_ID)));
                    dataBaseRowIdAndObjects.add(dataBaseRowIdAndObject);
                    cursor.moveToNext();
                }
            }
            return dataBaseRowIdAndObjects;
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            closeDB(db);
        }
        return null;
    }




    /*dynamic row id*/

    public Cursor getObjectWithRowIdDynamic(String tableName, List<String> columnName, List<ColumnDataPair> where) {
        db = getReadableDatabase();

        StringBuilder selection = new StringBuilder();

        if (where != null) {
            selectionArgs = new String[where.size()];
            for (int i = 0; i < where.size(); i++) {
                selectionArgs[i] = where.get(i).getData();
                if (i != where.size() - 1) {
                    selection.append(where.get(i).getColumn() + "=? , ");
                } else {
                    selection.append(where.get(i).getColumn() + "=?");
                }
            }
        } else {
            selectionArgs = new String[]{};
        }

        // columnName is static Varivbal cnt diretly add on this
        List<String> stringscolumnName= new ArrayList<>(columnName);
        stringscolumnName.add(ROW_ID);
        String[] columns = new String[stringscolumnName.size()];
        stringscolumnName.toArray(columns);
        cursor = db.query(tableName, columns, selection.toString(), selectionArgs, null, null, null);

        try {
            if (cursor != null) {
                cursor.moveToFirst();
                return cursor;
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                // cursor.close();
            }
            closeDB(db);
        }
        return null;
    }
/*Test to get distinct data*/

    public Cursor getDistinctWithRowIdDynamic(String tableName,String COLUMN_PPID) {
        db = getReadableDatabase();

        cursor = db.rawQuery("select * from "+tableName+ " group by "+ COLUMN_PPID + " ;",null);
        //  cursor = db.rawQuery("select * from "+tableName+" where " + COLUMN_PPID + " =  \"True\""  + " group by "+ COLUMN_PPID + " ;",null);

        try {
            if (cursor != null) {
                cursor.moveToFirst();
                return cursor;
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                // cursor.close();
            }
            closeDB(db);
        }
        return null;
    }

    public Cursor getDistinctWithRowIdDynamicCount(String tableName,String COLUMN_IS_TRADER_SUBMITTED,String COLUMN_PPID) {
        db = getReadableDatabase();

        //cursor = db.rawQuery("select * from "+tableName+ " group by "+ COLUMN_PPID + " ;",null);
        cursor = db.rawQuery("select * from "+tableName+" where " + COLUMN_IS_TRADER_SUBMITTED + " =  \"True\"" + " group by " + COLUMN_PPID + " ;",null);

        try {
            if (cursor != null) {
                cursor.moveToFirst();
                return cursor;
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                // cursor.close();
            }
            closeDB(db);
        }
        return null;
    }
    public Cursor getDistinctWithRowIdDynamicWhere(String tableName,String COLUMN_PPID,String wherecolumnName,String whereValue) {
        db = getReadableDatabase();

        //cursor = db.rawQuery("select * from "+tableName+ " group by "+ COLUMN_PPID + " where " + wherecolumnName + "=' " + whereValue +" ;",null);
        cursor = db.rawQuery("select * from "+tableName+" where " + wherecolumnName + " =  \"Pending\""  + " group by "+ COLUMN_PPID + " ;",null);

        try {
            if (cursor != null) {
                cursor.moveToFirst();
                return cursor;
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                // cursor.close();
            }
            closeDB(db);
        }
        return null;
    }

    public Cursor getDistinctWithRowIdDynamicWhereFilter(String tableName,String COLUMN_PPID,String wherecolumnName,String whereValue) {
        db = getReadableDatabase();

        //cursor = db.rawQuery("select * from "+tableName+ " group by "+ COLUMN_PPID + " where " + wherecolumnName + "=' " + whereValue +" ;",null);
        cursor = db.rawQuery("select * from "+tableName+" where " + wherecolumnName + " = '"+ whereValue  + "' group by "+ COLUMN_PPID + " ;",null);

        try {
            if (cursor != null) {
                cursor.moveToFirst();
                return cursor;
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                // cursor.close();
            }
            closeDB(db);
        }
        return null;
    }

    public Cursor getDistinctWithRowIdDynamicWhereFilterByMultipleColumn(String tableName,String COLUMN_PPID,String whereFirstcolumnName,String firstwhereValue,String whereSecondColumnName, String secondwhereValue, String whereThirdColumnName, String thirdWhereValue,String fourthWhereColumnName,String status) {
        // public Cursor getDistinctWithRowIdDynamicWhereFilterByMultipleColumn(String tableName,String COLUMN_PPID,String whereFirstcolumnName,String firstwhereValue,String whereSecondColumnName, String secondwhereValue) {
        db = getReadableDatabase();

        //cursor = db.rawQuery("select * from "+tableName+ " group by "+ COLUMN_PPID + " where " + wherecolumnName + "=' " + whereValue +" ;",null);
        //  cursor = db.rawQuery("select * from "+tableName+" where " +whereSecondColumnName+ " = '"+ secondwhereValue+ "'"+ " or " + whereFirstcolumnName + " = '"+ firstwhereValue  + "' group by "+ COLUMN_PPID + " ;",null);

        if("".equals(status) && "".equals(fourthWhereColumnName)) {
            debug("status   " + status + "  fourthWhereColumnName  " + fourthWhereColumnName );
            cursor = db.rawQuery("select * from " + tableName + " where " + whereFirstcolumnName + " = '" + firstwhereValue + "'" + " or " + whereSecondColumnName + " = '" + secondwhereValue + "'" + " or " + whereThirdColumnName + " = '" + thirdWhereValue + "' group by " + COLUMN_PPID + " ;", null);
        }
        else {

            debug("status is send and it is status  " + status + "  fourthWhereColumnName  " + fourthWhereColumnName );
            //cursor = db.rawQuery("select * from " + tableName + " where " + whereFirstcolumnName + " = '" + firstwhereValue + "'" + " or " + whereSecondColumnName + " = '" + secondwhereValue + "'" + " or " + whereThirdColumnName + " = '" + thirdWhereValue + "'" +  " AND " +  fourthWhereColumnName + " =  \"Pending\"" + " group by " + COLUMN_PPID + " ;", null);

            cursor=db.rawQuery("select * from " + tableName + " where " +  fourthWhereColumnName + " =  \"Pending\"" +  " and " +  "(" + whereFirstcolumnName +  " = '" + firstwhereValue + "'" + " or " + whereSecondColumnName + " = '" + secondwhereValue + "'" + " or " + whereThirdColumnName + " = '" + thirdWhereValue   + "')"  + " group by " + COLUMN_PPID + " ;", null);
        }

        try {
            if (cursor != null) {
                cursor.moveToFirst();
                return cursor;
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                // cursor.close();
            }
            closeDB(db);
        }
        return null;
    }


    public String insertDynamicTable(String tableName, List<ColumnDataPair> data) {
        String rowId = "-1";
        try {
            //createTableForJSON(tableName);
            for (int i = 0; i < data.size(); i++) {
                ColumnDataPair columnDataPair = data.get(i);
                debug(TAG + " inserting :  " + "Column  : "+i +"   :->" + columnDataPair.getColumn() + "    Data   : " + columnDataPair.getData());
            }
            db = getWritableDatabase(password);
            ContentValues contentValues;
            contentValues = new ContentValues();
            for (int i = 0; i < data.size(); i++) {
                ColumnDataPair columnDataPair = data.get(i);
                contentValues.put(columnDataPair.getColumn(), columnDataPair.getData());
            }
            rowId = (String.valueOf(db.insert(tableName, null, contentValues)));

            debug(TAG + " inserting data:-  " + data);
            return rowId;
        } catch (Exception e) {
            e.printStackTrace();
            debug(TAG + " erorr in db  " + e);
        } finally {
            closeDB(db);
        }
        return rowId;
    }

    public boolean updateDynamicTable(String tableName, String dbRowId, List<ColumnDataPair> data) {

        int updateCount = 0;
        try {
            // createTableForJSON(tableName);

            db = getWritableDatabase(password);
            ContentValues contentValues;
            contentValues = new ContentValues();
            for (int i = 0; i < data.size(); i++) {
                ColumnDataPair columnDataPair = data.get(i);
                contentValues.put(columnDataPair.getColumn(), columnDataPair.getData());
                debug(TAG + " inserting :  " + "Column  : "+i +"   :->" + columnDataPair.getColumn() + "    Data   : " + columnDataPair.getData());
            }
            updateCount = db.update(tableName, contentValues, ROW_ID + " =? ", new String[]{dbRowId});
            if (updateCount > 0) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDB(db);
        }
        return false;
    }


    public boolean deleteDynamicTable(String tabaleName) {
        boolean deleteStatus = false;
        try {
            db = getWritableDatabase(password);

            cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type ='table' AND name='" + tabaleName + "';", new String[]{});
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                cursor = db.rawQuery("DELETE FROM " + tabaleName, new String[]{});
                cursor.moveToFirst();
                debug("deliting " + tabaleName);
                if (cursor.getCount() > 0)
                    deleteStatus = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDB(db);
        }
        return deleteStatus;
    }


    public boolean deleteDynamicTable(String tabaleName, String dbRowId) {
        boolean deleteStatus = false;
        try {
            db = getWritableDatabase(password);
            cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type ='table' AND name='" + tabaleName + "';", new String[]{});
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                int deleteCount = db.delete(tabaleName, ROW_ID + " =? ", new String[]{dbRowId});

                if (deleteCount > 0) {
                    deleteStatus = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDB(db);
        }

        return deleteStatus;
    }


    //Sync date Storage

    public boolean storeLastSyncDate(String entity) {
        debug(TAG, "---storeLastSyncDate entity: " + entity);
        String DATE_FORMAT = "dd MMM yyyy HH:mm:ss";
        Cursor cursor = null;
        try {
            db = this.getWritableDatabase(password);
            createTableForSync();
            cursor = db.query(TABLE_SYNC_DETAIL, new String[]{COLUMN_ID},
                    COLUMN_ENTITY_NAME + " like '%" + entity + "%'", null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();

                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));

                int noOfRowsAffected = db.delete(TABLE_SYNC_DETAIL, COLUMN_ID + " = ?",
                        new String[]{String.valueOf(id)});
                debug(TAG, "---storeLastSyncDate noOfRowsAffected: " + noOfRowsAffected);
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
            String lastSyncDate = simpleDateFormat.format(new Date());

            ContentValues contentValues = new ContentValues();

            if (entity != null) {
                contentValues.put(COLUMN_ENTITY_NAME, entity);
                contentValues.put(COLUMN_LAST_SYNC_DATE, lastSyncDate);

                db.insert(TABLE_SYNC_DETAIL, null, contentValues);

                debug(TAG, "---storeLastSyncDate lastSyncDate: " + lastSyncDate);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        // api/search/details?fromtime=21 Jul 2015 10:25
        return false;
    }

    public String getLastSyncDate(String entity) {
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            createTableForSync();
            cursor = db.query(TABLE_SYNC_DETAIL, new String[]{COLUMN_LAST_SYNC_DATE},
                    COLUMN_ENTITY_NAME + " like '%" + entity + "%'", null, null, null, null);

            if (null == cursor || cursor.getCount() <= 0)
                return "";
            cursor.moveToFirst();

            return cursor.getString(cursor.getColumnIndex(COLUMN_LAST_SYNC_DATE));
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return "";
    }


    public static boolean deleteDatabase(Context context)
    {
        debug("delited data base");
        instance = null;
        return context.deleteDatabase(DATABASE_NAME);
    }

    public static class DataBaseRowIdAndObject {
        private String rowid;
        private Object object;

        public String getRowid() {
            return rowid;
        }

        public void setRowid(String rowid) {
            this.rowid = rowid;
        }

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }
    }

    public static class ColumnDataPair {
        private String Column;
        private String Data;

        public String getColumn() {
            return Column;
        }

        public void setColumn(String column) {
            Column = column;
        }

        public String getData() {
            return Data;
        }

        public void setData(String data) {
            Data = data;
        }
    }




}
