package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLiteDBhelper.ACCOUNT_NO;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLiteDBhelper.AMOUNT;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLiteDBhelper.DATE;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLiteDBhelper.TABLE_TRANSACTION;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLiteDBhelper.TYPE_OF_EXPENSE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class TR implements TransactionDAO {
    private final SQLiteDBhelper helping;
    private SQLiteDatabase database;

    public TR(Context context) {
        helping = new SQLiteDBhelper(context);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        database = helping.getWritableDatabase();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        ContentValues values = new ContentValues();
        values.put(DATE, df.format(date));
        values.put(ACCOUNT_NO, accountNo);
        values.put(TYPE_OF_EXPENSE, String.valueOf(expenseType));
        values.put(AMOUNT, amount);


        database.insert(TABLE_TRANSACTION, null, values);
        database.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() throws ParseException {
        List<Transaction> transactions = new ArrayList<Transaction>();

        database = helping.getReadableDatabase();

        String[] projection = {
                DATE,
                ACCOUNT_NO,
                TYPE_OF_EXPENSE,
                AMOUNT
        };

        Cursor cursor = database.query(
                TABLE_TRANSACTION,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndex(DATE));
            Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(date);
            String accountNumber = cursor.getString(cursor.getColumnIndex(ACCOUNT_NO));
            String type = cursor.getString(cursor.getColumnIndex(TYPE_OF_EXPENSE));
            ExpenseType expenseType = ExpenseType.valueOf(type);
            double amount = cursor.getDouble(cursor.getColumnIndex(AMOUNT));
            Transaction transaction = new Transaction(date1,accountNumber,expenseType,amount);

            transactions.add(transaction);
        }
        cursor.close();
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) throws ParseException {

        List<Transaction> transactions = new ArrayList<Transaction>();

        database = helping.getReadableDatabase();

        String[] projection = {
                DATE,
                ACCOUNT_NO,
                TYPE_OF_EXPENSE,
                AMOUNT
        };

        Cursor cursor = database.query(
                TABLE_TRANSACTION,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        int size = cursor.getCount();

        while(cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndex(DATE));
            Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(date);
            String accountNumber = cursor.getString(cursor.getColumnIndex(ACCOUNT_NO));
            String type = cursor.getString(cursor.getColumnIndex(TYPE_OF_EXPENSE));
            ExpenseType expenseType = ExpenseType.valueOf(type);
            double amount = cursor.getDouble(cursor.getColumnIndex(AMOUNT));
            Transaction transaction = new Transaction(date1,accountNumber,expenseType,amount);

            transactions.add(transaction);
        }

        if (size <= limit) {
            return transactions;
        }

        return transactions.subList(size - limit, size);


    }

}
