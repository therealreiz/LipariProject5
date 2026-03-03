package com.lipari.bank.cli;

import com.lipari.bank.model.Account;
import com.lipari.bank.model.Customer;
import com.lipari.bank.persistence.DatabaseManager;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entry point del progetto LipariBank — Day 5.
 *
 * <p>Dimostra i problemi Git del progetto:
 * <ol>
 *   <li>.gitignore inefficace per file già tracciati  — BUG #1</li>
 *   <li>Conflict markers non risolti                  — BUG #2</li>
 *   <li>Percorso assoluto nel DatabaseManager         — BUG #3</li>
 * </ol>
 */
public class BankConsole {

    private final List<Customer> customers = new ArrayList<>();
    private final List<Account>  accounts  = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        new BankConsole().run();
    }

    private void run() throws Exception {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  LipariBank — Day 5  |  Git Bugs         ║");
        System.out.println("╚══════════════════════════════════════════╝");
        DatabaseManager.initializeSchema();
        insertSampleData();
        loadData();
        printReport();
    }

    // ── Dati di esempio ───────────────────────────────────────────────────────
    private void insertSampleData() throws SQLException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO customers (fiscal_code, first_name, last_name) VALUES (?,?,?)")) {
                insertCustomer(ps, "RSSMRA85M01H501Z", "Mario",   "Rossi");
                insertCustomer(ps, "BNCNNA90L50C351X", "Anna",    "Bianchi");
                insertCustomer(ps, "VRDBRT75L10C351Y", "Roberto", "Verdi");
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO accounts (iban, balance, customer_id) VALUES (?,?,?)")) {
                insertAccount(ps, "IT60A0542811101000000001001", new BigDecimal("5000.00"), 1);
                insertAccount(ps, "IT60A0542811101000000002002", new BigDecimal("3000.00"), 1);
                insertAccount(ps, "IT60A0542811101000000003003", new BigDecimal("10000.00"), 2);
                insertAccount(ps, "IT60A0542811101000000004004", new BigDecimal("1500.00"),  3);
            }
        }
    }

    private static void insertCustomer(PreparedStatement ps,
            String cf, String fn, String ln) throws SQLException {
        ps.setString(1, cf); ps.setString(2, fn); ps.setString(3, ln);
        ps.executeUpdate();
    }

    private static void insertAccount(PreparedStatement ps,
            String iban, BigDecimal bal, long custId) throws SQLException {
        ps.setString(1, iban); ps.setBigDecimal(2, bal); ps.setLong(3, custId);
        ps.executeUpdate();
    }

    // ── Caricamento dati dal DB ───────────────────────────────────────────────
    private void loadData() throws SQLException {
        try (Connection conn = DatabaseManager.getConnection();
             Statement  stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(
                    "SELECT id, fiscal_code, first_name, last_name FROM customers ORDER BY last_name")) {
                while (rs.next()) {
                    customers.add(new Customer(
                            rs.getLong("id"),
                            rs.getString("fiscal_code"),
                            rs.getString("first_name"),
                            rs.getString("last_name")));
                }
            }
            try (ResultSet rs = stmt.executeQuery(
                    "SELECT id, iban, balance, customer_id FROM accounts ORDER BY customer_id")) {
                while (rs.next()) {
                    accounts.add(new Account(
                            rs.getLong("id"),
                            rs.getString("iban"),
                            rs.getBigDecimal("balance"),
                            rs.getLong("customer_id")));
                }
            }
        }
    }

    // ── Report ────────────────────────────────────────────────────────────────
    /**
     * Stampa il riepilogo dei clienti.
     *
     * <p>Versione semplice — una riga per cliente con numero di conti.
     */
    public void printReport() {
        System.out.println("\n── Report Clienti ───────────────────────────────────");
        System.out.printf("%-20s %-25s %10s%n", "Cod. Fiscale", "Nome", "N. Conti");
        System.out.println("─".repeat(57));
        for (Customer c : customers) {
            long nConti = accounts.stream()
                    .filter(a -> a.getCustomerId() == c.getId())
                    .count();
            System.out.printf("%-20s %-25s %10d%n",
                    c.getFiscalCode(), c.getFullName(), nConti);
        }
        System.out.println("─".repeat(57));
        System.out.printf("  Totale clienti: %d  |  Totale conti: %d%n",
                customers.size(), accounts.size());
    }
}
