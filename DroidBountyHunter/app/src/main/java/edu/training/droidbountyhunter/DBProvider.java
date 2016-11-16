package edu.training.droidbountyhunter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import java.util.Date;

/**
 * Created by Eden on 17/08/2016.
 */
public class DBProvider {
    private DBHelper oDB;
    private SQLiteDatabase db;

    public DBProvider(Context context){
        oDB = new DBHelper(context);
    }

    public void CloseDB(){
        if(db.isOpen()){
            db.close();
        }
    }

    public boolean isOpenDB(){
        return(db.isOpen());
    }

    public long executeSQL(String sql, Object[] bindArgs){
        long iRet = 0;

        db = oDB.getWritableDatabase();

        db.execSQL(sql, bindArgs);

        CloseDB();

        return iRet;
    }

    public Cursor querySQL(String sql, String[] selectionArgs){
        Cursor oRet = null;

        db = oDB.getReadableDatabase();
        oRet = db.rawQuery(sql, selectionArgs);

        return  oRet;
    }

    public void DeleteFugitivo(String pID){
        Object[] aData = {pID};
        executeSQL("DELETE FROM " + DBHelper.TABLE_NAME + " WHERE " + DBHelper._ID + " =?", aData);
    }

    public void UpdateFugitivo(String pStatus, String pID, String pFoto, double pLat, double pLon, String fecha){
        Object[] aData = {pStatus, pFoto, String.valueOf(pLat), String.valueOf(pLon), fecha, pID};
        executeSQL(  "UPDATE " + DBHelper.TABLE_NAME +
                    " SET " + DBHelper.COLUMN_NAME_STATUS + " = ?, " +
                              DBHelper.COLUMN_NAME_FOTO + " = ? ," +
                              DBHelper.COLUMN_NAME_LAT + " = ? ," +
                              DBHelper.COLUMN_NAME_LON + " = ? ," +
                              DBHelper.COLUMN_NAME_FECHA + " = ? " +
                    " WHERE " + DBHelper._ID + " = ?", aData);
    }

    public void InsertFugitivo(String pNombre){
        Object[] aData = {pNombre, "0"};
        executeSQL("INSERT INTO " + DBHelper.TABLE_NAME + "(" + DBHelper.COLUMN_NAME_NAME + ", " + DBHelper.COLUMN_NAME_STATUS + ") VALUES (?,?)", aData);
    }

    public String[][] ObtenerFugitivos(boolean pCapturado){
        int iCnt = 0;
        String[][] aData = null;
        String[] aFile = {(pCapturado?"1":"0")};

        Cursor aRS = querySQL("SELECT * FROM " + DBHelper.TABLE_NAME + " WHERE " + DBHelper.COLUMN_NAME_STATUS + " = ? ORDER BY " + DBHelper.COLUMN_NAME_NAME, aFile );

        if(aRS.getCount() > 0){
            aData = new String[aRS.getCount()][];
            while (aRS.moveToNext()){
                aData[iCnt] = new String[7];
                aData[iCnt][0] = aRS.getString(aRS.getColumnIndex(DBHelper._ID));
                aData[iCnt][1] = aRS.getString(aRS.getColumnIndex(DBHelper.COLUMN_NAME_NAME));
                aData[iCnt][2] = aRS.getString(aRS.getColumnIndex(DBHelper.COLUMN_NAME_STATUS));
                aData[iCnt][3] = aRS.getString(aRS.getColumnIndex(DBHelper.COLUMN_NAME_FOTO));
                aData[iCnt][4] = aRS.getString(aRS.getColumnIndex(DBHelper.COLUMN_NAME_LAT));
                aData[iCnt][5] = aRS.getString(aRS.getColumnIndex(DBHelper.COLUMN_NAME_LON));
                aData[iCnt][6] = aRS.getString(aRS.getColumnIndex(DBHelper.COLUMN_NAME_FECHA));

                iCnt++;
            }
        }
        else{
            aData = new String[0][];
        }

        aRS.close();
        CloseDB();
        return aData;
    }

    public int ContarFugitivos(){
        int iCnt = 0;
        String[] aFils = {""};

        Cursor aRS = querySQL("SELECT " + DBHelper._ID + " FROM " + DBHelper.TABLE_NAME + " WHERE id <> ?", aFils);

        iCnt = aRS.getCount();

        aRS.close();

        CloseDB();

        return iCnt;
    }

    public static class DBHelper extends SQLiteOpenHelper{
        private static final String TAG = "DBManager";
        private static final String DATABASE_NAME = "droidBH.db";
        private static final int DATABASE_VERSION = 5;
        private static final String TABLE_NAME = "fugitivos";
        private static final String _ID = "id";
        private static final String COLUMN_NAME_NAME = "name";
        private static final String COLUMN_NAME_STATUS = "status";
        private static final String COLUMN_NAME_FOTO = "foto";
        public static final String COLUMN_NAME_LAT = "lat";
        public static final String COLUMN_NAME_LON = "lon";
        public static final String COLUMN_NAME_FECHA = "fecha";

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w("[CHECK]", "DBHelper.onCreate...");
            db.execSQL("CREATE TABLE " + DBHelper.TABLE_NAME + " (" +
                        DBHelper._ID + " INTEGER PRIMARY KEY, " +
                        DBHelper.COLUMN_NAME_NAME + " TEXT, " +
                        DBHelper.COLUMN_NAME_FOTO + " TEXT, " +
                        DBHelper.COLUMN_NAME_LAT + " TEXT, " +
                        DBHelper.COLUMN_NAME_LON + " TEXT, " +
                        DBHelper.COLUMN_NAME_FECHA + " TEXT, " +
                        DBHelper.COLUMN_NAME_STATUS + " INTEGER" +
                        ");"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("TAG", "Actualización de la BDD de la version " + oldVersion + " a la " + newVersion +
                         ", de la que se destruira la información anterior.");

            db.execSQL("DROP TABLE iF EXISTS " + DBHelper.TABLE_NAME);

            onCreate(db);

        }
    }
}
