package com.example.test6;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String USERS_Table = "Users";
    public static final String Uer_Id = "Id";
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String ROLE = "ROLE";

    public DBHelper(@Nullable Context context) {
        super(context, "DB_BMP601.db", null, 2);;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CreateUserTable = "CREATE TABLE " + USERS_Table + " (" + Uer_Id + " INTEGER PRIMARY KEY AUTOINCREMENT," + USERNAME + " TEXT," + PASSWORD + " TEXT," + ROLE + " TEXT )";
        String CreateDrug_CompaniesTable = "CREATE TABLE Drug_Companies (" +
                "company_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "company_name TEXT NOT NULL," +
                "contact_info TEXT)";
        String CreateCabinetsTable = "CREATE TABLE Cabinets (" +
                "cabinet_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "cabinet_name TEXT NOT NULL)";
        String CreateShelvesTable = "CREATE TABLE Shelves (" +
                "shelf_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "cabinet_id INTEGER NOT NULL," +
                "shelf_number TEXT NOT NULL," +
                "FOREIGN KEY (cabinet_id) REFERENCES Cabinets (cabinet_id))";
        String CreateDrugsTable = "CREATE TABLE Drugs (" +
                "drug_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "drug_name TEXT NOT NULL," +
                "description TEXT," +
                "quantity INTEGER NOT NULL," +
                "company_id INTEGER NOT NULL," +
                "cabinet_id INTEGER NOT NULL," +
                "shelf_id INTEGER NOT NULL," +
                "FOREIGN KEY (company_id) REFERENCES Drug_Companies (company_id)," +
                "FOREIGN KEY (cabinet_id) REFERENCES Cabinets (cabinet_id)," +
                "FOREIGN KEY (shelf_id) REFERENCES Shelves (shelf_id))";
        db.execSQL(CreateUserTable);
        db.execSQL(CreateDrug_CompaniesTable);
        db.execSQL(CreateCabinetsTable);
        db.execSQL(CreateShelvesTable);
        db.execSQL(CreateDrugsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_Table);
        db.execSQL("DROP TABLE IF EXISTS Drug_Companies");
        db.execSQL("DROP TABLE IF EXISTS Cabinets");
        db.execSQL("DROP TABLE IF EXISTS Shelves");
        db.execSQL("DROP TABLE IF EXISTS Drugs");
        onCreate(db);
    }

    public boolean adduser(userModel user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USERNAME, user.getUsername());
        cv.put(PASSWORD, user.getPassword());
        cv.put(ROLE, user.getRole());

        long insert = db.insert(USERS_Table, null, cv);
        return insert != -1;
    }

    public List<userModel> getAll() {
        List<userModel> listofuser = new ArrayList<>();
        String selectAll = "SELECT * From " + USERS_Table;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectAll, null);
        if (cursor.moveToFirst()) {
            do {
                int userId = cursor.getInt(0);
                String username = cursor.getString(1);
                String password = cursor.getString(2);
                String Role = cursor.getString(3);
                userModel user = new userModel(userId, username, password, Role);
                listofuser.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listofuser;
    }

    public boolean deleteUser(userModel user) {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + USERS_Table + " WHERE " + Uer_Id + " = " + user.getId();
        Cursor cursor = db.rawQuery(delete, null);
        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean Loginemp(String username, String password) {
        SQLiteDatabase loginuser = this.getWritableDatabase();
        Cursor cursor = loginuser.rawQuery("SELECT * FROM Users WHERE USERNAME = ? AND PASSWORD = ? AND ROLE =? ", new String[]{username, password, "worker"});
        return cursor.getCount() > 0;
    }

    public boolean LoginPharmacist(String username, String password) {
        SQLiteDatabase loginuser = this.getWritableDatabase();
        Cursor cursor = loginuser.rawQuery("SELECT * FROM Users WHERE USERNAME = ? AND PASSWORD = ? AND ROLE =? ", new String[]{username, password, "Pharmacist"});
        return cursor.getCount() > 0;
    }


    public int addOrGetCompanyId(String companyName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT company_id FROM Drug_Companies WHERE company_name = ?", new String[]{companyName});
            if (cursor.moveToFirst()) {
                int companyId = cursor.getInt(0);
                cursor.close();
                return companyId;
            } else {
                cursor.close();
                ContentValues cv = new ContentValues();
                cv.put("company_name", companyName);
                long result = db.insert("Drug_Companies", null, cv);
                return (int) result;
            }
        } catch (Exception e) {
            Log.e("DBHelper", "Error adding or getting company ID", e);
            return -1;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    public int addOrGetCabinetId(String cabinetName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT cabinet_id FROM Cabinets WHERE cabinet_name = ?", new String[]{cabinetName});
            if (cursor.moveToFirst()) {
                int cabinetId = cursor.getInt(0);
                cursor.close();
                return cabinetId;
            } else {
                cursor.close();
                ContentValues cv = new ContentValues();
                cv.put("cabinet_name", cabinetName);
                long result = db.insert("Cabinets", null, cv);
                return (int) result;
            }
        } catch (Exception e) {
            Log.e("DBHelper", "Error adding or getting cabinet ID", e);
            return -1;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    public int addOrGetShelfId(int cabinetId, String shelfNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT shelf_id FROM Shelves WHERE cabinet_id = ? AND shelf_number = ?", new String[]{String.valueOf(cabinetId), shelfNumber});
            if (cursor.moveToFirst()) {
                int shelfId = cursor.getInt(0);
                cursor.close();
                return shelfId;
            } else {
                cursor.close();
                ContentValues cv = new ContentValues();
                cv.put("cabinet_id", cabinetId);
                cv.put("shelf_number", shelfNumber);
                long result = db.insert("Shelves", null, cv);
                return (int) result;
            }
        } catch (Exception e) {
            Log.e("DBHelper", "Error adding or getting shelf ID", e);
            return -1;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }



    public List<String> getAllMedicines() {
        List<String> medicines = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT d.drug_id, d.drug_name, d.description, c.company_name, d.quantity, b.cabinet_name, s.shelf_number " +
                "FROM Drugs d " +
                "JOIN Drug_Companies c ON d.company_id = c.company_id " +
                "JOIN Cabinets b ON d.cabinet_id = b.cabinet_id " +
                "JOIN Shelves s ON d.shelf_id = s.shelf_id";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    int drugId = cursor.getInt(0);
                    String drugName = cursor.getString(1);
                    String description = cursor.getString(2);
                    String companyName = cursor.getString(3);
                    int quantity = cursor.getInt(4);
                    String cabinetName = cursor.getString(5);
                    String shelfNumber = cursor.getString(6);
                    String medicineInfo = "Drug ID: " + drugId + "\nDrug: " + drugName + "\nDescription: " + description + "\nCompany: " + companyName + "\nQuantity: " + quantity + "\nCabinet: " + cabinetName + "\nShelf: " + shelfNumber;
                    medicines.add(medicineInfo);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DBHelper", "Error retrieving all medicines", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return medicines;
    }

    public List<String> searchMedicines(String searchText, String cabinet, String shelf) {
        List<String> medicines = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder query = new StringBuilder("SELECT d.drug_id, d.drug_name, d.description, c.company_name, d.quantity, b.cabinet_name, s.shelf_number " +
                "FROM Drugs d " +
                "JOIN Drug_Companies c ON d.company_id = c.company_id " +
                "JOIN Cabinets b ON d.cabinet_id = b.cabinet_id " +
                "JOIN Shelves s ON d.shelf_id = s.shelf_id " +
                "WHERE 1=1 ");
        List<String> args = new ArrayList<>();
        if (!searchText.isEmpty()) {
            query.append("AND (d.drug_name LIKE ? OR c.company_name LIKE ?) ");
            args.add("%" + searchText + "%");
            args.add("%" + searchText + "%");
        }
        if (!cabinet.equals("All")) {
            query.append("AND b.cabinet_name = ? ");
            args.add(cabinet);
        }
        if (!shelf.equals("All")) {
            query.append("AND s.shelf_number = ? ");
            args.add(shelf);
        }
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query.toString(), args.toArray(new String[0]));
            if (cursor.moveToFirst()) {
                do {
                    int drugId = cursor.getInt(0);
                    String drugName = cursor.getString(1);
                    String description = cursor.getString(2);
                    String companyName = cursor.getString(3);
                    int quantity = cursor.getInt(4);
                    String cabinetName = cursor.getString(5);
                    String shelfNumber = cursor.getString(6);
                    String medicineInfo = "Drug ID: " + drugId + "\nDrug: " + drugName + "\nDescription: " + description + "\nCompany: " + companyName + "\nQuantity: " + quantity + "\nCabinet: " + cabinetName + "\nShelf: " + shelfNumber;
                    medicines.add(medicineInfo);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DBHelper", "Error searching medicines", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return medicines;
    }

    public List<String> getAllCabinets() {
        List<String> cabinets = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT cabinet_name FROM Cabinets";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String cabinetName = cursor.getString(0);
                cabinets.add(cabinetName);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cabinets;
    }

    public List<String> getShelvesByCabinet(String cabinetName) {
        List<String> shelves = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT s.shelf_number " +
                "FROM Shelves s " +
                "JOIN Cabinets c ON s.cabinet_id = c.cabinet_id " +
                "WHERE c.cabinet_name = ?";
        Cursor cursor = db.rawQuery(query, new String[]{cabinetName});
        if (cursor.moveToFirst()) {
            do {
                String shelfNumber = cursor.getString(0);
                shelves.add(shelfNumber);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return shelves;
    }

    public String getMedicineById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT d.drug_name, c.company_name, d.quantity, b.cabinet_name, s.shelf_number " +
                "FROM Drugs d " +
                "JOIN Drug_Companies c ON d.company_id = c.company_id " +
                "JOIN Cabinets b ON d.cabinet_id = b.cabinet_id " +
                "JOIN Shelves s ON d.shelf_id = s.shelf_id " +
                "WHERE d.drug_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            String drugName = cursor.getString(0);
            String companyName = cursor.getString(1);
            int quantity = cursor.getInt(2);
            String cabinetName = cursor.getString(3);
            String shelfNumber = cursor.getString(4);
            String medicineInfo = "Drug: " + drugName + "\nCompany: " + companyName + "\nQuantity: " + quantity + "\nCabinet: " + cabinetName + "\nShelf: " + shelfNumber;
            cursor.close();
            db.close();
            return medicineInfo;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }



}
