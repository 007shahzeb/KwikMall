package mall.kwik.kwikmall.sqlitedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by dharamveer on 27/11/17.
 */

public class DBHelper extends SQLiteOpenHelper{



    private static final String DATABASE_NAME = "kwiqmallD21B1.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE1 = "productsList";
    public static final String TABLE2 = "nooforders";
    public static final String TABLE3 = "savedAddress";
    public static final String TABLE4 = "newAddress";
    public static final String TABLE5 = "cartcounter";
    public static final String TABLE6 = "saveOrders";

    // Labels Table Columns names TABLE1
    public static final String ID1 = "id1";
    public static final String PROCUCT_ID = "product_id";
    public static final String USER_ID = "user_id";
    public static final String KEY_name = "nameOfFood";
    public static final String KEY_price = "price";
    public static final String KEY_qty = "totalItems";
    public static final String KEY_ImageUri = "imageUri";
    public static final String KEY_TotapPrice = "totalPrice";


    // Labels Table Columns names TABLE6
    public static final String ID6 = "id6";
    public static final String PROCUCT_ID_ORDER = "product_id_orders";
    public static final String USER_ID_ORDERS = "user_id_orders";
    public static final String KEY_name_ORDERS = "nameOfFood_orders";
    public static final String KEY_price_ORDERS = "price_orders";
    public static final String KEY_qty_ORDERS = "totalItems_orders";
    public static final String KEY_ImageUri_ORDERS = "imageUri_orders";
    public static final String KEY_TotapPrice_ORDERS = "totalPrice_orders";


    // Labels Table Columns names TABLE2
    public static final String ID = "id";
    public static final String SUM_TOTAL = "sumTotal";

    // Labels Table Columns names TABLE3
    public static final String ID3 = "id3";
    public static final String PER_NAME = "name";
    public static final String CNTRY_CODE = "countycode";
    public static final String CONTACT_NO = "contactno";
    public static final String EMAIL_ADD = "emailAdd";
    public static final String ADDR = "addre";
    public static final String CITY_NAME = "edCity";
    public static final String ZIP_CODE = "edZipCode";


    // Labels Table Columns names TABLE4
    public static final String ID4 = "id4";
    public static final String MAPADDRESS = "mapaddress";
    public static final String FLATNO = "flatno";
    public static final String LANDMARK = "landmark";
    public static final String SAVEASTAG = "saveastag";


    // Labels Table Columns names TABLE5
    public static final String ID5 = "id5";
    public static final String COUNTER = "counter";




    private SQLiteDatabase db;


    public DBHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    private static final String CREATE_TABLE5 = "CREATE TABLE " +TABLE5+"("+
            ID5+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
            +COUNTER+ " INTEGER);";

    private static final String CREATE_TABLE1 = "CREATE TABLE "+TABLE1+"("+ID1+ " integer primary key autoincrement,"
            +PROCUCT_ID+ " INTEGER, "
            +USER_ID+ " INTEGER, "
            +KEY_name+ " text not null UNIQUE, "
            +KEY_price+ " Text, "
            +KEY_qty+ " Text, "
            +KEY_ImageUri+ " Text, "
            +KEY_TotapPrice+ " Text);";


    private static final String CREATE_TABLE6 = "CREATE TABLE "+TABLE6+"("+ID6+ " integer primary key autoincrement,"
            +PROCUCT_ID_ORDER+ " INTEGER, "
            +USER_ID_ORDERS+ " INTEGER, "
            +KEY_name_ORDERS+ " text not null UNIQUE, "
            +KEY_price_ORDERS+ " Text, "
            +KEY_qty_ORDERS+ " Text, "
            +KEY_ImageUri_ORDERS+ " Text, "
            +KEY_TotapPrice_ORDERS+ " Text);";



    private static final String CREATE_TABLE2 = "CREATE TABLE " +TABLE2+"("+ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
            +SUM_TOTAL+ " INTEGER);";

    private static final String CREATE_TABLE3 = "CREATE TABLE "+TABLE3+"("+ID3+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
            +PER_NAME+ " Text, "
            +CONTACT_NO+ " Text, "
            +EMAIL_ADD+ " Text, "
            +ADDR+ " Text);";


    private static final String CREATE_TABLE4 = "CREATE TABLE "+TABLE4+"("+ID4+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
            +MAPADDRESS+ " Text, "
            +FLATNO+ " Text,"
            +LANDMARK+ " Text,"
            +SAVEASTAG+ " Text);";



    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE1);
        db.execSQL(CREATE_TABLE2);
        db.execSQL(CREATE_TABLE3);
        db.execSQL(CREATE_TABLE4);
        db.execSQL(CREATE_TABLE5);
        db.execSQL(CREATE_TABLE6);

    }



    public boolean deleteSingleRowTABLE1(String foodName){

        SQLiteDatabase db= this.getWritableDatabase();

        db.delete(TABLE1, KEY_name + " = ?", new String[] {foodName});

        return true;

    }


    public boolean deleteSingleRowTABLE6(String food){

        SQLiteDatabase db= this.getWritableDatabase();

        db.delete(TABLE6, KEY_name_ORDERS + " = ?", new String[] {food});

        return true;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed, all data will be gone!!!

        db.execSQL("DROP TABLE IF EXISTS productsList");
        db.execSQL("DROP TABLE IF EXISTS nooforders");
        db.execSQL("DROP TABLE IF EXISTS savedAddress");
        db.execSQL("DROP TABLE IF EXISTS newAddress");
        db.execSQL("DROP TABLE IF EXISTS saveOrders");

        onCreate(db);

    }


    public void addNewAddress(String mapLoa,String flatno,String landmark,String savetag){

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MAPADDRESS,mapLoa);
        values.put(FLATNO,flatno);
        values.put(LANDMARK,landmark);
        values.put(SAVEASTAG,savetag);


        String sqlQuery = "SELECT * FROM " + TABLE4 + " WHERE " + FLATNO + " = " + "\""
                + flatno + "\"";

        Cursor c = db.rawQuery(sqlQuery, null);
        if (c != null && c.getCount() != 0) {

        } else {
            db.insert(TABLE4,null,values);
        }

    }





    public boolean updateCounter(Integer id,int counter) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COUNTER, counter);
        db.update(TABLE5, contentValues, ID5 + " = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Cursor getCounter(int id) {
        SQLiteDatabase db = this.getReadableDatabase();



        Cursor cursor = db.query(TABLE5,
                new String[] { ID5,COUNTER}, id + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        return cursor;
    }


    public Integer deleteCounter(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE5,
                ID5 + " = ? ",
                new String[] { Integer.toString(id) });
    }

/*

    public void insertAddress(String name,String countryCode,String contact,String email,String addr,String cityName,String Zip){

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PER_NAME,name);
        values.put(CNTRY_CODE, countryCode);
        values.put(CONTACT_NO, contact);
        values.put(EMAIL_ADD, email);
        values.put(ADDR, addr);
        values.put(CITY_NAME, cityName);
        values.put(ZIP_CODE, Zip);

        String sqlQuery = "SELECT * FROM " + TABLE3 + " WHERE " + CONTACT_NO + " = " + "\""
                + contact + "\"";

        Cursor c = db.rawQuery(sqlQuery, null);
        if (c != null && c.getCount() != 0) {

        } else {
            db.insert(TABLE3,null,values);
        }

    }*/

    public void insertAddress(String name,String contact,String email,String addr){

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PER_NAME,name);
        values.put(CONTACT_NO, contact);
        values.put(EMAIL_ADD, email);
        values.put(ADDR, addr);

        String sqlQuery = "SELECT * FROM " + TABLE3 + " WHERE " + CONTACT_NO + " = " + "\""
                + contact + "\"";

        Cursor c = db.rawQuery(sqlQuery, null);
        if (c != null && c.getCount() != 0) {

        } else {
            db.insert(TABLE3,null,values);
        }

    }





    public void insert(int userid, int product_id,String itemname,String priceItem,String totalitems,String imageURI,String total_Price ) {

        //Open connection to write data
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PROCUCT_ID, product_id);
        values.put(USER_ID, userid);
        values.put(KEY_name,itemname);
        values.put(KEY_price, priceItem);
        values.put(KEY_qty, totalitems);
        values.put(KEY_ImageUri, imageURI);
        values.put(KEY_TotapPrice, total_Price);


        String sqlQuery = "SELECT * FROM " + TABLE1 + " WHERE " + PROCUCT_ID + " = " + "\""
                + product_id + "\"";

        Cursor c = db.rawQuery(sqlQuery, null);
        if (c != null && c.getCount() != 0) {

        } else {
            db.insert(TABLE1,null,values);
        }

    }

    public void saveOrders(int userid, int product_id,String itemname,String priceItem,String totalitems,String imageURI,String total_Price ) {

        //Open connection to write data
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PROCUCT_ID_ORDER, product_id);
        values.put(USER_ID_ORDERS, userid);
        values.put(KEY_name_ORDERS,itemname);
        values.put(KEY_price_ORDERS, priceItem);
        values.put(KEY_qty_ORDERS, totalitems);
        values.put(KEY_ImageUri_ORDERS, imageURI);
        values.put(KEY_TotapPrice_ORDERS, total_Price);


        String sqlQuery = "SELECT * FROM " + TABLE6 + " WHERE " + PROCUCT_ID_ORDER + " = " + "\""
                + product_id + "\"";

        Cursor c = db.rawQuery(sqlQuery, null);
        if (c != null && c.getCount() != 0) {

        } else {
            db.insert(TABLE6,null,values);
        }

    }


    public List<SaveOrdersModel> getSaveOrders() {

        List<SaveOrdersModel> saveOrdersModelArrayList = new ArrayList<SaveOrdersModel>();

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from "+TABLE1+" ;",null);
        StringBuffer stringBuffer = new StringBuffer();


        SaveOrdersModel saveOrdersModel = null;

        while (cursor.moveToNext())
        {

            saveOrdersModel = new SaveOrdersModel();

            int product_id = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));
            int userid = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
            String nameOfFood = cursor.getString(cursor.getColumnIndexOrThrow("nameOfFood"));
            String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
            String totalItems = cursor.getString(cursor.getColumnIndexOrThrow("totalItems"));
            String imageUri = cursor.getString(cursor.getColumnIndexOrThrow("imageUri"));
            String totalPrice = cursor.getString(cursor.getColumnIndexOrThrow("totalPrice"));


            saveOrdersModel.setUser_id_ORDERS(userid);
            saveOrdersModel.setKEY_ID_ORDERS(product_id);
            saveOrdersModel.setKEY_name_ORDERS(nameOfFood);
            saveOrdersModel.setKEY_price_ORDERS(price);
            saveOrdersModel.setKEY_TotalPrice_ORDERS(totalPrice);
            saveOrdersModel.setKEY_qty_ORDERS(totalItems);
            saveOrdersModel.setKEY_ImageUri_ORDERS(imageUri);
            stringBuffer.append(saveOrdersModel);

            saveOrdersModelArrayList.add(saveOrdersModel);
        }


        for (SaveOrdersModel mo:saveOrdersModelArrayList ) {

            Log.i("Hellomo",""+mo.getUser_id_ORDERS());
        }

        return saveOrdersModelArrayList;

    }





    public List<CartModel> getStoreProducts() {

        List<CartModel> cartModelArrayList1 = new ArrayList<CartModel>();

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from "+TABLE1+" ;",null);
        StringBuffer stringBuffer = new StringBuffer();


        CartModel cartModel = null;

        while (cursor.moveToNext())
        {

            cartModel = new CartModel();

            int product_id = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));
            int userid = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
            String nameOfFood = cursor.getString(cursor.getColumnIndexOrThrow("nameOfFood"));
            String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
            String totalItems = cursor.getString(cursor.getColumnIndexOrThrow("totalItems"));
            String imageUri = cursor.getString(cursor.getColumnIndexOrThrow("imageUri"));
            String totalPrice = cursor.getString(cursor.getColumnIndexOrThrow("totalPrice"));


            cartModel.setUser_id(userid);
            cartModel.setKEY_ID(product_id);
            cartModel.setKEY_name(nameOfFood);
            cartModel.setKEY_price(price);
            cartModel.setKEY_TotalPrice(totalPrice);
            cartModel.setKEY_qty(totalItems);
            cartModel.setKEY_ImageUri(imageUri);
            stringBuffer.append(cartModel);

            cartModelArrayList1.add(cartModel);
        }


        for (CartModel mo:cartModelArrayList1 ) {

            Log.i("Hellomo",""+mo.getUser_id());
        }

        return cartModelArrayList1;

    }


    public int getProductsCount() {

        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE1;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);



        if(cursor != null && !cursor.isClosed()){
            count = cursor.getCount();
            cursor.close();
        }
        return count;

    }



    public NewAddressModel getNewAddress(){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from "+TABLE4+" ;",null);
        StringBuffer stringBuffer = new StringBuffer();

        NewAddressModel newAddressModel = null;

        while (cursor.moveToNext())
        {

            newAddressModel = new NewAddressModel();


            String newAddress = cursor.getString(cursor.getColumnIndexOrThrow("mapaddress"));
            String flatno = cursor.getString(cursor.getColumnIndexOrThrow("flatno"));


            newAddressModel.setNewAddress(newAddress);
            newAddressModel.setFlatno(flatno);


        }

        return newAddressModel;
    }

    public AddressesModel getAddress(){


        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from "+TABLE3+" ;",null);
        StringBuffer stringBuffer = new StringBuffer();

        AddressesModel addressesModel = null;

        while (cursor.moveToNext())
        {

            addressesModel = new AddressesModel();

            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
           // String countycode = cursor.getString(cursor.getColumnIndexOrThrow("countycode"));
            String contactno = cursor.getString(cursor.getColumnIndexOrThrow("contactno"));
            String emailAdd = cursor.getString(cursor.getColumnIndexOrThrow("emailAdd"));
            String addre = cursor.getString(cursor.getColumnIndexOrThrow("addre"));
          //  String cityname = cursor.getString(cursor.getColumnIndexOrThrow("edCity"));
          //  String zipcode = cursor.getString(cursor.getColumnIndexOrThrow("edZipCode"));


            addressesModel.setName(name);
            //addressesModel.setCountycode(countycode);
            addressesModel.setContactno(contactno);
            addressesModel.setEmailAdd(emailAdd);
            addressesModel.setAddre(addre);
         //   addressesModel.setCityname(cityname);
          //  addressesModel.setZipcode(zipcode);

        }

        return addressesModel;

    }


    public boolean updateAddress(String name,String countryCode,String contact,String email,String addr,String cityName,String Zip){

        ContentValues values = new ContentValues();
        values.put(PER_NAME,name);
        values.put(CNTRY_CODE, countryCode);
        values.put(CONTACT_NO, contact);
        values.put(EMAIL_ADD, email);
        values.put(ADDR, addr);
        values.put(CITY_NAME, cityName);
        values.put(ZIP_CODE, Zip);

        int i =  db.update(TABLE3, values, CONTACT_NO + "=" + contact, null);
        return i > 0;

    }


    public void deleteAllOrders(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM productsList"); //delete all rows in a table
        db.close();

    }







    public void deleteAllAddresses(){



        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM savedAddress"); //delete all rows in a table
        db.close();

    }

    public void deleteNewAddress(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM newAddress"); //delete all rows in a table
        db.close();

    }


}
