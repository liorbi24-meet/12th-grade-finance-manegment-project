package com.example.financemanagment;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvTotalBalance;
    private TextView tvIncome;
    private TextView tvExpenses;
    private RecyclerView recyclerViewTransactions;
    private TransactionAdapter transactionAdapter;
    private List<Transaction> transactionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvTotalBalance = findViewById(R.id.tvTotalBalance);
        tvIncome = findViewById(R.id.tvIncome);
        tvExpenses = findViewById(R.id.tvExpenses);
        recyclerViewTransactions = findViewById(R.id.recyclerViewTransactions);

        // Sample data for income and expenses
        double income = 2000.00;
        double expenses = 1200.00;
        double totalBalance = income - expenses;

        // Set values for balance, income, and expenses
        tvTotalBalance.setText("Total Balance: $" + String.format("%.2f", totalBalance));
        tvIncome.setText("Income: $" + String.format("%.2f", income));
        tvExpenses.setText("Expenses: $" + String.format("%.2f", expenses));

        // Initialize transaction list and adapter
        transactionList = new ArrayList<>();
        transactionList.add(new Transaction("Groceries", -50.00, "2024-10-20"));
        transactionList.add(new Transaction("Salary", 2000.00, "2024-10-15"));
        transactionList.add(new Transaction("Coffee", -5.00, "2024-10-10"));

        transactionAdapter = new TransactionAdapter(transactionList);
        recyclerViewTransactions.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTransactions.setAdapter(transactionAdapter);
    }
}
