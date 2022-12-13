package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLiteDBhelper.ACCOUNT_NO;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLiteDBhelper.BALANCE;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLiteDBhelper.NAME_OF_BANK;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLiteDBhelper.NAME_OF_HOLDER;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLiteDBhelper.TABLE_ACCOUNT;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class AC implements AccountDAO {
    private final SQLiteDBhelper helping;
    private SQLiteDatabase database;


    public AC(Context context) {
        helping = new SQLiteDBhelper(context);
    }

    @Override
    public List<String> getAccountNumbersList() {
        database = helping.getReadableDatabase();

        String[] projection = {
                ACCOUNT_NO
        };


        Cursor cursor = database.query(
                TABLE_ACCOUNT,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        List<String> accountNumbers = new ArrayList<String>();

        while(cursor.moveToNext()) {
            String accountNumber = cursor.getString(
                    cursor.getColumnIndexOrThrow(ACCOUNT_NO));
            accountNumbers.add(accountNumber);
        }
        cursor.close();
        return accountNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accounts = new ArrayList<Account>();

        database = helping.getReadableDatabase();

        String[] projection = {
                ACCOUNT_NO,
                NAME_OF_BANK,
                NAME_OF_HOLDER,
                BALANCE
        };

        Cursor cursor = database.query(
                TABLE_ACCOUNT,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()) {
            String accountNumber = cursor.getString(cursor.getColumnIndex(ACCOUNT_NO));
            String bankName = cursor.getString(cursor.getColumnIndex(NAME_OF_BANK));
            String accountHolderName = cursor.getString(cursor.getColumnIndex(NAME_OF_HOLDER));
            double balance = cursor.getDouble(cursor.getColumnIndex(BALANCE));
            Account account = new Account(accountNumber,bankName,accountHolderName,balance);

            accounts.add(account);
        }
        cursor.close();
        return accounts;

    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {

        database = helping.getReadableDatabase();
        String[] projection = {
                ACCOUNT_NO,
                NAME_OF_BANK,
                NAME_OF_HOLDER,
                BALANCE
        };

        String selection = ACCOUNT_NO + " = ?";
        String[] selectionArgs = { accountNo };

        Cursor c = database.query(
                TABLE_ACCOUNT,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (c == null){
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        else {
            c.moveToFirst();

            Account account = new Account(accountNo, c.getString(c.getColumnIndex(NAME_OF_BANK)),
                    c.getString(c.getColumnIndex(NAME_OF_HOLDER)), c.getDouble(c.getColumnIndex(BALANCE)));
            return account;
        }
    }

    @Override
    public void addAccount(Account account) {

        database = helping.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ACCOUNT_NO, account.getAccountNo());
        values.put(NAME_OF_BANK, account.getBankName());
        values.put(NAME_OF_HOLDER, account.getAccountHolderName());
        values.put(BALANCE,account.getBalance());


        database.insert(TABLE_ACCOUNT, null, values);
        database.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        database = helping.getWritableDatabase();
        database.delete(TABLE_ACCOUNT, ACCOUNT_NO + " = ?",
                new String[] { accountNo });
        database.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        database = helping.getWritableDatabase();
        String[] projection = {
                BALANCE
        };

        String selection = ACCOUNT_NO + " = ?";
        String[] selectionArgs = { accountNo };

        Cursor cursor = database.query(
                TABLE_ACCOUNT,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        double balance;
        if(cursor.moveToFirst())
            balance = cursor.getDouble(0);
        else{
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }

        ContentValues values = new ContentValues();
        switch (expenseType) {
            case EXPENSE:
                values.put(BALANCE, balance - amount);
                break;
            case INCOME:
                values.put(BALANCE, balance + amount);
                break;
        }


        database.update(TABLE_ACCOUNT, values, ACCOUNT_NO + " = ?",
                new String[] { accountNo });

        cursor.close();
        database.close();

    }
}
