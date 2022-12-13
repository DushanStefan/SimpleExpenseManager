package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.AC;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.TR;

public class PersistentExpenseManager extends ExpenseManager {

    public PersistentExpenseManager(Context context) {
        super(context);
        setup(context);
    }

    @Override
    public void setup(Context context) {
        /*** Begin generating dummy data for In-Memory implementation ***/

        TransactionDAO persistentTransactionDAO = new TR(context);
        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO persistentAccountDAO = new AC(context);
        setAccountsDAO(persistentAccountDAO);



        /*** End ***/
    }
}
