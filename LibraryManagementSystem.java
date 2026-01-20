import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

// ------------------- Main Class -------------------
public class LibraryManagementSystem {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}

// ------------------- Data Classes -------------------
class Book {
    int id;
    String title;
    String author;
    int copies; // Number of available copies

    public Book(int id, String title, String author, int copies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.copies = copies;
    }
}

class User {
    String name;
    String rollNo;
    String college;
    String year;
    String branch;

    public User(String name, String rollNo, String college, String year, String branch) {
        this.name = name;
        this.rollNo = rollNo;
        this.college = college;
        this.year = year;
        this.branch = branch;
    }
}

class Admin {
    ArrayList<Book> books = new ArrayList<>();
    ArrayList<User> users = new ArrayList<>();

    public void addBook(int id, String title, String author, int copies) {
        books.add(new Book(id, title, author, copies));
    }

    public void deleteBook(int id) {
        books.removeIf(book -> book.id == id);
    }

    public void addUser(User user) {
        users.add(user);
    }
}

class UserModule {
    ArrayList<Book> books;

    public UserModule(ArrayList<Book> books) {
        this.books = books;
    }

    public boolean issueBook(int id) {
        for (Book book : books) {
            if (book.id == id) {
                if (book.copies > 0) {
                    book.copies--;
                    return true;
                } else return false;
            }
        }
        return false;
    }

    public boolean returnBook(int id) {
        for (Book book : books) {
            if (book.id == id) {
                book.copies++;
                return true;
            }
        }
        return false;
    }
}

// ------------------- GUI Classes -------------------
class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("Library Management System - Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        JButton adminBtn = new JButton("Admin Login");
        JButton userBtn = new JButton("User Login");

        panel.add(new JLabel("Choose Login Type:", SwingConstants.CENTER));
        panel.add(adminBtn);
        panel.add(userBtn);

        add(panel);

        Admin admin = new Admin();

        // Preload Programming Books
        admin.addBook(101, "C Programming", "Dennis Ritchie", 5);
        admin.addBook(102, "C++ Programming", "Bjarne Stroustrup", 5);
        admin.addBook(103, "Java Programming", "James Gosling", 5);
        admin.addBook(104, "Python Programming", "Guido van Rossum", 5);
        admin.addBook(105, "HTML & CSS", "Jon Duckett", 5);
        admin.addBook(106, "JavaScript", "Douglas Crockford", 5);
        admin.addBook(107, "MySQL Cookbook", "Paul DuBois", 5);
        admin.addBook(108, "Data Structures & Algorithms", "Robert Lafore", 5);

        adminBtn.addActionListener(e -> {
            new AdminFrame(admin);
            dispose();
        });

        userBtn.addActionListener(e -> {
            new UserDetailsFrame(admin.books, admin);
            dispose();
        });

        setVisible(true);
    }
}

// ------------------- User Details Frame -------------------
class UserDetailsFrame extends JFrame {
    public UserDetailsFrame(ArrayList<Book> books, Admin admin) {
        setTitle("Enter Your Details");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        JTextField nameField = new JTextField();
        JTextField rollField = new JTextField();
        JTextField collegeField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField branchField = new JTextField();
        JButton submitBtn = new JButton("Submit");

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Roll Number:"));
        panel.add(rollField);
        panel.add(new JLabel("College:"));
        panel.add(collegeField);
        panel.add(new JLabel("Year:"));
        panel.add(yearField);
        panel.add(new JLabel("Branch:"));
        panel.add(branchField);
        panel.add(new JLabel(""));
        panel.add(submitBtn);

        add(panel);

        submitBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String roll = rollField.getText().trim();
            String college = collegeField.getText().trim();
            String year = yearField.getText().trim();
            String branch = branchField.getText().trim();

            if (!name.isEmpty() && !roll.isEmpty() && !college.isEmpty() && !year.isEmpty() && !branch.isEmpty()) {
                User user = new User(name, roll, college, year, branch);
                admin.addUser(user);
                new UserFrame(new UserModule(books), user);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
            }
        });

        setVisible(true);
    }
}

// ------------------- Admin Frame -------------------
class AdminFrame extends JFrame {
    Admin admin;

    public AdminFrame(Admin admin) {
        this.admin = admin;

        setTitle("Admin Panel");
        setSize(650, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        // Add Book Panel
        JPanel addBookPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        JTextField idField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField copiesField = new JTextField();
        JButton addBookBtn = new JButton("Add Book");

        addBookPanel.add(new JLabel("Book ID:"));
        addBookPanel.add(idField);
        addBookPanel.add(new JLabel("Title:"));
        addBookPanel.add(titleField);
        addBookPanel.add(new JLabel("Author:"));
        addBookPanel.add(authorField);
        addBookPanel.add(new JLabel("Copies:"));
        addBookPanel.add(copiesField);
        addBookPanel.add(new JLabel(""));
        addBookPanel.add(addBookBtn);

        addBookBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                int copies = Integer.parseInt(copiesField.getText());
                admin.addBook(id, titleField.getText(), authorField.getText(), copies);
                JOptionPane.showMessageDialog(this, "Book added successfully!");
                idField.setText("");
                titleField.setText("");
                authorField.setText("");
                copiesField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter valid numeric values.");
            }
        });

        // Delete Book Panel
        JPanel deleteBookPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField delIdField = new JTextField();
        JButton deleteBookBtn = new JButton("Delete Book");

        deleteBookPanel.add(new JLabel("Book ID to Delete:"));
        deleteBookPanel.add(delIdField);
        deleteBookPanel.add(new JLabel(""));
        deleteBookPanel.add(deleteBookBtn);

        deleteBookBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(delIdField.getText());
                admin.deleteBook(id);
                JOptionPane.showMessageDialog(this, "Book deleted successfully!");
                delIdField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter valid numeric ID.");
            }
        });

        tabs.add("Add Book", addBookPanel);
        tabs.add("Delete Book", deleteBookPanel);

        add(tabs);
        setVisible(true);
    }
}

// ------------------- User Frame -------------------
class UserFrame extends JFrame {
    UserModule userModule;
    User user;
    DefaultTableModel tableModel;

    public UserFrame(UserModule userModule, User user) {
        this.userModule = userModule;
        this.user = user;

        setTitle("User Panel");
        setSize(800, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        // User Info Panel
        JPanel userInfoPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        userInfoPanel.add(new JLabel("Name: " + user.name));
        userInfoPanel.add(new JLabel("Roll Number: " + user.rollNo));
        userInfoPanel.add(new JLabel("College: " + user.college));
        userInfoPanel.add(new JLabel("Year: " + user.year));
        userInfoPanel.add(new JLabel("Branch: " + user.branch));

        // View Books Panel
        JPanel viewPanel = new JPanel(new BorderLayout());
        String[] columns = {"ID", "Title", "Author", "Available Copies"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable bookTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        JButton refreshBtn = new JButton("Refresh");

        viewPanel.add(scrollPane, BorderLayout.CENTER);
        viewPanel.add(refreshBtn, BorderLayout.SOUTH);
        refreshBtn.addActionListener(e -> refreshTable());

        // Issue/Return Panel
        JPanel issuePanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField issueIdField = new JTextField();
        JButton issueBtn = new JButton("Issue Book");
        JTextField returnIdField = new JTextField();
        JButton returnBtn = new JButton("Return Book");

        issuePanel.add(new JLabel("Book ID to Issue:"));
        issuePanel.add(issueIdField);
        issuePanel.add(new JLabel(""));
        issuePanel.add(issueBtn);
        issuePanel.add(new JLabel("Book ID to Return:"));
        issuePanel.add(returnIdField);
        issuePanel.add(new JLabel(""));
        issuePanel.add(returnBtn);

        issueBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(issueIdField.getText());
                boolean issued = userModule.issueBook(id);
                if (issued) JOptionPane.showMessageDialog(this, "Book issued successfully!");
                else JOptionPane.showMessageDialog(this, "No copies available or book not found.");
                issueIdField.setText("");
                refreshTable();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter valid numeric ID.");
            }
        });

        returnBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(returnIdField.getText());
                boolean returned = userModule.returnBook(id);
                if (returned) JOptionPane.showMessageDialog(this, "Book returned successfully!");
                else JOptionPane.showMessageDialog(this, "Book not found.");
                returnIdField.setText("");
                refreshTable();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter valid numeric ID.");
            }
        });

        tabs.add("User Info", userInfoPanel);
        tabs.add("View Books", viewPanel);
        tabs.add("Issue/Return Books", issuePanel);

        add(tabs);
        setVisible(true);

        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Book book : userModule.books) {
            tableModel.addRow(new Object[]{book.id, book.title, book.author, book.copies});
        }
    }
}
