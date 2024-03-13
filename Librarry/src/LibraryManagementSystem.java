import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


class Book {
    //using private data members for security
    private int bookId;
    private String title;
    private String author;
    private String genre;
    private boolean isBorrowed;
    //declared constructor to store the data of book
    public Book(int bookId, String title, String author, String genre) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isBorrowed = false;//initialized the availability to false
    }
    //used access modifier getter to use and update the private data types
    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }
    //Returns a string representation of the book information
    public String toString() {
        return "BOOK INFO" + "\n Book Id : " + bookId + "\nBook Title : " + title + "\nAuthor : " + author + "\nGenre : " + genre + "\nBorrowed : " + isBorrowed;
    }
}
//User class to store the data of user
class User {
    // using private data members for security
    private int userId;
    private String username;
    //Construct to store the data of user
    public User(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }
    //used access modifier getter to use and update the private data types
    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String toString() {
        return "USER INFO" + "\nUser Id : " + userId + "\nUsername : " + username;
    }
}

class Library {
    //list to store books
    private List<Book> books;
    //list to store users
    private List<User> users;
    //file to store book info
    private static final String BOOKS_FILE = "bookInfo.txt";
    //file to store user info
    private static final String USERS_FILE = "userInfo.txt";
    //file to store book Ids
    private static final String BOOK_IDS_FILE = "bookIds.txt";
    //file to store user ids
    private static final String USER_IDS_FILE = "userIds.txt";

    public Library() {
        this.books = new ArrayList<>();
        this.users = new ArrayList<>();
        loadBooks();
        loadUsers();
    }

    private void loadBooks() {
        try (Scanner scanner = new Scanner(new File(BOOKS_FILE))) {
            //splitting the book info in parts in order to perform various functions
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                int bookId = Integer.parseInt(parts[0].trim());
                String title = parts[1].trim();
                String author = parts[2].trim();
                String genre = parts[3].trim();
                boolean isBorrowed;
                try {
                    isBorrowed = Boolean.parseBoolean(parts[4].trim());
                } catch (Exception e) {
                    // Handle the exception, for now, set isBorrowed to false
                    isBorrowed = false;
                    System.err.println("Error parsing boolean value: " + e.getMessage());
                }
                Book book = new Book(bookId, title, author, genre);
                book.setBorrowed(isBorrowed);
                books.add(book);
            }
        } catch (FileNotFoundException e) {
            //Exception casre if the file is not found
            System.err.println("Error reading books file: " + e.getMessage());
        }
    }

    private void loadUsers() {
        //splitting the user info in parts in order to perform various functions
        try (Scanner scanner = new Scanner(new File(USERS_FILE))) {
            //while loop to read all the data from user file
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                int userId = Integer.parseInt(parts[0].trim());
                String username = parts[1].trim();
                User user = new User(userId, username);
                users.add(user);
            }
        } catch (FileNotFoundException e) {
            //Exception case if the file is not found
            System.err.println("Error reading users file: " + e.getMessage());
        }
    }

    private void saveBooks() {
        //Method to add new books into books file
        try (PrintWriter writer = new PrintWriter(new FileWriter(BOOKS_FILE))) {
            for (Book book : books) {
                writer.println(book.getBookId() + "," + book.getTitle() + "," + book.getAuthor() + "," + book.getGenre() + "," + book.isBorrowed());
            }
        } catch (IOException e) {
            System.err.println("Error writing books file: " + e.getMessage());
        }
    }

    private void saveUsers() {
        //Method to add new user into users file
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (User user : users) {
                writer.println(user.getUserId() + "," + user.getUsername());
            }
        } catch (IOException e) {
            System.err.println("Error writing users file: " + e.getMessage());
        }
    }
//Retrieves the next available book ID from a file, increments it, and returns the updated value.

    private int getNextBookId() {
        int nextBookId = 1;
        try (Scanner scanner = new Scanner(new File(BOOK_IDS_FILE))) {
            if (scanner.hasNextLine()) {
                nextBookId = Integer.parseInt(scanner.nextLine().trim());
            }
        } catch (FileNotFoundException e) {
            // Ignore for now; if the file doesn't exist, start from book ID 1.
        }
        return nextBookId;
    }
    //Returns the next available user ID from a file, increments it, and returns the updated value
    private int getNextUserId() {
        int nextUserId = 1;
        try (Scanner scanner = new Scanner(new File(USER_IDS_FILE))) {
            if (scanner.hasNextLine()) {
                nextUserId = Integer.parseInt(scanner.nextLine().trim());
            }
        } catch (FileNotFoundException e) {
            // Ignore for now; if the file doesn't exist, start from user ID 1.
        }
        return nextUserId;
    }
    //Saves the  next book id into the Book ids file
    private void saveNextBookId(int nextBookId) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BOOK_IDS_FILE))) {
            writer.println(nextBookId);
        } catch (IOException e) {
            System.err.println("Error writing bookIds file: " + e.getMessage());
        }
    }
    //Saves the  next book id into the Book ids file
    private void saveNextUserId(int nextUserId) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_IDS_FILE))) {
            writer.println(nextUserId);
        } catch (IOException e) {
            System.err.println("Error writing userIds file: " + e.getMessage());
        }
    }
    //Method to add new book
    public void addBook(String title, String author, String genre) {
        int nextBookId = getNextBookId();
        Book newBook = new Book(nextBookId, title, author, genre);
        books.add(newBook);
        saveBooks();
        //increment in book id
        saveNextBookId(nextBookId + 1);
        System.out.println("Book added successfully: " + newBook);
    }
    //Method to add new user

    public void addUser(String username) {
        int nextUserId = getNextUserId();
        User newUser = new User(nextUserId, username);
        users.add(newUser);
        saveUsers();
        //increment in user id
        saveNextUserId(nextUserId + 1);
        System.out.println("User added successfully: " + newUser);
    }
    //method to display all the available books
    public void displayBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
        } else {
            System.out.println("Books in the library:");
            //using enhanced for loop to read all the data from file and print it one by one
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }
    //method to display all the borrowed books
    public void displayBorrowedBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
        } else {
            System.out.println("Borrowed books:");
            for (Book book : books) {
                //using enhanced for loop to read all the data from file and print it one by one
                if (book.isBorrowed()) {
                    System.out.println(book);
                }
            }
        }
    }
    //Method to search borrowd books by user ID
    public void searchBooksByUserId(int userId) {
        boolean userFound = false;
        for (User user : users) {
            if (user.getUserId() == userId) {
                //Condition to check whether the user id is present or not
                userFound = true;
                //if user is found then prints the books borrowed by the user
                System.out.println("Books borrowed by user " + user.getUsername() + " (ID: " + user.getUserId() + "):");
                for (Book book : books) {
                    if (book.isBorrowed() && book.getBookId() == userId) {
                        System.out.println(book);
                    }
                }
                break;
            }
        }

        if (!userFound) {//if user is not found then prints the result
            System.out.println("User not found with ID: " + userId);
        }
    }

    public void borrowBook(int bookId, int userId) {
        // Check if both the book and user exist

        Book bookToBorrow = findBook(bookId);
        User user = findUser(userId);

        if (bookToBorrow != null && user != null) {
            if (!bookToBorrow.isBorrowed()) {
                //check if the book is already borrowed
                bookToBorrow.setBorrowed(true);
                saveBooks();
                // Update book status, save changes, and provide success messag
                System.out.println(user.getUsername() + " (ID: " + userId + ") has borrowed the book: " + bookToBorrow.getTitle());
            } else {
                System.out.println("Book is already borrowed.");
            }
        } else {//if book or user not found then displays the message
            if (bookToBorrow == null) {
                System.out.println("Book not found with ID: " + bookId);
            }

            if (user == null) {
                System.out.println("User not found with ID: " + userId);
            }
        }
    }
//Method for returning book
    public void returnBook(int bookId, int userId) {
        Book bookToReturn = findBook(bookId);
        User user = findUser(userId);

        if (bookToReturn != null && user != null) {
            if (bookToReturn.isBorrowed()) {
                bookToReturn.setBorrowed(false);//updates the availability status
                saveBooks();
                System.out.println(user.getUsername() + " (ID: " + userId + ") has returned the book: " + bookToReturn.getTitle());
            } else {
                System.out.println("Book is not currently borrowed.");
            }
        } else {
            if (bookToReturn == null) {
                System.out.println("Book not found with ID: " + bookId);
            }

            if (user == null) {
                System.out.println("User not found with ID: " + userId);
            }
        }
    }
//method to find book
    private Book findBook(int bookId) {
        for (Book book : books) {
            if (book.getBookId() == bookId) {
                return book;
            }
        }
        return null;
    }
    //method to find user

    private User findUser(int userId) {
        for (User user : users) {
            if (user.getUserId() == userId) {
                return user;
            }
        }
        return null;
    }
}

public class LibraryManagementSystem {

//main function
    public static void main(String[] args) {


        System.out.println(" \n");
        Library library = new Library();
        Scanner scanner = new Scanner(System.in);
//different functionalities option
        while (true) {
            System.out.println("\nLibrary Management System");
            System.out.println("1. Add Book");
            System.out.println("2. Add User");
            System.out.println("3. Display Books");
            System.out.println("4. Display Borrowed Books");
            System.out.println("5. Search Books by User ID");
            System.out.println("6. Borrow Book");
            System.out.println("7. Return Book");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    System.out.print("Enter Book Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter Author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter Genre: ");
                    String genre = scanner.nextLine();
                    library.addBook(title, author, genre);
                    break;

                case 2:
                    System.out.print("Enter User Name: ");
                    String username = scanner.nextLine();
                    library.addUser(username);
                    break;

                case 3:
                    library.displayBooks();
                    break;

                case 4:
                    library.displayBorrowedBooks();
                    break;

                case 5:
                    System.out.print("Enter User ID: ");
                    int searchUserId = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character
                    library.searchBooksByUserId(searchUserId);
                    break;

                case 6:
                    System.out.print("Enter Book ID to borrow: ");
                    int borrowBookId = scanner.nextInt();
                    System.out.print("Enter User ID: ");
                    int borrowUserId = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character
                    library.borrowBook(borrowBookId, borrowUserId);
                    break;

                case 7:
                    System.out.print("Enter Book ID to return: ");
                    int returnBookId = scanner.nextInt();
                    System.out.print("Enter User ID: ");
                    int returnUserId = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character
                    library.returnBook(returnBookId, returnUserId);
                    break;

                case 8:
                    System.out.println("Exiting Library Management System. Goodbye!");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }
}
